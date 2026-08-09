package unit.service;

import org.junit.jupiter.api.Test;
import qa.universe.exception.NoteNotFoundException;
import qa.universe.exception.NoteReadException;
import qa.universe.service.MarkdownService;

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
}
