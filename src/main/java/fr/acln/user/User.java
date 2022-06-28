package fr.acln.user;

import fr.acln.game.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username;

    @JsonbTransient
    private String password;

    @JsonbTransient
    private String token;

    @ManyToMany(fetch = EAGER)
    private Set<Game> games = new HashSet<>();

}
