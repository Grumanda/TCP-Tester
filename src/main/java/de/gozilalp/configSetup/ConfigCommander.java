package de.gozilalp.configSetup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class sets up the logic for the config file.
 * It reads the data from the config file, creates the config file or changes config values.
 *
 * @author grumanda
 */
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

    /**
     * This method reads the config file, checks the values and writes it to {@link ConfigData}.
     *
     * @throws WrongConfigValueException exception
     */
    private void readConfig() throws WrongConfigValueException {
        try (Scanner scanner = new Scanner(CONFIG_FILE)) {
            String payload = "";
            while (scanner.hasNextLine()) {
                payload = scanner.nextLine();
                // comment ignoring
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
                if (key.equals(ConfigData.LAF.getKEY())) {
                    if (!ConfigData.isValidLafValue(value)) {
                        scanner.close();
                        throw new WrongConfigValueException(key, value);
                    }
                    ConfigData.LAF.setValue(value);
                }
                if (key.equals(ConfigData.PORT.getKEY())) {
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
        }
    }

    /**
     * This method creates a new config file (and the directory if it does not exist).
     *
     * @throws WrongConfigValueException exception
     */
    public static void createNewConfigFile() throws WrongConfigValueException {
        // Get directory and check if it exists
        File parentDir = CONFIG_FILE.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            // Copy backup file into directory
            Files.copy(BACKUP_CONFIG.toPath(), CONFIG_FILE.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getInstance().readConfig();
    }

    /**
     * This method writes new values into the config file.
     *
     * @param key Key of the config
     * @param value Value for this key
     * @throws WrongConfigValueException exception
     */
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