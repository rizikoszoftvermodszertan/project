package inf.unideb.hu.riziko.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class WebSocketController extends TextWebSocketHandler {
    private final HashMap<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UUID uuid = UUID.fromString(message.getPayload());
        sessions.put(uuid.toString(), session);
        session.sendMessage(new TextMessage("Registration: OK"));
    }

    public void sendMessages(List<String> to, String message){
        sessions.entrySet().stream()
                .filter(entry -> to.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(session -> {
                    TextMessage textMessage = new TextMessage(message);
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


}
