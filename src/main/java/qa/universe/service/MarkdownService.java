package qa.universe.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import qa.universe.exception.NoteNotFoundException;
import qa.universe.exception.NoteReadException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MarkdownService {

    private final Path knowledgeRoot;
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService(@Value("${knowledge.path}") String knowledgePath) {
        this.knowledgeRoot = Paths.get(knowledgePath).toAbsolutePath().normalize();
        MutableDataSet options = new MutableDataSet();
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    public String getNoteContent(String relativePath) {
        Path resolved = knowledgeRoot.resolve(relativePath).normalize();

        if (!resolved.startsWith(knowledgeRoot)) {
            throw new NoteReadException("Path escapes knowledge directory: " + relativePath);
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
}
