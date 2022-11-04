package com.bighealsinc.whelperbot.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "guilds")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
public class Guild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "guild_id", unique = true)
    private long guildId;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "user_guilds",
            joinColumns = @JoinColumn(name = "guild_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    @ToString.Exclude
    private Set<User> guildUsers = new HashSet<>();

    public void addUser(User user) {
        if (guildUsers == null) {
            guildUsers = new HashSet<>();
        }

        guildUsers.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Guild guild = (Guild) o;
//        return id != null && Objects.equals(id, guild.id);
        return Objects.equals(id, guild.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
