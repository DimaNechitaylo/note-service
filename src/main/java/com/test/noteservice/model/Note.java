package com.test.noteservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String content;
    private User user;
    private LocalDateTime createdAt;

}
