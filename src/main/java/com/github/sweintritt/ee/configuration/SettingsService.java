package com.github.sweintritt.ee.configuration;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SettingsService {

    private static SettingsService instance;

    private final File configFile;

    private final Gson gson;

    private SettingsService() {
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();
        configFile = new File(System.getProperty("user.home") + File.separator + ".ee.json");
        log.debug("config file: {}{}{}", configFile.getAbsolutePath(), File.separator, configFile.getName());
    }

    public void load(final List<Configurator<?>> configurators) throws IOException {
        if (configFile.exists()) {
            final String json = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
            final Map<String, Map<String, String>> settings = gson.fromJson(json, Map.class);
            for (final Configurator<?> configurator : configurators) {
                if (settings.containsKey(configurator.getName())) {
                    for (final Map.Entry<String, String> entry : settings.get(configurator.getName()).entrySet()) {
                        configurator.getSettings().get(entry.getKey()).setValue(entry.getValue());
                    }
                }
            }
        }
    }

    public void save(final List<Configurator<?>> configurators) throws IOException {
        final Map<String, Map<String, String>> settings = new HashMap<>();

        for (final Configurator<?> configurator : configurators) {
            for (final Setting setting : configurator.getSettings().values()) {
                if (setting.getValue() != null && !setting.getValue().equals(setting.getDefaultValue())) {
                    // TODO Use a sorted map to show the settings in a fixed order in the settings dialog
                    settings.computeIfAbsent(configurator.getName(), c -> new HashMap<>()).put(setting.getName(), setting.getValue());
                }
            }
        }

        save(settings);
    }

    private void save(final Map<String, Map<String, String>> settings) throws IOException {
        if (!configFile.exists() && !configFile.createNewFile()) {
            throw new IOException("Unable to create file " + configFile);
        }

        FileUtils.writeStringToFile(configFile, gson.toJson(settings), StandardCharsets.UTF_8);
    }

    public static SettingsService getInstance() {
        if (instance == null) {
            instance = new SettingsService();
        }

        return instance;
    }
}
