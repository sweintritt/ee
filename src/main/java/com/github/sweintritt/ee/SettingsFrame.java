package com.github.sweintritt.ee;

import com.github.sweintritt.ee.configuration.Configurator;
import com.github.sweintritt.ee.configuration.Setting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@Slf4j
public class SettingsFrame extends JFrame {

    private final GridLayout gridLayout;

    public SettingsFrame(final List<Configurator<?>> configurators) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                configurators.forEach(Configurator::update);
                // TODO Save settings
            }
        });
        gridLayout = new GridLayout(1, 1);
        setLayout(gridLayout);
        setTitle("Settings");
        setSize(500, 100);
        configurators.forEach(this::add);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public void add(final Configurator<?> configurator) {
        final JPanel section = new JPanel();
        section.setBorder(BorderFactory.createTitledBorder(configurator.getName()));
        final GridLayout sectionLayout = new GridLayout(1, 2);
        section.setLayout(sectionLayout);

        for (final Map.Entry<String, Setting> entry : configurator.getSettings().entrySet()) {
            section.add(new JLabel(entry.getKey()));
            section.add(getInputComponent(entry.getValue()));
            sectionLayout.setRows(sectionLayout.getRows() + 1);
        }

        add(section);
        gridLayout.setRows(gridLayout.getRows() + 1);
    }

    private JComponent getInputComponent(final Setting setting) {
        if (!CollectionUtils.isEmpty(setting.getPossibleValues())) {
            final JComboBox<String> comboBox = new JComboBox<>(setting.getPossibleValues().toArray(new String[]{}));
            comboBox.setSelectedItem(setting.getValue());
            comboBox.addItemListener(e -> setting.setValue((String) comboBox.getSelectedItem()));
            return comboBox;
        } else if (setting.getType().equals(String.class)) {
            final JTextField textField = new JTextField(setting.getValue());
            textField.getDocument().addDocumentListener((ValueChangedListener) e -> setting.setValue(textField.getText()));
            return new JTextField(setting.getValue());
        } else if (setting.getType().equals(Boolean.class)) {
            final JCheckBox checkBox = new JCheckBox(StringUtils.EMPTY, setting.getBooleanValue());
            checkBox.addChangeListener(e -> setting.setValue(String.valueOf(checkBox.isSelected())));
            return checkBox;
        } else {
            log.error("Unknown type for value {}", setting.getValue());
            return null;
        }
    }
}