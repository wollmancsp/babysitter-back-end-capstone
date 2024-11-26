package com.findasitter.sitter.chat;

public record Chat(
        Integer chat_id,
        String[] users_id_array,
        Message[] messages_array
) {}