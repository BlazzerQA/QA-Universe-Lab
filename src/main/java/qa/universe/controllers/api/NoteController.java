package qa.universe.controllers.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qa.universe.dto.NoteResponse;
import qa.universe.models.Note;
import qa.universe.service.MarkdownService;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final MarkdownService markdownService;

    @GetMapping("/{category}/{note}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable String category, @PathVariable String note) {
        return ResponseEntity.ok(markdownService.getNote(category, note));
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(markdownService.getAllNotes());
    }
}
