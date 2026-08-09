package unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import qa.universe.controllers.api.NoteController;
import qa.universe.dto.NoteResponse;
import qa.universe.exception.GlobalExceptionHandler;
import qa.universe.exception.NoteNotFoundException;
import qa.universe.exception.PathTraversalException;
import qa.universe.service.MarkdownService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private MarkdownService markdownService;

    @InjectMocks
    private NoteController noteController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnExistingNote() throws Exception {
        NoteResponse response = new NoteResponse(
                "Collections",
                "java",
                "java/collections.md",
                "<h1>Java Collections</h1>"
        );
        when(markdownService.getNote("java", "collections")).thenReturn(response);

        mockMvc.perform(get("/api/notes/java/collections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Collections"))
                .andExpect(jsonPath("$.category").value("java"))
                .andExpect(jsonPath("$.path").value("java/collections.md"))
                .andExpect(jsonPath("$.content").value("<h1>Java Collections</h1>"));
    }

    @Test
    void shouldReturn404WhenNoteNotFound() throws Exception {
        when(markdownService.getNote("java", "unknown"))
                .thenThrow(new NoteNotFoundException("Note not found"));

        mockMvc.perform(get("/api/notes/java/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Note not found"));
    }

    @Test
    void shouldReturn400ForPathTraversal() throws Exception {
        when(markdownService.getNote("..", "pom"))
                .thenThrow(new PathTraversalException("Path escapes knowledge directory"));

        mockMvc.perform(get("/api/notes/{category}/{note}", "..", "pom"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Path escapes knowledge directory"));
    }
}
