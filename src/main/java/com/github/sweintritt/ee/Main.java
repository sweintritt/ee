package com.github.sweintritt.ee;

import lombok.extern.slf4j.Slf4j;

import javax.swing.SwingUtilities;

@Slf4j
public class Main {

    // Main class
    public static void main(String args[]) {
        log.info("Starting...");
        SwingUtilities.invokeLater(() -> {
            try {
                new EditorFrame().setVisible(true);
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
