package com.github.sweintritt.ee.configuration;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class Setting {
    @NonNull
    private String name;
    @NonNull
    private Class<?> type;
    private String value;
    @NonNull
    private String defaultValue;
    private List<String> possibleValues;

    public String getValue() {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(getValue());
    }
}
