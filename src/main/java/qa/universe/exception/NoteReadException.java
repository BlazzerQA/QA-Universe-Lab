package qa.universe.exception;

public class NoteReadException extends RuntimeException {

    public NoteReadException(String message) {
        super(message);
    }

    public NoteReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
