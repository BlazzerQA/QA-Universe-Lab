package unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import qa.universe.dto.NoteResponse;
import qa.universe.exception.NoteNotFoundException;
import qa.universe.exception.NoteReadException;
import qa.universe.models.Note;
import qa.universe.service.MarkdownService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarkdownServiceTest {

    private final MarkdownService markdownService = new MarkdownService("knowledge");

    @Test
    void shouldReturnHtmlForExistingNote() {
        String html = markdownService.getNoteContent("java/collections.md");

        assertTrue(html.contains("<h1>Java Collections</h1>"));
    }

    @Test
    void shouldConvertMarkdownToHtml() {
        String html = markdownService.getNoteContent("java/collections.md");

        assertTrue(html.contains("<h2>"));
        assertTrue(html.contains("<p>"));
        assertTrue(html.contains("<code>"));
    }

    @Test
    void shouldThrowExceptionWhenNoteNotFound() {
        assertThrows(NoteNotFoundException.class, () ->
                markdownService.getNoteContent("java/not-found.md"));
    }

    @Test
    void shouldRejectPathTraversal() {
        assertThrows(NoteReadException.class, () ->
                markdownService.getNoteContent("../../pom.xml"));
    }

    @Test
    void shouldFindNotesInMultipleCategories(@TempDir Path tempDir) throws IOException {
        createFile(tempDir, "java/collections.md");
        createFile(tempDir, "selenium/waits.md");
        createFile(tempDir, "interview/java.md");

        MarkdownService service = new MarkdownService(tempDir.toString());
        List<Note> notes = service.getAllNotes();

        assertEquals(3, notes.size());
        assertEquals("interview", notes.get(0).getCategory());
        assertEquals("java", notes.get(1).getCategory());
        assertEquals("selenium", notes.get(2).getCategory());
    }

    @Test
    void shouldFindNotesRecursively(@TempDir Path tempDir) throws IOException {
        createFile(tempDir, "java/basics/oop.md");

        MarkdownService service = new MarkdownService(tempDir.toString());
        List<Note> notes = service.getAllNotes();

        assertEquals(1, notes.size());
        assertEquals("Oop", notes.get(0).getTitle());
        assertEquals("java", notes.get(0).getCategory());
        assertEquals("java/basics/oop.md", notes.get(0).getPath());
    }

    @Test
    void shouldIgnoreNonMarkdownFiles(@TempDir Path tempDir) throws IOException {
        createFile(tempDir, "java/collections.md");
        createFile(tempDir, "java/image.png");
        createFile(tempDir, "java/example.txt");

        MarkdownService service = new MarkdownService(tempDir.toString());
        List<Note> notes = service.getAllNotes();

        assertEquals(1, notes.size());
        assertEquals("java/collections.md", notes.get(0).getPath());
    }

    @Test
    void shouldReturnEmptyListWhenNoNotes(@TempDir Path tempDir) {
        MarkdownService service = new MarkdownService(tempDir.toString());
        List<Note> notes = service.getAllNotes();

        assertTrue(notes.isEmpty());
    }

    @Test
    void shouldReturnNoteResponseForExistingNote() {
        NoteResponse response = markdownService.getNote("java", "collections");

        assertEquals("Java Collections", response.getTitle());
        assertEquals("java", response.getCategory());
        assertEquals("java/collections.md", response.getPath());
        assertTrue(response.getContent().contains("<h1>Java Collections</h1>"));
    }

    @Test
    void shouldThrowExceptionWhenGetNoteNotFound() {
        assertThrows(NoteNotFoundException.class, () ->
                markdownService.getNote("java", "unknown"));
    }

    @Test
    void shouldRejectPathTraversalInGetNote() {
        assertThrows(NoteReadException.class, () ->
                markdownService.getNote("../..", "pom"));
    }

    @Test
    void shouldHandleNoteNameWithExtension() {
        NoteResponse response = markdownService.getNote("java", "collections.md");

        assertEquals("Java Collections", response.getTitle());
        assertEquals("java/collections.md", response.getPath());
    }

    @Test
    void shouldReturnEmptyContentForEmptyNote(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("java/empty.md");
        Files.createDirectories(file.getParent());
        Files.createFile(file);

        MarkdownService service = new MarkdownService(tempDir.toString());
        NoteResponse response = service.getNote("java", "empty");

        assertEquals("", response.getContent());
        assertEquals("java/empty.md", response.getPath());
    }

    private void createFile(Path root, String relativePath) throws IOException {
        Path file = root.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.createFile(file);
    }
}
