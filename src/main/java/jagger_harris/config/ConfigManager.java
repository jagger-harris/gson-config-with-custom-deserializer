package jagger_harris.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigManager {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveConfig(Config config, String filePath) {
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new GsonBuilder()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                gson.toJson(config, writer);
                System.out.println("Configuration saved successfully");
            }
        } catch (IOException e) {
            System.out.println("Unable to write file: " + e.getMessage());
        }
    }

    public static Config loadConfig(String filePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Config.class, new ConfigDeserializer())
                .create();
        Config loadedConfig = new Config();
        boolean shouldCreateNewConfig = false;

        try (FileReader reader = new FileReader(filePath)) {
            try {
                loadedConfig = gson.fromJson(reader, Config.class);
            } catch (JsonParseException e) {
                System.out.println("Invalid config file: \n" + e.getMessage());
                shouldCreateNewConfig = true;
            }
        } catch (IOException e) {
            System.out.println("Could not read config file: \n" + e.getMessage());
            shouldCreateNewConfig = true;
        }

        if (shouldCreateNewConfig) {
            backupConfig(filePath);
            saveConfig(loadedConfig, filePath);
            System.out.println("Config file not found or corrupted, creating a new default config file and backed up current one");
        } else {
            System.out.println("Config loaded successfully");
        }

        return loadedConfig;
    }

    public static void backupConfig(String filePath) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        String backupFilePath = filePath + "." + timestamp + ".backup";

        try {
            Files.copy(Path.of(filePath), Path.of(backupFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Original config file does not exist, making a new one without backup");
        }
    }
}
