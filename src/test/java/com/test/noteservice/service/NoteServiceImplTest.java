package com.test.noteservice.service;


import com.test.noteservice.exception.LikeException;
import com.test.noteservice.exception.NoteNotFoundException;
import com.test.noteservice.exception.UnlikeException;
import com.test.noteservice.exception.UserNotFoundException;
import com.test.noteservice.model.Like;
import com.test.noteservice.model.Note;
import com.test.noteservice.model.User;
import com.test.noteservice.repository.LikeRepository;
import com.test.noteservice.repository.NoteRepository;
import com.test.noteservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NoteServiceImplTest {

    @InjectMocks
    private NoteServiceImpl noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeRepository likeRepository;

    private Note note;

    @BeforeEach
    public void setUp() {
        note = Note.builder().id("1").content("Test").build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetNoteById() {
        when(noteRepository.findById("1")).thenReturn(Optional.of(note));

        assertEquals(note, noteService.getNoteById("1"));
    }

    @Test
    public void testGetNoteByIdNotFound() {
        when(noteRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> {
            noteService.getNoteById("1");
        });
    }

    @Test
    public void testGetAllNotes() {
        List<Note> notes = getListOfNotesWithIds12ContentsTest1Test2();

        when(noteRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Optional.of(notes));

        assertEquals(notes, noteService.getAllNotes());
    }

    @Test
    void createNote_shouldCreateNote_whenValidParametersArePassed() {
        String content = "Test";
        String userId = "1";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Note expectedNote = note;
        expectedNote.setUser(user);
        when(noteRepository.save(any())).thenReturn(expectedNote);

        Note createdNote = noteService.createNote(content, userId);

        assertEquals(expectedNote, createdNote);
    }

    @Test
    void createNote_shouldThrowUserNotFoundException_whenInvalidUserIdIsPassed() {
        String content = "Test note";
        String userId = "1";
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> noteService.createNote(content, userId));
    }

    @Test
    public void updateNote_ShouldReturnUpdatedNote_WhenNoteExists() {
        String noteId = "1";
        String content = "New Content";

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        when(noteRepository.save(note)).thenReturn(note);

        Note updatedNote = noteService.updateNote(noteId, content);

        assertEquals(content, updatedNote.getContent());
    }

    @Test
    public void updateNote_ShouldThrowNoteNotFoundException_WhenNoteDoesNotExist() {
        String noteId = "1";
        String content = "New Content";

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.updateNote(noteId, content));
    }

    @Test
    public void deleteNoteById_ShouldDeleteNote_WhenNoteExists() {
        String noteId = "1";

        when(noteRepository.existsById(noteId)).thenReturn(true);
        doNothing().when(noteRepository).deleteById(noteId);

        noteService.deleteNoteById(noteId);

        assertEquals(true, noteRepository.existsById(noteId));
    }

    @Test
    public void deleteNoteById_ShouldThrowNoteNotFoundException_WhenNoteDoesNotExist() {
        String noteId = "1";
        when(noteRepository.existsById(noteId)).thenReturn(false);

        assertThrows(NoteNotFoundException.class, () -> noteService.deleteNoteById(noteId));
    }

    @Test
    public void testAddLikeToNoteById() throws LikeException {
        String noteId = "123";
        String userId = "456";
        Like like = new Like(userId, noteId);
        when(likeRepository.existsByUserIdAndNoteId(userId, noteId)).thenReturn(false);

        noteService.addLikeToNoteById(noteId, userId);

        verify(likeRepository, times(1)).save(like);
    }

    @Test
    public void testAddLikeToNoteById_LikeException() throws LikeException {
        // Arrange
        String noteId = "123";
        String userId = "456";
        when(likeRepository.existsByUserIdAndNoteId(userId, noteId)).thenReturn(true);

        assertThrows(LikeException.class, () -> noteService.addLikeToNoteById(noteId, userId));
    }

    @Test
    public void testRemoveLikeFromNoteById() throws UnlikeException {
        String noteId = "123";
        String userId = "456";
        when(likeRepository.existsByUserIdAndNoteId(userId, noteId)).thenReturn(true);

        noteService.removeLikeFromNoteById(noteId, userId);

        verify(likeRepository, times(1)).deleteByUserIdAndNoteId(userId, noteId);
    }

    @Test
    public void testRemoveLikeFromNoteById_UnlikeException() throws UnlikeException {
        String noteId = "123";
        String userId = "456";
        when(likeRepository.existsByUserIdAndNoteId(userId, noteId)).thenReturn(false);

        assertThrows(UnlikeException.class, () -> noteService.removeLikeFromNoteById(noteId, userId));
    }

    private List<Note> getListOfNotesWithIds12ContentsTest1Test2() {
        return Arrays.asList(Note.builder().id("1").content("Test1").build(), Note.builder().id("2").content("Test2").build());
    }
}
