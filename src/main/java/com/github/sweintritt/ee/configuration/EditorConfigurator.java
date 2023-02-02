package com.github.sweintritt.ee.configuration;

import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EditorConfigurator extends Configurator<RSyntaxTextArea> {

    private static final String CODE_FOLDING = "enable.code.folding";
    private static final String HIGHTLIGHT_CURRENT_LINE = "highlight.current.line";
    private static final String AUTO_INDENT = "enable.auto.indent";
    private static final String ANTI_ALIASING = "enable.anti.aliasing";
    private static final String BRACKET_MATCHING = "enable.bracket.matching";
    private static final String SHOW_WHITESPACES = "enable.show.whitespaces";
    private static final String MARK_OCCURENCES = "enable.mark.occurences";
    private static final String LINE_WRAP = "enable.line.wrap";
    private static final String TAB_SIZE = "tab.size";
    private static final String EDITOR_THEME = "editor.theme";
    private static final String THEME_PATH = "/org/fife/ui/rsyntaxtextarea/themes/%s.xml";

    public EditorConfigurator(final RSyntaxTextArea syntaxTextArea) {
        super("editor.config", syntaxTextArea, getDefaults());
    }

    @Override
    public void update() {
        log.debug("Configuring editor");
        try {
            Theme.load(getClass()
                            .getResourceAsStream(String.format(THEME_PATH, settings.get(EDITOR_THEME).getValue())))
                    .apply(object);
        } catch (final IOException e) {
            log.error("Unable to set theme: {}", e.getMessage(), e);
        }
        object.setCodeFoldingEnabled(Boolean.parseBoolean(settings.get(CODE_FOLDING).getValue()));
        object.setHighlightCurrentLine(Boolean.parseBoolean(settings.get(HIGHTLIGHT_CURRENT_LINE).getValue()));
        object.setAutoIndentEnabled(Boolean.parseBoolean(settings.get(AUTO_INDENT).getValue()));
        object.setAntiAliasingEnabled(Boolean.parseBoolean(settings.get(ANTI_ALIASING).getValue()));
        object.setBracketMatchingEnabled(Boolean.parseBoolean(settings.get(BRACKET_MATCHING).getValue()));
        object.setWhitespaceVisible(Boolean.parseBoolean(settings.get(SHOW_WHITESPACES).getValue()));
        object.setMarkOccurrences(Boolean.parseBoolean(settings.get(MARK_OCCURENCES).getValue()));
        object.setLineWrap(Boolean.parseBoolean(settings.get(LINE_WRAP).getValue()));
        object.setTabSize(Integer.parseInt(settings.get(TAB_SIZE).getValue()));
    }

    private static Map<String, Setting> getDefaults() {
        final Map<String, Setting> defaults = new HashMap<>();
        defaults.put(CODE_FOLDING, Setting.builder()
                .name(CODE_FOLDING)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.FALSE))
                .build());
        defaults.put(HIGHTLIGHT_CURRENT_LINE, Setting.builder()
                .name(HIGHTLIGHT_CURRENT_LINE)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(AUTO_INDENT, Setting.builder()
                .name(AUTO_INDENT)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(ANTI_ALIASING, Setting.builder()
                .name(ANTI_ALIASING)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(BRACKET_MATCHING, Setting.builder()
                .name(BRACKET_MATCHING)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(SHOW_WHITESPACES, Setting.builder()
                .name(SHOW_WHITESPACES)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(MARK_OCCURENCES, Setting.builder()
                .name(MARK_OCCURENCES)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(LINE_WRAP, Setting.builder()
                .name(LINE_WRAP)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(AUTO_INDENT, Setting.builder()
                .name(AUTO_INDENT)
                .type(Setting.Type.BOOLEAN)
                .defaultValue(String.valueOf(Boolean.TRUE))
                .build());
        defaults.put(TAB_SIZE, Setting.builder()
                .name(TAB_SIZE)
                .type(Setting.Type.INTEGER)
                .defaultValue("4")
                .build());
        defaults.put(EDITOR_THEME, Setting.builder()
                .name(EDITOR_THEME)
                .type(Setting.Type.STRING)
                .defaultValue("monokai")
                .possibleValues(Arrays.asList("dark", "default", "druid", "eclipse", "idea", "monokai"))
                .build());

        return defaults;
    }
}
