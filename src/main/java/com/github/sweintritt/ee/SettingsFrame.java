package com.github.sweintritt.ee;

import java.awt.Button;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;

public class SettingsFrame extends JFrame {

    private enum SettingsType {
        BOOLEAN, STRING, INTEGER, LIST
    }

    private static class Setting {
        public String name;
        public String value;
        public SettingsType type;
        public String description;

        //Transienat
        public String defaultValue;
        //Transienat
        public Consumer<String> validator;
        // Optional List of possible values
        //Transienat
        public List<String> possibleValues;

        public String getValue() {
            if (value == null) {
                return defaultValue;
            }

            return value;
        }
    }

    public SettingsFrame() {
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // indicates terminate operation on close of window
        GridLayout gl = new GridLayout(3, 2);// create grid layout frame
        setLayout(gl);
        setTitle("Demo For Grid Layout");
        setSize(200, 200);
        add(new Button("Button A"));
        add(new Button("Button B"));
        add(new Button("Button C"));
        add(new Button("Button D"));
        gl.setRows(gl.getRows() + 1);
        add(new Button("Button E"));
        gl.setRows(gl.getRows() + 1);
        add(new Button("Button F"));
        gl.setRows(gl.getRows() + 1);
    }

    private static Map<String, List<Setting>> getSetttings() {
        final Setting showLineNumbers = new Setting();
        showLineNumbers.defaultValue = Boolean.TRUE.toString().toLowerCase();
        showLineNumbers.name = "Show line numbers";
        showLineNumbers.description = "Display line numbers in the editor";
        showLineNumbers.type = SettingsType.BOOLEAN;
        showLineNumbers.validator = v -> {
            if (v == null || Boolean.valueOf(v) == null) {
                throw new IllegalArgumentException("Null not allowed");
            }
        };

        final Map<String, List<Setting>> settings = new HashMap<>();
        settings.put("editor", Arrays.asList(showLineNumbers));
        return settings;

    }
}