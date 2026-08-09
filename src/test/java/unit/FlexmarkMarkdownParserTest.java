package unit;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FlexmarkMarkdownParserTest {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public FlexmarkMarkdownParserTest() {
        MutableDataSet options = new MutableDataSet();
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    private String toHtml(String markdown) {
        return renderer.render(parser.parse(markdown)).trim();
    }

    @Test
    void shouldRenderHeader() {
        String html = toHtml("# Java Collections");
        assertTrue(html.contains("<h1>Java Collections</h1>"));
    }

    @Test
    void shouldRenderSubHeader() {
        String html = toHtml("## ArrayList");
        assertTrue(html.contains("<h2>ArrayList</h2>"));
    }

    @Test
    void shouldRenderParagraph() {
        String html = toHtml("ArrayList — это реализация интерфейса List.");
        assertTrue(html.contains("<p>"));
        assertTrue(html.contains("</p>"));
        assertTrue(html.contains("ArrayList — это реализация интерфейса List."));
    }

    @Test
    void shouldRenderInlineCode() {
        String html = toHtml("Интерфейс `List`.");
        assertTrue(html.contains("<code>List</code>"));
    }

    @Test
    void shouldRenderUnorderedList() {
        String markdown = """
                - Быстрый доступ по индексу
                - Динамический размер
                """;
        String html = toHtml(markdown);
        assertTrue(html.contains("<ul>"));
        assertTrue(html.contains("<li>Быстрый доступ по индексу</li>"));
        assertTrue(html.contains("<li>Динамический размер</li>"));
        assertTrue(html.contains("</ul>"));
    }

    @Test
    void shouldRenderBoldText() {
        String html = toHtml("This is **Markdown**.");
        assertTrue(html.contains("<strong>Markdown</strong>"));
    }

    @Test
    void shouldRenderFencedCodeBlock() {
        String markdown = """
                ```java
                System.out.println("Hello");
                ```
                """;
        String html = toHtml(markdown);
        assertTrue(html.contains("<pre>"));
        assertTrue(html.contains("<code"));
        assertTrue(html.contains("System.out.println("));
    }

    @Test
    void shouldRenderComplexMarkdown() {
        String markdown = """
                # Hello

                This is **Markdown**.

                ```java
                System.out.println("Hello");
                ```
                """;
        String html = toHtml(markdown);
        assertTrue(html.contains("<h1>Hello</h1>"));
        assertTrue(html.contains("<strong>Markdown</strong>"));
        assertTrue(html.contains("<pre>"));
        assertTrue(html.contains("<code"));
    }
}
