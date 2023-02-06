package com.github.sweintritt.ee;

import com.github.sweintritt.ee.configuration.Configurator;
import com.github.sweintritt.ee.configuration.EditorConfigurator;
import com.github.sweintritt.ee.configuration.SettingsService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Main {

    private static EditorFrame editorFrame;
    private static List<Configurator<?>> configurators = new LinkedList<>();

    // Main class
    public static void main(String args[]) {
        log.info("Starting...");
        SwingUtilities.invokeLater(() -> {
            try {
                log.debug("Setting up editor");
                editorFrame = new EditorFrame(configurators);
                log.debug("Loading configuration");
                loadConfiguration();
                log.debug("Configuring components");
                configurators.forEach(Configurator::update);
                log.debug("Ready...");
                editorFrame.setVisible(true);
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private static void loadConfiguration() throws IOException {
        configurators.add(new EditorConfigurator(editorFrame.getTextArea()));
        log.debug("Loading user settings");
        SettingsService.getInstance().load(configurators);
    }
}
