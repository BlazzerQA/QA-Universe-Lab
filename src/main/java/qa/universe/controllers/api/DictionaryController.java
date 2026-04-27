package qa.universe.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

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
    public ResponseEntity<?> translate(@RequestParam(required = false) String word) {
        if (word == null || word.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Parameter 'word' is required"
            ));
        }

        String translation = dictionary.get(word.toLowerCase());

        if (translation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Word not found"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "english", word.toLowerCase(),
                "russian", translation
        ));
    }
}