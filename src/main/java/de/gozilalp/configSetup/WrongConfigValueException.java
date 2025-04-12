package de.gozilalp.configSetup;

/**
 * This class defines a exception. It is thrown when a value is not valid.
 *
 * @author grumanda
 */
public class WrongConfigValueException extends Exception {

    private final String KEY;
    private final String VALUE;

    public WrongConfigValueException(String key, String value) {
        super("A not valid value have been set for key '" + key + "'! Value: " + value);
        this.KEY = key;
        this.VALUE = value;
    }

    public WrongConfigValueException() {
        super("Could not set values for all keys!");
        this.KEY = null;
        this.VALUE = null;
    }

    public String getKEY() {
        return KEY;
    }

    public String getVALUE() {
        return VALUE;
    }
}