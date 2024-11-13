package com.findasitter.sitter.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/message")
@CrossOrigin("http://localhost:4200")
public class ChatController {
    private final ChatRepository chatRepository;

    public ChatController(ChatRepository messageRepository) {
        this.chatRepository = messageRepository;
    }
    //Returns Chat based on requested Chat ID
    @GetMapping("TestSubscribe")
    LocalDateTime returnTest() {
        System.out.println("Hit");
        return LocalDateTime.now();
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
        System.out.println("User ID 0: " + id1 + " User ID 1: " + id2);
        chatRepository.createNewChat(parseInt(id1), parseInt(id2));
    }

//    @PostMapping("/MessageCreate")
//    boolean messageCreate(@RequestBody Map<String, String> userIDPair) {
//        System.out.println("User ID 0: " + userIDPair.get("0") + " User ID 1: " + userIDPair.get("1"));
//        var map = new HashMap<Number, Number>();
//        map.put(0, parseInt(userIDPair.get("0")));
//        map.put(1, parseInt(userIDPair.get("1")));
//        chatRepository.createNewChat(map);
//        return true;
//    }
//    @GetMapping("MessageCreate/{userIDPair}")
//    void messageCreate(@PathVariable Number userIDPair) {
//        System.out.println("User ID 0: " + userIDPair);
//        chatRepository.createNewChat(userIDPair);
//    }

//    @PostMapping("/MessageCreate")
//    boolean messageCreate(@RequestBody Number num) {
//        System.out.println("Temp" + num);
//        return true;
//    }

//    @GetMapping("Temp")
//    boolean temp() {
//        System.out.println("Temp");
//        return true;
//    }



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
