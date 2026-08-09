package qa.universe.service;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import qa.universe.dto.NoteResponse;
import qa.universe.exception.NoteNotFoundException;
import qa.universe.exception.NoteReadException;
import qa.universe.exception.PathTraversalException;
import qa.universe.models.Note;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class MarkdownService {

    private final Path knowledgeRoot;
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService(@Value("${knowledge.path}") String knowledgePath) {
        this.knowledgeRoot = Paths.get(knowledgePath).toAbsolutePath().normalize();
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, List.of(TablesExtension.create()));
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    public String getNoteContent(String relativePath) {
        Path resolved = knowledgeRoot.resolve(relativePath).normalize();

        if (!resolved.startsWith(knowledgeRoot)) {
            throw new PathTraversalException("Path escapes knowledge directory: " + relativePath);
        }

        if (!Files.exists(resolved) || !Files.isRegularFile(resolved)) {
            throw new NoteNotFoundException("Note not found: " + relativePath);
        }

        try {
            String markdown = Files.readString(resolved, StandardCharsets.UTF_8);
            return renderer.render(parser.parse(markdown)).trim();
        } catch (IOException e) {
            throw new NoteReadException("Failed to read note: " + relativePath, e);
        }
    }

    public NoteResponse getNote(String category, String noteName) {
        String baseName = stripExtension(noteName);
        String relativePath = category + "/" + baseName + ".md";
        String content = getNoteContent(relativePath);
        String title = extractTitleFromHtml(content);
        if (title == null) {
            title = capitalize(baseName);
        }
        return new NoteResponse(title, category, relativePath, content);
    }

    private String stripExtension(String fileName) {
        if (fileName != null && fileName.toLowerCase().endsWith(".md")) {
            return fileName.substring(0, fileName.length() - 3);
        }
        return fileName;
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    public List<Note> getAllNotes() {
        if (!Files.exists(knowledgeRoot) || !Files.isDirectory(knowledgeRoot)) {
            throw new NoteReadException("Knowledge directory not found: " + knowledgeRoot);
        }

        try (Stream<Path> paths = Files.walk(knowledgeRoot)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".md"))
                    .map(this::toNote)
                    .sorted(Comparator.comparing(Note::getCategory)
                            .thenComparing(Note::getTitle))
                    .toList();
        } catch (IOException e) {
            throw new NoteReadException("Failed to scan knowledge directory: " + knowledgeRoot, e);
        }
    }

    private Note toNote(Path file) {
        String relativePath = knowledgeRoot.relativize(file).toString().replace("\\", "/");
        String fileName = file.getFileName().toString();
        String fallbackTitle = fileName.substring(0, fileName.length() - 3);
        fallbackTitle = fallbackTitle.substring(0, 1).toUpperCase() + fallbackTitle.substring(1);

        String title = extractTitleFromFile(file);
        if (title == null) {
            title = fallbackTitle;
        }

        String category = "";
        int slashIndex = relativePath.indexOf('/');
        if (slashIndex > 0) {
            category = relativePath.substring(0, slashIndex);
        }

        return new Note(title, category, relativePath);
    }

    private String extractTitleFromFile(Path file) {
        try {
            String markdown = Files.readString(file, StandardCharsets.UTF_8);
            return extractTitle(markdown);
        } catch (IOException e) {
            return null;
        }
    }

    private String extractTitle(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return null;
        }
        Matcher matcher = Pattern.compile("^#\\s+(.+)$", Pattern.MULTILINE).matcher(markdown);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private String extractTitleFromHtml(String html) {
        if (html == null || html.isBlank()) {
            return null;
        }
        Matcher matcher = Pattern.compile("<h1[^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(html);
        if (matcher.find()) {
            return matcher.group(1).trim().replaceAll("<[^>]+>", "");
        }
        return null;
    }
}
