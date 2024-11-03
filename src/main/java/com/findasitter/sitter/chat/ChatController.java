package com.findasitter.sitter.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/message")
@CrossOrigin("http://localhost:4200")


public class ChatController {
    private final ChatRepository chatRepository;

    public ChatController(ChatRepository messageRepository) {
        this.chatRepository = messageRepository;
    }

    // Searches database to find chats with a specific userID
    @GetMapping("{userID}")
    List<Chat> findAllChatsByUserID(@PathVariable Integer userID) {
//        System.out.println("1: " + userID);
        return chatRepository.findAllChatsByUserID(userID);
    }

//    @GetMapping("")
//    String temp(){
//        var cat = "temp";
//        return cat;
//    }

    //Returns Chat based on requested Chat ID
    @GetMapping("UpdateChat/{chatID}")
    Chat returnChat(@PathVariable Integer chatID) {
        return chatRepository.returnChat(chatID);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void messageCreate(@RequestBody Message newMessage) {
//        System.out.println("Message Received:" + newMessage.message_id() + " " + newMessage.user_id() + " " + newMessage.message_text() + " " + newMessage.message_time() + " " + newMessage.chat_id());
        chatRepository.create(newMessage);
    }
}
