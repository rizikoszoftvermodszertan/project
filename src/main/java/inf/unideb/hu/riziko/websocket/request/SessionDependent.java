package inf.unideb.hu.riziko.websocket.request;

import org.springframework.web.socket.WebSocketSession;

public interface SessionDependent {
    void setSession(WebSocketSession session);
}
