package com.test.noteservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoteDto {
    private String id;
    private String content;
    private String username;
    private Long likesCount;
    private LocalDateTime createdAt;
}
