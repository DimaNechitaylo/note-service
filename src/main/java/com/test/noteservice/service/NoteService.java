package com.test.noteservice.service;
import com.test.noteservice.model.Note;

import java.util.List;

public interface NoteService {
    Note getNoteById(String id);
    List<Note> getAllNotes();
    Note createNote(String content, String userId);
    Note updateNote(String noteId, String content);
    void deleteNoteById(String noteId);
    void addLikeToNoteById(String noteId, String userId);
    void removeLikeFromNoteById(String noteId, String userId);
}
