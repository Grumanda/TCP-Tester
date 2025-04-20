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

    private static final String DO_TOUR_TEXT = "Do you want to do a quick tour?";

    public static void main(String[] args) {
        start();
    }

    /**
     * This method starts the program in the right order.
     */
    public static void start() {
        DatabaseManager dbManager = new DatabaseManager();
        ConfigData.LAF.setValue(dbManager.getLaf());
        ConfigData.PORT.setValue(dbManager.getPort());
        loadLaF();
        // Check if tour should be shown
        if (dbManager.getTour().equals("0")) {
            int doTour = JOptionPane.showOptionDialog(null, DO_TOUR_TEXT,
                    "Feature Tour", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (doTour == JOptionPane.YES_OPTION) {
                dbManager.setTour("1");
            } else {
                dbManager.setTour("2");
            }
        }
        SocketServerWindow.getInstance();
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
}