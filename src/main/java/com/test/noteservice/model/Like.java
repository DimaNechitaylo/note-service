package com.test.noteservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "likes")
public class Like {
    @Id
    private String id;
    private String userId;
    private String noteId;

    public Like(String userId, String noteId) {
        this.userId = userId;
        this.noteId = noteId;
    }
}
