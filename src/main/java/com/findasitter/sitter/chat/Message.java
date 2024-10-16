package com.findasitter.sitter.chat;
import jakarta.validation.constraints.NotEmpty;
import java.sql.Timestamp;

public record Message(
        Integer message_id,
        @NotEmpty Integer user_id,
        @NotEmpty String message_text,
        Timestamp message_time,
        Integer chat_id
) {}
