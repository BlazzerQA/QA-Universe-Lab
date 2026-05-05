package qa.universe.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

    private static final int MAX_WORD_LENGTH = 100;

    private final Map<String, String> dictionary = new LinkedHashMap<>();

    public DictionaryController() {
        dictionary.put("apple", "яблоко");
        dictionary.put("book", "книга");
        dictionary.put("car", "машина");
        dictionary.put("dog", "собака");
        dictionary.put("cat", "кошка");
        dictionary.put("house", "дом");
        dictionary.put("sun", "солнце");
        dictionary.put("water", "вода");
        dictionary.put("milk", "молоко");
        dictionary.put("school", "школа");
        dictionary.put("table", "стол");
        dictionary.put("friend", "друг");
    }

    @GetMapping("/translate")
    public ResponseEntity<?> translate(
            @RequestParam(required = false) List<String> word
    ) {

        if (word == null || word.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Parameter 'word' is required"
            ));
        }

        String requestWord = word.get(0);

        if (requestWord == null || requestWord.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Parameter 'word' is required"
            ));
        }

        String normalizedWord = requestWord
                .trim()
                .toLowerCase(Locale.ROOT);

        if (normalizedWord.length() >= MAX_WORD_LENGTH) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Word is too long"
            ));
        }

        String translation = dictionary.get(normalizedWord);

        if (translation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Word not found"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "english", normalizedWord,
                "russian", translation
        ));
    }
}