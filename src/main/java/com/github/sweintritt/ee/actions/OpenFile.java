package com.github.sweintritt.ee.actions;

import com.github.sweintritt.ee.SyntaxStyleProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class OpenFile implements ActionListener {

    private final JFrame frame;
    private final JFileChooser fileChooser;
    private final RSyntaxTextArea textArea;

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final int r = fileChooser.showOpenDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            final File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

            try {
                final String text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                frame.setTitle(file.getName());
                textArea.setSyntaxEditingStyle(SyntaxStyleProvider.get(file));
                // TODO textArea.setDocument();
                textArea.setText(text); // TODO (sweintritt) what to do with large files?
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(null, evt.getMessage());
            }
        }
    }
}
