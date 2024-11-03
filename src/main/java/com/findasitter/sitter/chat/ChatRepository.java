package com.findasitter.sitter.chat;

import com.findasitter.sitter.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatRepository {
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcClient jdbcClient;
    public ChatRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Chat returnChat(Integer chatID) {
        String[] userIDList = jdbcClient.sql("SELECT user_id FROM UserChat WHERE chat_id = :chatID").param("chatID", chatID).query(String.class).stream().toArray(String[]::new);
        Message[] messageList = jdbcClient.sql("SELECT * FROM Message WHERE chat_id = :chatID").param("chatID", chatID).query(Message.class).stream().toArray(Message[]::new);
        return new Chat(chatID, userIDList, messageList);
    }

    public List<Chat> findAllChatsByUserID(Integer userID) {
        List<Chat> chatList = new ArrayList<>();
        List<Integer> chatIDs = jdbcClient.sql("SELECT chat_id FROM UserChat WHERE user_id = :userID").param("userID", userID).query(Integer.class).stream().toList();
        System.out.println("ChatID: " + chatIDs.getFirst());
        for(var chatID : chatIDs) {
            chatList.add(returnChat(chatID));
        }
        return chatList;
    }

    public void create(Message message) {
        var updated = jdbcClient.sql("INSERT INTO message(user_id, message_text, message_time, chat_id) values(?,?,?,?)")
                .params(List.of(message.user_id(), message.message_text(), message.message_time(), message.chat_id()))
                .update();

        Assert.state(updated == 1, "Failed to create user: " + message.message_text());
    }
}