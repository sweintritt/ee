// Java Program to create a text editor using java
// Original Version from https://www.geeksforgeeks.org/java-swing-create-a-simple-text-editor/
package com.github.sweintritt.ee;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme;
import com.github.sweintritt.ee.actions.OpenFile;
import com.github.sweintritt.ee.actions.OpenSettings;
import com.github.sweintritt.ee.actions.SaveFile;
import com.github.sweintritt.ee.configuration.Configurator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

// Editor Exampel - ee
@Slf4j
class EditorFrame extends JFrame implements ActionListener {
    private final JPanel panel;
    @Getter
    private final RSyntaxTextArea textArea;
    private final JFileChooser fileChooser;

    public EditorFrame(final List<Configurator<?>> configurators) throws IOException, URISyntaxException {
        super("EditorExample");

        FlatLightLaf.setup();

        try {
            UIManager.setLookAndFeel( new FlatMonokaiProIJTheme() );
        } catch (final Exception e) {
            log.error("Unable to set look&feel: " + e.getMessage(), e);
        }

        panel = new JPanel(new BorderLayout());
        fileChooser = new JFileChooser();
        textArea = new RSyntaxTextArea(50, 150);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);

        final RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        setContentPane(panel);

        final JMenu fileMenu = new JMenu("File");
        addItem(fileMenu, "New", this, KeyEvent.VK_N);
        addItem(fileMenu, "Open", new OpenFile(this, fileChooser, textArea), KeyEvent.VK_O);
        addItem(fileMenu, "Save", new SaveFile(fileChooser, textArea), KeyEvent.VK_S);
        addItem(fileMenu, "Settings", new OpenSettings(configurators), KeyEvent.VK_P);

        final JMenu editMenu = new JMenu("Edit");
        addItem(editMenu, "cut", this, KeyEvent.VK_X);
        addItem(editMenu, "copy", this, KeyEvent.VK_C);
        addItem(editMenu, "paste", this, KeyEvent.VK_V);

        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);

        setIconImage(ImageIO.read(new File(this.getClass().getClassLoader().getResource("icon_64.png").toURI())));
        setSize(500, 500);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static void addItem(final JMenu menu, final String text, final ActionListener actionListener, final int keyEvent) {
        final JMenuItem item = new JMenuItem(text);
        item.setMnemonic(keyEvent);
        item.setAccelerator(KeyStroke.getKeyStroke(keyEvent, InputEvent.CTRL_DOWN_MASK));
        item.addActionListener(actionListener);
        menu.add(item);
    }

    public void actionPerformed(final ActionEvent e) {
        final String command = e.getActionCommand();

        if (command.equals("cut")) {
            textArea.cut();
        } else if (command.equals("copy")) {
            textArea.copy();
        } else if (command.equals("paste")) {
            textArea.paste();
        } else if (command.equals("New")) {
            textArea.setText("");
            this.setTitle("new");
        } else {
            log.error("Unknown command: " + command);
        }
    }
}
