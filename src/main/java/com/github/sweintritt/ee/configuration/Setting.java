package com.github.sweintritt.ee.configuration;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class Setting {

    public enum Type {
        STRING, BOOLEAN
    }

    @NonNull
    private String name;
    @NonNull
    private Type type;
    private String value;
    @NonNull
    private String defaultValue;
    private List<String> possibleValues;

    public String getValue() {
        final String v = Optional.ofNullable(value).orElse(defaultValue);
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(getValue());
    }
}
