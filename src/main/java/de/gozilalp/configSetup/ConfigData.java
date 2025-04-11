package de.gozilalp.configSetup;

import java.util.Arrays;
import java.util.List;

public enum ConfigData {

    LAF("LAF", ""),
    PORT("PORT", "");

    private String key;
    private String value;

    ConfigData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean isDataSet() {
        if (LAF.getValue().isEmpty() || PORT.getValue().isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isValidLafValue(String value) {
        List<DefinedLAFs> definedLAFsList = List.of(DefinedLAFs.values());
        for (DefinedLAFs design : definedLAFsList) {
            if (design.getConfigValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPortValue(String value) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue <= 0 || intValue > 65535) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
