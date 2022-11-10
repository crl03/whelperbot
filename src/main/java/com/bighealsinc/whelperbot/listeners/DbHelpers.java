package com.bighealsinc.whelperbot.listeners;

import com.bighealsinc.whelperbot.entities.*;
import com.bighealsinc.whelperbot.services.GuildService;
import com.bighealsinc.whelperbot.services.RaidSchedulesService;
import com.bighealsinc.whelperbot.services.UserGuildsService;
import com.bighealsinc.whelperbot.services.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DbHelpers {

    private UserService userService;

    private GuildService guildService;

    private UserGuildsService userGuildsService;

    private RaidSchedulesService raidSchedulesService;

    public DbHelpers(UserService userService, GuildService guildService,
                     UserGuildsService userGuildsService, RaidSchedulesService raidSchedulesService) {
        this.userService = userService;
        this.guildService = guildService;
        this.userGuildsService = userGuildsService;
        this.raidSchedulesService = raidSchedulesService;
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

    public List<UserGuilds> getAllUsersGuild(long discordGuildId) {
        Optional<Guild> tempGuild = guildService.findByDiscordGuildId(discordGuildId);

        return userGuildsService.findAllByGuildId(tempGuild.get().getId());
    }

    public List<RaidSchedules> getGuildRaidSchedules(long userDiscordId, String userDiscordName, long discordGuildId) {
        checkUserAndGuild(userDiscordId, userDiscordName, discordGuildId);

        int guildDbId = guildService.findByDiscordGuildId(discordGuildId).get().getId();

        return raidSchedulesService.findAllByGuildId(guildDbId, Sort.by(Sort.Direction.ASC, "raidSchedulesPK"));
    }

    public List<RaidSchedules> getAllRaidSchedules() {
        return raidSchedulesService.findAll();
    }

    public RaidSchedulesPK createUserRaidSchedulePK(long userDiscordId, String userDiscordName, long discordGuildId, LocalDateTime dateTime) {
        checkUserAndGuild(userDiscordId, userDiscordName, discordGuildId);

        int guildDbId = guildService.findByDiscordGuildId(discordGuildId).get().getId();
        int userDBId = userService.findByDiscordId(userDiscordId).get().getId();

        RaidSchedulesPK newUserRaidSchedulePK = new RaidSchedulesPK(userDBId, guildDbId, dateTime);

        return newUserRaidSchedulePK;
    }

    public void saveNewUserRaidSchedule(RaidSchedulesPK raidSchedulesPK, String gameName, String serverName) {
        RaidSchedules newUserRaidSchedule = new RaidSchedules();

        newUserRaidSchedule.setRaidSchedulesPK(raidSchedulesPK);
        newUserRaidSchedule.setGame(gameName);
        newUserRaidSchedule.setGameServer(serverName);
        newUserRaidSchedule.setActive(true);

        updateRaidSchedule(newUserRaidSchedule);
    }

    public void updateRaidSchedule(RaidSchedules raid) {
        raidSchedulesService.save(raid);
    }

    public boolean deleteRaidSchedule(long userDiscordId, long discordGuildId, LocalDateTime dateTime) {
        Optional<User> userId = userService.findByDiscordId(userDiscordId);
        Optional<Guild> guildId = guildService.findByDiscordGuildId(discordGuildId);

        if (userId.isPresent() && guildId.isPresent()) {
            Optional<RaidSchedules> tempRaidSchedule = raidSchedulesService.findByCompositeId(userId.get().getId(), guildId.get().getId(), dateTime);
            if (tempRaidSchedule.isEmpty()) return false;
            tempRaidSchedule.ifPresent(raidSchedules -> raidSchedulesService.deleteByRaidSchedulesPK(raidSchedules.getRaidSchedulesPK()));
            return true;
        }
        return false;
    }

    public void incrementMessageCount(UserGuilds userGuild) {
        userGuild.setMessages(userGuild.getMessages() + 1);
        userGuildsService.save(userGuild);
    }

    public void incrementReactionCount(UserGuilds userGuild) {
        userGuild.setReactions(userGuild.getReactions() + 1);
        userGuildsService.save(userGuild);
    }

    public void incrementRaidScheduleCount(UserGuilds userGuild) {
        userGuild.setScheduledRaids(userGuild.getScheduledRaids() + 1);
        userGuildsService.save(userGuild);
    }

}
