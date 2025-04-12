package de.gozilalp.configSetup;

import java.util.List;

/**
 * This enum contains all config keys with their value.
 *
 * @author grumanda
 */
public enum ConfigData {

    LAF("LAF", ""),
    PORT("PORT", "");

    private final String KEY;
    private String value;

    ConfigData(String key, String value) {
        this.KEY = key;
        this.value = value;
    }

    /**
     * Checks if all data have been set.
     *
     * @return boolean
     */
    public static boolean isDataSet() {
        return !LAF.getValue().isEmpty() && !PORT.getValue().isEmpty();
    }

    /**
     * Checks if the given value exists in {@link DefinedLAFs}.
     *
     * @param value Value to check
     * @return boolean
     */
    public static boolean isValidLafValue(String value) {
        List<DefinedLAFs> definedLAFsList = List.of(DefinedLAFs.values());
        for (DefinedLAFs design : definedLAFsList) {
            if (design.getCONFIG_VALUE().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given value is a valid port.
     * @param value Value to check
     * @return boolean
     */
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

    public String getKEY() {
        return KEY;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}