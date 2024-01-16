package jagger_harris;

import jagger_harris.config.Config;
import jagger_harris.config.ConfigManager;

import java.io.File;

public class GsonConfigWithCustomDeserializer {
    public static final String CONFIG_NAME = "my_config";
    public static final String CONFIG_FILE_PATH = System.getProperty("user.dir") + File.separator + "config" + File.separator + CONFIG_NAME + ".json";
    public static final Config CONFIG = ConfigManager.loadConfig(CONFIG_FILE_PATH);

    public static void main(String[] args) {
        System.out.println("Config value: " + CONFIG.getMyConfigOption());
        System.out.println(CONFIG_FILE_PATH);
    }
}
