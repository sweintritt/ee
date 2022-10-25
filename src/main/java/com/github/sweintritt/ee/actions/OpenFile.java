package com.github.sweintritt.ee.actions;

import com.github.sweintritt.ee.EditorFrame;
import lombok.RequiredArgsConstructor;

import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@RequiredArgsConstructor
public class OpenFile implements ActionListener {

    private final EditorFrame editor;
    private final JFileChooser fileChooser;

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final int r = fileChooser.showOpenDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            final File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            editor.openFile(file);
        }
    }
}
