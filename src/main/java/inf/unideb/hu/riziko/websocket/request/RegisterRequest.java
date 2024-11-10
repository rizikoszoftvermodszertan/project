package inf.unideb.hu.riziko.websocket.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import inf.unideb.hu.riziko.model.connection.User;
import inf.unideb.hu.riziko.websocket.response.Response;
import inf.unideb.hu.riziko.websocket.response.UserResponse;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;


public class RegisterRequest extends Request implements SessionDependent {
    @JsonProperty
    String username;
    @JsonIgnore
    @Setter
    WebSocketSession session;

    public Response respond() {
        User user = User.register(username, session);
        return new UserResponse(requestid, user);
    }
}
