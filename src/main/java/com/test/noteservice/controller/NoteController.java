package com.test.noteservice.controller;

import com.test.noteservice.exception.NoteNotFoundException;
import com.test.noteservice.model.Note;
import com.test.noteservice.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") String id) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestParam String content, String userId) {
        Note createdNote = noteService.createNote(content, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable("id") String id, @RequestBody String content) {
        Note updatedNote = noteService.updateNote(id, content);
        if (updatedNote == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable("id") String id) {
        try {
            noteService.deleteNoteById(id);
            return ResponseEntity.ok().build();
        }catch (NoteNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping("/{id}/like")
//    public ResponseEntity<Void> likeNoteById(@PathVariable("id") String id) {
//        if (noteService.addLikeToNoteById(id)) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }

//    @PostMapping("/{id}/unlike")
//    public ResponseEntity<Void> unlikeNoteById(@PathVariable("id") String id) {
//        if (noteService.unlikeNoteById(id)) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }

}