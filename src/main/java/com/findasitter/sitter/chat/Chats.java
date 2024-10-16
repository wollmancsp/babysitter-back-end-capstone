package com.findasitter.sitter.chat;

// Stored in userchat table
public record Chats(
        Integer userchat_id,
        Integer user_id,
        Integer chat_id
) {}