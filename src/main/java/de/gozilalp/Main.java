package de.gozilalp;

import  com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;
import de.gozilalp.configSetup.*;
import de.gozilalp.socket.gui.SocketServerWindow;
import javax.swing.*;

/**
 * This class defines the main method of the program.
 *
 * @author grumanda
 */
public class Main {

    public static void main(String[] args) {
        start();
    }

    /**
     * This method starts the program in the right order.
     */
    public static void start() {
        try {
            ConfigCommander.getInstance();
            loadLaF();
            SocketServerWindow.getInstance();
        } catch (WrongConfigValueException e) {
            showWrongConfigValueException(e);
        }
    }

    /**
     * This method loads the Look & Feel.
     */
    private static void loadLaF() {
        switch (ConfigData.LAF.getValue()) {
            case "FLAT_LIGHT_LAF" -> FlatLightLaf.setup();
            case "FLAT_ARC_ORANGE_IJTHEME" -> FlatArcOrangeIJTheme.setup();
            case "FLAT_MONOKAI_PRO_IJTHEME" -> FlatMonokaiProIJTheme.setup();
            case "FLAT_DARK_LAF" -> FlatDarkLaf.setup();
            case "FLAT_MATERIAL_DESIGN_DARK_IJTHEME" -> FlatMaterialDesignDarkIJTheme.setup();
            case "FLAT_ARC_DARK_IJTHEME" -> FlatArcDarkIJTheme.setup();
            case "FLAT_ONE_DARK_IJTHEME" -> FlatOneDarkIJTheme.setup();
            case "FLAT_ATOM_ONE_LIGHT_IJTHEME" -> FlatAtomOneLightIJTheme.setup();
            case "FLAT_MATERIAL_LIGHTER_IJTHEME" -> FlatMaterialLighterIJTheme.setup();
            default -> FlatLightLaf.setup();
        }
    }

    /**
     * If a value could not be set, this method is called.
     * It shows an error and gives the user the opportunity to show the {@link ConfigErrorStacktraceDialog}.
     *
     * @param e Exception
     */
    private static void showWrongConfigValueException(WrongConfigValueException e) {
        Object[] options = {"OK", "Show Stacktrace"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "An error occured while reading the config file!",
                "Error",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice == 0 || choice == -1) {
            System.exit(0);
        } else {
            new ConfigErrorStacktraceDialog(e.getLocalizedMessage());
        }
    }
}