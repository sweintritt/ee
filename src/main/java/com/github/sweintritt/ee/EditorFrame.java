// Java Program to create a text editor using java
// Original Version from https://www.geeksforgeeks.org/java-swing-create-a-simple-text-editor/
package com.github.sweintritt.ee;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme;
import com.github.sweintritt.ee.actions.OpenSettings;
import com.github.sweintritt.ee.configuration.Configurator;
import com.github.sweintritt.ee.configuration.EditorConfigurator;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

// Editor Exampel - ee
@Slf4j
class EditorFrame extends JFrame implements ActionListener {
    @Getter
    private final RSyntaxTextArea textArea;
    private final JFileChooser fileChooser;

    private final List<Configurator<?>> configurators;

    public EditorFrame() throws IOException, URISyntaxException {
        super("EditorExample");

        configurators = new LinkedList<>();

        FlatLightLaf.setup();

        try {
            UIManager.setLookAndFeel( new FlatMonokaiProIJTheme() );
        } catch (final Exception e) {
            log.error("Unable to set look&feel: " + e.getMessage(), e);
        }

        JPanel cp = new JPanel(new BorderLayout());
        fileChooser = new JFileChooser();
        textArea = new RSyntaxTextArea(50, 150);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        configurators.add(new EditorConfigurator(textArea));

        final RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        cp.add(scrollPane);
        setContentPane(cp);

        final JMenu fileMenu = new JMenu("File");
        addItem(fileMenu, "New", this);
        addItem(fileMenu, "Open", this);
        addItem(fileMenu, "Save", this);
        addItem(fileMenu, "Settings", new OpenSettings(configurators));

        final JMenu editMenu = new JMenu("Edit");
        addItem(editMenu, "cut", this);
        addItem(editMenu, "copy", this);
        addItem(editMenu, "paste", this);

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

    private static void addItem(final JMenu menu, final String text, final ActionListener actionListener) {
        JMenuItem item = new JMenuItem(text);
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
        } else if (command.equals("Save")) {
            // Invoke the showsSaveDialog function to show the save dialog
            final int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                final File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try (FileWriter fileWriter = new FileWriter(file, false);
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                     bufferedWriter.write(textArea.getText());
                     bufferedWriter.flush();
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(this, evt.getMessage());
                }
            }
        } else if (command.equals("Open")) {
            // Invoke the showsOpenDialog function to show the save dialog
            final int r = fileChooser.showOpenDialog(null);

            // If the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // Set the label to the path of the selected directory
                final File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try (FileReader fileReader = new FileReader(file);
                     BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    final StringBuilder text = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        text.append(line).append(System.lineSeparator());
                    }

                    // Set the text
                    setTitle(file.getName());
                    textArea.setSyntaxEditingStyle(SyntaxStyleProvider.get(file));
                    // textArea.setDocument();
                    textArea.setText(text.toString()); // TODO (sweintritt) what to do with large files?
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(this, evt.getMessage());
                }
            }
        } else if (command.equals("New")) {
            textArea.setText("");
            this.setTitle("new");
        } else {
            log.error("Unknown command: " + command);
        }
    }
}
