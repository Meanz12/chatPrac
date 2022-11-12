package com.websocket.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker // Stomp 사용
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }
    @Override
    public  void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*");
    }
}
