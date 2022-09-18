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
        defaults.put(EDITOR_THEME, Setting.builder()
                        .name(EDITOR_THEME)
                        .type(Setting.Type.STRING)
                        .defaultValue("monokai")
                        .possibleValues(Arrays.asList("dark", "default", "druid", "eclipse", "idea", "monokai"))
                        .build());
        return defaults;
    }
}
