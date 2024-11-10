package inf.unideb.hu.riziko.websocket.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import inf.unideb.hu.riziko.model.connection.User;
import inf.unideb.hu.riziko.websocket.response.NotFoundResponse;
import inf.unideb.hu.riziko.websocket.response.Response;
import inf.unideb.hu.riziko.websocket.response.UserResponse;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

public class ReconnectRequest extends Request implements SessionDependent{
    @JsonProperty
    String userId;
    @Setter
    WebSocketSession session;
    @Override
    public Response respond() {
        User user = User.reconnect(userId, session);
        if(user == null){
            return new NotFoundResponse(requestid);
        }
        return new UserResponse(requestid, user);
    }
}
