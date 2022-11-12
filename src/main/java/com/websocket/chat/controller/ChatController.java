package com.websocket.chat.controller;

import com.websocket.chat.dto.ChatMessage;
import com.websocket.chat.pubsub.RedisPublisher;
import com.websocket.chat.repository.ChatRoomRepository;
import com.websocket.chat.security.jwt.JwtTokenProvider;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ChatController(RedisPublisher redisPublisher,ChatRoomRepository chatRoomRepository,JwtTokenProvider jwtTokenProvider) {
        this.redisPublisher = redisPublisher;
        this.chatRoomRepository = chatRoomRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        if (ChatMessage.MessageType.ENTER.equals(message.getType()))
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(nickname + "님이 입장하셨습니다.");
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }
}
