// Java Program to create a text editor using java
// Original Version from https://www.geeksforgeeks.org/java-swing-create-a-simple-text-editor/
package com.github.sweintritt.ee;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme;
import com.formdev.flatlaf.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
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
import java.net.URLConnection;
import java.nio.file.Files;

// Editor Exampel - ee
@Slf4j
class EditorFrame extends JFrame implements ActionListener {
    private final RSyntaxTextArea textArea;
    private final JFileChooser fileChooser;

    public EditorFrame() throws IOException, URISyntaxException {
        super("EditorExample");
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
        Theme.load(getClass()
                        .getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"))
                .apply(textArea);
        textArea.setCodeFoldingEnabled(true);
        final RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        cp.add(scrollPane);
        setContentPane(cp);

        // Create a menubar
        JMenuBar mb = new JMenuBar();

        // Create amenu for menu
        JMenu m1 = new JMenu("File");

        // Create menu items
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");

        // Add action listener
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);

        // Create amenu for menu
        JMenu m2 = new JMenu("Edit");

        // Create menu items
        JMenuItem mi4 = new JMenuItem("cut");
        JMenuItem mi5 = new JMenuItem("copy");
        JMenuItem mi6 = new JMenuItem("paste");

        // Add action listener
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);

        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);

        mb.add(m1);
        mb.add(m2);

        setJMenuBar(mb);
        setIconImage(ImageIO.read(new File(this.getClass().getClassLoader().getResource("icon_64.png").toURI())));
        setSize(500, 500);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
                    textArea.setSyntaxEditingStyle(getSyntaxStyle(file));
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

    // TODO (sweintritt) does not work correctly
    private static String getSyntaxStyle(final File file) {
        try {
            String type = URLConnection.guessContentTypeFromName(file.getName());

            if (StringUtils.isEmpty(type)) {
                type = Files.probeContentType(file.toPath());
            }

            if (StringUtils.isEmpty(type)) {
                type = URLConnection.getFileNameMap().getContentTypeFor(file.getName());
            }

            log.debug("Content type set to {}", type);
            type = type.replace("application", "text");
            log.debug("Content type set to {}", type);
            return type;
        } catch (final Exception e) {
            log.error("Unable to get file type: {}", e.getMessage(), e);
            return  SyntaxConstants.SYNTAX_STYLE_NONE;
        }
    }
}
