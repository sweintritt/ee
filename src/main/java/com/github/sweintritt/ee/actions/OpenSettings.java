package com.github.sweintritt.ee.actions;

import com.github.sweintritt.ee.configuration.Configurator;
import com.github.sweintritt.ee.SettingsFrame;
import lombok.RequiredArgsConstructor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@RequiredArgsConstructor
public class OpenSettings implements ActionListener {

    private final List<Configurator<?>> configurators;

    @Override
    public void actionPerformed(final ActionEvent event) {
        new SettingsFrame(configurators);
    }
}
