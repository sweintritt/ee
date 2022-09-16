package com.github.sweintritt.ee;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface ValueChangedListener extends DocumentListener {
    void update(final DocumentEvent e);

    @Override
    default void insertUpdate(final DocumentEvent e) {
        update(e);
    }
    @Override
    default void removeUpdate(final DocumentEvent e) {
        update(e);
    }
    @Override
    default void changedUpdate(final DocumentEvent e) {
        update(e);
    }
}

