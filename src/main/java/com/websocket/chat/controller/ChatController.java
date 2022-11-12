package com.websocket.chat.controller;

import com.websocket.chat.dto.ChatMessage;
import com.websocket.chat.pubsub.RedisPublisher;
import com.websocket.chat.repository.ChatRoomRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    public ChatController(RedisPublisher redisPublisher,ChatRoomRepository chatRoomRepository) {
        this.redisPublisher = redisPublisher;
        this.chatRoomRepository = chatRoomRepository;
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType()))
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }
}
