package com.findasitter.sitter.transaction;

import java.time.LocalDateTime;

public record Transaction(
        Integer job_id,
        Integer job_parent,
        Integer job_sitter,
        String job_details,
        LocalDateTime job_created,
        LocalDateTime job_acceptedDate,
        LocalDateTime job_start,
        LocalDateTime job_end,
        Double job_pay,
        Integer job_status
) {}