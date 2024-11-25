package inf.unideb.hu.riziko.model.Lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    String name;
    @EqualsAndHashCode.Include
    String userId;

    public User(String name){
        this.name = name;
        userId = UUID.randomUUID().toString();
    }
}
