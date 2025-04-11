package de.gozilalp.configSetup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigCommander {

    private static ConfigCommander instance;
    private static final File CONFIG_FILE = new File(System.getenv("LOCALAPPDATA")
            + "\\Socket-Server\\config.txt");
    private static final File BACKUP_CONFIG = new File(ConfigCommander.class.getResource(
            "/config_backup.txt").getFile());

    private ConfigCommander() throws WrongConfigValueException {
        readConfig();
    }

    public static ConfigCommander getInstance() throws WrongConfigValueException {
        if (instance == null) {
            instance = new ConfigCommander();
        }
        return instance;
    }

    private void readConfig() throws WrongConfigValueException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(CONFIG_FILE);
            String payload = "";
            while (scanner.hasNextLine()) {
                // comment ignoring
                payload = scanner.nextLine();
                if (payload.startsWith("//")) {
                    continue;
                }
                if (payload.isEmpty()) {
                    continue;
                }

                // split each line in "key and value"
                String[] parts = payload.split("=");
                String key = parts[0];
                String value = parts[1];

                // write keys or throw exception
                if (key.equals(ConfigData.LAF.getKey())) {
                    if (!ConfigData.isValidLafValue(value)) {
                        scanner.close();
                        throw new WrongConfigValueException(key, value);
                    }
                    ConfigData.LAF.setValue(value);
                }
                if (key.equals(ConfigData.PORT.getKey())) {
                    if (!ConfigData.isValidPortValue(value)) {
                        scanner.close();
                        throw new WrongConfigValueException(key, value);
                    }
                    ConfigData.PORT.setValue(value);
                }
            }
            scanner.close();

            if (!ConfigData.isDataSet()) {
                throw new WrongConfigValueException();
            }
        } catch (FileNotFoundException e) {
            createNewConfigFile();
        } finally {
            scanner.close();
        }
    }

    public static void createNewConfigFile() throws WrongConfigValueException {
        File parentDir = CONFIG_FILE.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            Files.copy(BACKUP_CONFIG.toPath(), CONFIG_FILE.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getInstance().readConfig();
    }

    public void writeNewValue(String key, String value) throws WrongConfigValueException {
        try {
            // Read the config again and save a list with all columns
            Scanner scanner = new Scanner(CONFIG_FILE);
            List<String> columnPayloads = new ArrayList<>();
            while (scanner.hasNextLine()) {
                columnPayloads.add(scanner.nextLine());
            }
            scanner.close();

            // Searching for the right key and overriding the value
            for (int i = 0; i < columnPayloads.size(); i++) {
                String[] parts = columnPayloads.get(i).split("=");
                if (parts[0].startsWith("//")) {
                    continue;
                }
                if (parts[0].equals(key)) {
                    columnPayloads.set(i, key + "=" + value);
                }
            }

            // Rewrite the file
            FileWriter writer = new FileWriter(CONFIG_FILE, false);
            for (String column : columnPayloads) {
                writer.write(column + "\n");
            }
            writer.close();
            getInstance().readConfig();
        } catch (FileNotFoundException e) {
            createNewConfigFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
