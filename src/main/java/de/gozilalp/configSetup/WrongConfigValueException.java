package de.gozilalp.configSetup;

public class WrongConfigValueException extends Exception {

    private String key;
    private String value;

    public WrongConfigValueException(String key, String value) {
        super("A not valid value have been set for key '" + key + "'! Value: " + value);
        this.key = key;
        this.value = value;
    }

    public WrongConfigValueException() {
        super("Could not set values for all keys!");
        this.key = null;
        this.value = null;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
