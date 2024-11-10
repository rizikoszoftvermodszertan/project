package inf.unideb.hu.riziko.model.connection;

import ch.qos.logback.core.joran.sanity.Pair;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.*;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Setter
    @Getter
    @JsonProperty
    private String displayName;
    @EqualsAndHashCode.Include
    @JsonProperty
    @Getter
    private final String id;
    @Setter
    private WebSocketSession session;

    private static final HashMap<String, User> users = new HashMap<>();

    private User(String displayName,WebSocketSession session){
        this.displayName = displayName;
        this.id = generateId();
        this.session = session;
        users.put(id, this);
    }

    private String generateId(){
        while(true){
            String uuid = UUID.randomUUID().toString();
            if(users.keySet().stream().noneMatch(user -> Objects.equals(user, uuid))){
                return uuid;
            }
        }
    }

    public static User register(String displayName, WebSocketSession session){
        User user = new User(displayName,session);
        users.put(user.id, user);
        return user;
    }

    public static User reconnect(String id, WebSocketSession session){
        if(!users.containsKey(id)){
            return null;
        }
        users.get(id).session = session;
        return users.get(id);
    }

    public static User rename(String id, String displayName){
        if(!users.containsKey(id)){
            throw new IllegalArgumentException("User not found");
        }
        users.get(id).displayName = displayName;
        return users.get(id);
    }

    public static void removeBySession(WebSocketSession session){
        for(Map.Entry<String, User> pair : users.entrySet()){
            if(Objects.equals(pair.getValue().session, session)){
                pair.getValue().session = null;
                return;
            }
        }
    }

}
