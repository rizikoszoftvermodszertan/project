package inf.unideb.hu.riziko.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import inf.unideb.hu.riziko.websocket.request.Request;
import inf.unideb.hu.riziko.websocket.request.SessionDependent;
import inf.unideb.hu.riziko.websocket.response.BadRequestResponse;
import inf.unideb.hu.riziko.websocket.response.Response;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;


public class SocketHandler extends TextWebSocketHandler {
    private static final JsonMapper objectMapper = JsonMapper.builder()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .build();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Request request;
        try{
            request = objectMapper.readValue(message.getPayload(), Request.class);
        }
        catch(Exception e){
            BadRequestResponse badRequestResponse = new BadRequestResponse();
            e.printStackTrace();
            sendResponse(session, badRequestResponse);
            return;
        }

        if(request instanceof SessionDependent sessionDependent) {
            sessionDependent.setSession(session);
        }
        Response response = request.respond();
        sendResponse(session, response);

    }

    private void sendResponse(WebSocketSession session, Response response) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        TextMessage textMessage = new TextMessage(jsonResponse);
        session.sendMessage(textMessage);
    }
}
