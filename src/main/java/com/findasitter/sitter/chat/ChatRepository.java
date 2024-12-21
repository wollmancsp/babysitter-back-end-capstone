package com.findasitter.sitter.chat;

import com.findasitter.sitter.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatRepository {
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcClient jdbcClient;
    public ChatRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Chat> findAllChatsByUserID(Integer userID) {
        List<Chat> chatList = new ArrayList<>();
        List<Integer> chatIDs = jdbcClient.sql("SELECT chat_id FROM UserChat WHERE user_id = :userID").param("userID", userID).query(Integer.class).list();
        for(var chatID : chatIDs) {
            chatList.add(returnChat(chatID));
        }
        return chatList;
    }

    public boolean createNewChat(Integer id1, Integer id2) {
        if(!UserAlreadyHasChat(id1, id2)) {
            String[] chatIDList = jdbcClient.sql("SELECT * FROM chat")
                    .query(String.class).stream().toArray(String[]::new);
            Integer new_chat_id = chatIDList.length + 1;
            jdbcClient.sql("INSERT INTO chat(chat_id) values(?)")
                    .params(List.of(new_chat_id)).update();
            jdbcClient.sql("INSERT INTO userchat(user_id, chat_id) values(?,?)")
                    .params(List.of(id1, new_chat_id))
                    .update();
            jdbcClient.sql("INSERT INTO userchat(user_id, chat_id) values(?,?)")
                    .params(List.of(id2, new_chat_id))
                    .update();
        }else {
            System.out.println("User already has chat");
        }
        return true;
    }

    //True means chat already exists between the two users
    public boolean UserAlreadyHasChat(Integer id1, Integer id2) {
        List<Integer> user1ChatIDs = jdbcClient.sql("SELECT chat_id FROM UserChat WHERE user_id = :userID").param("userID", id1).query(Integer.class).stream().toList();
        //Yeah this is inefficient, blah blah blah. IT WORKS THO
        List<Integer> allUsersInChatWithUser1 = new ArrayList<>();
        for(Integer chatID : user1ChatIDs) {
            List<Integer> oneChatUserIDs = jdbcClient.sql("SELECT user_id FROM UserChat WHERE chat_id = :chatID").param("chatID", chatID).query(Integer.class).stream().toList();
            allUsersInChatWithUser1.addAll(oneChatUserIDs);
        }
        if(allUsersInChatWithUser1.contains(id2)) {
            return true;
        }else {
            return false;
        }
    }

    public Chat returnChat(Integer chatID) {
        String[] userIDList = jdbcClient.sql("SELECT user_id FROM UserChat WHERE chat_id = :chatID").param("chatID", chatID).query(String.class).list().toArray(String[]::new);
        Message[] messageList = jdbcClient.sql("SELECT * FROM Message WHERE chat_id = :chatID").param("chatID", chatID).query(Message.class).list().toArray(new Message[0]);
        return new Chat(chatID, userIDList, messageList);
    }

    public void create(Message message) {
        jdbcClient.sql("INSERT INTO message(user_id, message_text, message_time, chat_id) values(?,?,?,?)")
                .params(List.of(message.user_id(), message.message_text(), message.message_time(), message.chat_id()))
                .update();
    }
}