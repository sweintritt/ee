package com.github.sweintritt.ee.actions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class SaveFile implements ActionListener {

    private final JFileChooser fileChooser;
    private final JTextArea textArea;

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // TODO (sweintritt) Check for present file
        final int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            final File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                 FileUtils.writeStringToFile(file, textArea.getText(), StandardCharsets.UTF_8);
            } catch (final Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
}
