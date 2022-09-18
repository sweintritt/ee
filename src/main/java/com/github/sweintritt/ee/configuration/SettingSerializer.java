package com.github.sweintritt.ee.configuration;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SettingSerializer implements JsonSerializer<Setting>, JsonDeserializer<Setting> {

    public JsonElement serialize(final Setting setting, final Type type, final JsonSerializationContext context) {
        return new JsonPrimitive(setting.getValue());
    }

    public Setting deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) {
        return Setting.builder()
                .value(json.getAsString())
                .build();
    }
}
