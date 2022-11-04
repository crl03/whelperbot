package com.bighealsinc.whelperbot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_guilds")
//@IdClass(UserGuildsPK.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
public class UserGuilds implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private int userId;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "guild_id")
//    private int guildId;

    @EmbeddedId
    private UserGuildsPK userGuildsPK;

    @Column(name = "messages")
    private int messages;

    @Column(name = "reactions")
    private int reactions;

    @Column(name = "scheduled_raids")
    private int scheduledRaids;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.MERGE, CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.MERGE, CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "guild_id", insertable = false, updatable = false)
    @JsonBackReference
    private Guild guild;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserGuilds that = (UserGuilds) o;
        return userGuildsPK != null && Objects.equals(userGuildsPK, that.userGuildsPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userGuildsPK);
    }
}
