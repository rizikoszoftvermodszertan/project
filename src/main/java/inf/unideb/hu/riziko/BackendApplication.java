package inf.unideb.hu.riziko;

import inf.unideb.hu.riziko.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;

import inf.unideb.hu.riziko.controller.SocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Configuration
    @EnableWebSocket
    public class WebSocketConfiguration implements WebSocketConfigurer {
        @Autowired
        WebSocketHandler webSocketHandler;

        //public WebSocketConfiguration(WebSocketHandler webSocketHandler) {}

        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(webSocketHandler, "/socket").withSockJS();
        }


    }

}
