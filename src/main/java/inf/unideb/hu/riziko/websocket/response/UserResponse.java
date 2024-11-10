package inf.unideb.hu.riziko.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import inf.unideb.hu.riziko.model.connection.User;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponse extends Response {
    @JsonProperty
    User user;
    public UserResponse(int id, User user) {
        super(id, 200);
        this.user = user;
    }
}
