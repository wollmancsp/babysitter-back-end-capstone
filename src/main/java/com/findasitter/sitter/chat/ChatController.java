package com.findasitter.sitter.chat;

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

    @GetMapping("")
    void temp() {

    }

    // Searches database to find chats with a specific userID
    @GetMapping("{userID}")
    List<Chat> findAllChatsByUserID(@PathVariable Integer userID) {
//        System.out.println("1: " + userID);
        return chatRepository.findAllChatsByUserID(userID);
    }

    // Searches database to find messages with a specific chatID
//@GetMapping("{chatID}")
//    List<Message> findAllMessagesByChatID(@PathVariable Integer chatID) {
//        System.out.println("MSG: " + chatID);
//    return messageRepository.findAllMessagesByChatID(chatID);
//}
//
//    // new message added
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    void create(@Valid @RequestBody Message message) {
//        messageRepository.create(message);
//    }
}
