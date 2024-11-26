package com.findasitter.sitter.chat;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/message")
@CrossOrigin("http://localhost:4200")
public class ChatController {
    private final ChatRepository chatRepository;

    public ChatController(ChatRepository messageRepository) {
        this.chatRepository = messageRepository;
    }

    // Searches database to find chats with a specific userID
    @GetMapping("FindAllChats/{userID}")
    List<Chat> findAllChatsByUserID(@PathVariable Integer userID) {
//        System.out.println("1: " + userID);
        return chatRepository.findAllChatsByUserID(userID);
    }

    //Creates a row for Chat, and 2 for UserChat
    @PostMapping("/ChatCreate")
    void chatCreate(@RequestBody @RequestParam("p1") String id1, @RequestParam("p2") String id2) {
        chatRepository.createNewChat(parseInt(id1), parseInt(id2));
    }

    //Returns Chat based on requested Chat ID
    @GetMapping("UpdateChat/{chatID}")
    Chat returnChat(@PathVariable Integer chatID) {
        return chatRepository.returnChat(chatID);
    }

    @PostMapping("")
    void messageCreate(@RequestBody Message newMessage) {
        chatRepository.create(newMessage);
    }
}
