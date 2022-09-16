package com.github.sweintritt.ee;

import com.github.sweintritt.ee.configuration.Configurator;
import com.github.sweintritt.ee.configuration.EditorConfigurator;
import lombok.extern.slf4j.Slf4j;

import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Main {

    private static EditorFrame editorFrame;
    private static List<Configurator<?>> configurators;

    // Main class
    public static void main(String args[]) {
        log.info("Starting...");
        SwingUtilities.invokeLater(() -> {
            try {
                log.debug("Setting up editor");
                editorFrame = new EditorFrame();
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

    private static void loadConfiguration() {
        configurators = Arrays.asList(
                new EditorConfigurator(editorFrame.getTextArea())
        );
    }
}
