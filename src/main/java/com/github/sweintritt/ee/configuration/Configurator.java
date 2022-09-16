package com.github.sweintritt.ee.configuration;

import lombok.Getter;

import java.util.Map;

public abstract class Configurator<T> {
    @Getter
    protected final String name;
    protected final T object;
    @Getter
    protected Map<String, Setting> settings;

    protected Configurator(final String name, final T object, final Map<String, Setting> settings) {
        this.name = name;
        this.object = object;
        this.settings = settings;
    }

    public abstract void update();

}
