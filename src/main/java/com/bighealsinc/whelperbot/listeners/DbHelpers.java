package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.entities.Guild;
import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.bighealsinc.whelperbot.services.GuildService;
import com.bighealsinc.whelperbot.services.UserGuildsService;
import com.bighealsinc.whelperbot.services.UserService;
import discord4j.core.event.domain.message.ReactionAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DbHelpers {

    private UserService userService;

    private GuildService guildService;

    private UserGuildsService userGuildsService;

    public DbHelpers(UserService userService, GuildService guildService, UserGuildsService userGuildsService) {
        this.userService = userService;
        this.guildService = guildService;
        this.userGuildsService = userGuildsService;
    }

    public void checkUserAndGuild(long userDiscordId, String userDiscordName, long discordGuildId) {
        System.out.println("Inside DbHelpers checkUserAndGuild");
        System.out.println("Does user exist: " + userService.findByDiscordId(userDiscordId).isPresent());
        // query db table users with userDiscordId to find user's id in the database
        if (userService.findByDiscordId(userDiscordId).isEmpty()) {
            System.out.println("userService.findByDiscordId was null.");
            // if user doesn't exist add them
            User newUser = new User();
            newUser.setDiscordId(userDiscordId);
            newUser.setDiscordUserName(userDiscordName);
            System.out.println("Created new user with user discordId and discordName");
            userService.save(newUser);
            System.out.println("Saved new user.");

        }

        // query db table guilds with discordGuildId to find guild's id in the database
        if (guildService.findByDiscordGuildId(discordGuildId).isEmpty()) {

            // if guild doesn't exist add it and add user to guild
            Guild newGuild = new Guild();
            newGuild.setGuildId(discordGuildId);
            guildService.save(newGuild);
            newGuild.addUser(userService.findByDiscordId(userDiscordId).get());
            guildService.save(newGuild);

        } else {
            // check if existing guild has user, if not then add user to guild
            Guild tempGuild = guildService.findByDiscordGuildId(discordGuildId).get();
            Optional<User> tempUser = userService.findByDiscordId(userDiscordId);
            Set<User> tempGuildUsers = tempGuild.getGuildUsers();
            if (!tempGuildUsers.contains(tempUser.get())) {
                tempGuild.addUser(tempUser.get());
                guildService.save(tempGuild);
            }
        }



    }

    public UserGuilds getUserGuild(long userDiscordId, String userDiscordName, long discordGuildId) {
        System.out.println("Inside DbHelpers getUserGuild");
        checkUserAndGuild(userDiscordId, userDiscordName, discordGuildId);
        System.out.println("Finished running checkUserAndGuild");
        // find UserGuilds with info form previous queries
        int userId = userService.findByDiscordId(userDiscordId).get().getId();
        int guildId = guildService.findByDiscordGuildId(discordGuildId).get().getId();
        System.out.println("Found DB userId: " + userId);
        System.out.println("Found DB guildId: " + guildId);
        System.out.println("Returning UserGuilds: " + userGuildsService.findByCompositeId(userId, guildId));
        return userGuildsService.findByCompositeId(userId, guildId);
    }

    public void incrementMessageCount(UserGuilds userGuild) {
        userGuild.setMessages(userGuild.getMessages() + 1);
        userGuildsService.save(userGuild);
    }

    public void incrementReactionCount(UserGuilds userGuild) {
        userGuild.setReactions(userGuild.getReactions() + 1);
        userGuildsService.save(userGuild);
    }

    public void incrementRaidScheduleCount() {

    }
}
