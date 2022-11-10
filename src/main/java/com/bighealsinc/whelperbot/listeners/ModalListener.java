package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.entities.RaidSchedulesPK;
import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.TextInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public abstract class ModalListener {

    @Autowired
    private DbHelpers dbHelpers;

    @Value("&appid=${openWeatherToken}")
    private String openWeatherToken;



    public Mono<Void> processModal(ModalSubmitInteractionEvent event) {
        Mono<Void> result = null;

        switch (event.getCustomId()) {
            case "raid" -> result = processRaid(event);
            case "weather" -> result = processWeather(event);
            case "deleteraid" -> result = processDeleteRaid(event);
        }

        return result;
    }

    private Mono<Void> processRaid(ModalSubmitInteractionEvent event) {
        long userDiscordId = event.getInteraction().getUser().getId().asLong();
        String userDiscordName = event.getInteraction().getUser().getUsername();
        long discordGuildId = event.getInteraction().getGuildId().get().asLong();
        String gameName = "";
        String serverName = "";
        String date = "";
        String time = "";

        for (TextInput component : event.getComponents(TextInput.class)) {
            switch (component.getCustomId()) {
                case "game-name" -> gameName = component.getValue().get();
                case "server-name" -> serverName = component.getValue().get();
                case "date" -> date = component.getValue().get();
                case "time" -> time = component.getValue().get();
            }
        }

        System.out.println("game-name: " + gameName);
        System.out.println("server-name: " + serverName);
        System.out.println("date: " + date);
        System.out.println("time:" + time);

        LocalDateTime dateTime = getDateTime(date, time);


        RaidSchedulesPK tempUserRaidSchedulePK = dbHelpers.createUserRaidSchedulePK(userDiscordId, userDiscordName, discordGuildId, dateTime);

        dbHelpers.saveNewUserRaidSchedule(tempUserRaidSchedulePK, gameName, serverName);

        UserGuilds userGuild = dbHelpers.getUserGuild(userDiscordId, userDiscordName, discordGuildId);

        dbHelpers.incrementRaidScheduleCount(userGuild);

        return event.reply()
                .withEphemeral(true)
                .withContent("Raid has been scheduled.");

    }

    private Mono<Void> processDeleteRaid(ModalSubmitInteractionEvent event) {
        String message = "Raid deleted.";
        long userDiscordId = event.getInteraction().getUser().getId().asLong();
        long discordGuildId = event.getInteraction().getGuildId().get().asLong();
        String date = "";
        String time = "";

        for (TextInput component : event.getComponents(TextInput.class)) {
            switch (component.getCustomId()) {
                case "date" -> date = component.getValue().get();
                case "time" -> time = component.getValue().get();
            }
        }

        LocalDateTime dateTime = getDateTime(date, time);

        boolean deleted = dbHelpers.deleteRaidSchedule(userDiscordId, discordGuildId, dateTime);

        if (!deleted) {
            message = "You have no raid scheduled at this date and time.";
        };

        return event.reply()
                .withEphemeral(true)
                .withContent(message);
    }

    private static LocalDateTime getDateTime(String date, String time) {
        StringBuilder combinedDateTime = new StringBuilder();
        String[] formatDate = date.split("/");
        String[] formatTime = time.split("[:\s]");
        combinedDateTime.append("20");
        combinedDateTime.append(formatDate[2]);
        combinedDateTime.append("-");
        combinedDateTime.append(formatDate[0]);
        combinedDateTime.append("-");
        combinedDateTime.append(formatDate[1]);
        combinedDateTime.append("T");
        if (formatTime[2].equalsIgnoreCase("pm") && !formatTime[0].equalsIgnoreCase("12")) {
            String hours = String.valueOf((12 + Integer.parseInt(formatTime[0])));
            combinedDateTime.append(hours);
        } else if (formatTime[2].equalsIgnoreCase("am") && formatTime[0].equalsIgnoreCase("12")) {
            combinedDateTime.append("00");
        } else {
            combinedDateTime.append(formatTime[0]);
        }
        combinedDateTime.append(":");
        combinedDateTime.append(formatTime[1]);
        combinedDateTime.append(":00");

        System.out.println("Combined Date and Time: " + combinedDateTime);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse(combinedDateTime, formatter);
        return dateTime;
    }

    private Mono<Void> processWeather(ModalSubmitInteractionEvent event) {
        String zipCode = "";
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (component.getCustomId().equalsIgnoreCase("zipcode")) {
                zipCode = component.getValue().get();
                break;
            }
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest zipCodeRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://api.openweathermap.org/geo/1.0/zip?zip="+ zipCode + ",US" + openWeatherToken))
                .GET()
                .build();
        HttpResponse<String> zipCodeResponse;
        HttpResponse<String> weatherResponse;
        String lat = "";
        String lon = "";
        String message = "";
        String city = "";

        try {
            System.out.println(zipCodeRequest.uri());
            zipCodeResponse = client.send(zipCodeRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(zipCodeResponse.body());
            if (zipCodeResponse.statusCode() == 404) {
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Cannot find zip code.");
            }
            JsonNode tempNode = new ObjectMapper().readTree(zipCodeResponse.body());
            lat = tempNode.get("lat").asText();
            lon = tempNode.get("lon").asText();
            city = tempNode.get("name").asText();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("LAT: " + lat);
        System.out.println("LON: " + lon);
        HttpRequest weatherRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat="+ lat +"&lon=" + lon + openWeatherToken + "&units=imperial"))
                .build();

        try {
            weatherResponse = client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode tempNode = new ObjectMapper().readTree(weatherResponse.body());
            System.out.println(weatherResponse.body());
            StringBuilder tempString = new StringBuilder();
            tempString.append("Current weather in ").append(city).append(":\n")
//                    .append(tempNode.at("/weather").get(0).get("main").asText()).append(":\t\t ")
                    .append("Overall:\t\t ")
                    .append((tempNode.at("/weather").get(0).get("description").asText()).substring(0,1).toUpperCase())
                    .append((tempNode.at("/weather").get(0).get("description").asText()).substring(1)).append("\n")
                    .append("Temp:\t\t\t").append(tempNode.at("/main/temp").asText()).append("\n")
                    .append("Feels Like:\t ").append(tempNode.at("/main/feels_like").asText()).append("\n")
                    .append("Min Temp:\t").append(tempNode.at("/main/temp_min").asText()).append("\n")
                    .append("Max Temp:   ").append(tempNode.at("/main/temp_max").asText()).append("\n")
                    .append("Humidity:\t ").append(tempNode.at("/main/humidity").asText()).append("%\n")
                    .append("Wind:\t\t\t").append(tempNode.at("/wind/speed").asText()).append("mph");
            message = tempString.toString();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return event.reply()
                .withEphemeral(true)
                .withContent(message);
    }
}
