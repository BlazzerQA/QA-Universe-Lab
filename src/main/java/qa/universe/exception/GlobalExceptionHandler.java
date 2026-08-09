package qa.universe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<?> handleNoteNotFound(NoteNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(Map.of("status", status.value(), "message", e.getMessage()));
    }

    @ExceptionHandler(PathTraversalException.class)
    public ResponseEntity<?> handlePathTraversal(PathTraversalException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(Map.of("status", status.value(), "message", e.getMessage()));
    }

    @ExceptionHandler(NoteReadException.class)
    public ResponseEntity<?> handleNoteRead(NoteReadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}
