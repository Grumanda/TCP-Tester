package de.gozilalp.socket.gui;

import de.gozilalp.configSetup.DatabaseManager;
import de.gozilalp.featureTour.FeatureTour;
import de.gozilalp.featureTour.TourStep;
import de.gozilalp.socket.gui.components.SocketServerJFrame;
import de.gozilalp.socket.gui.tabs.SendConstantMessagesTab;
import de.gozilalp.socket.gui.tabs.SettingsTab;
import javax.swing.*;
import java.util.List;

/**
 * This class defines the main window of the program.
 *
 * @author grumanda
 */
public class SocketServerWindow extends SocketServerJFrame {

    private static SocketServerWindow instance;

    private SocketServerWindow() {
        // Settings
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed Pane and add tabs
        JTabbedPane tabbedPane = new JTabbedPane();
//        SendSingleMessageTab singleTab = SendSingleMessageTab.getInstance(tabbedPane);
        SendConstantMessagesTab constantTab = SendConstantMessagesTab.getInstance(tabbedPane);
        SettingsTab settingsTab = SettingsTab.getInstance(tabbedPane);
//        tabbedPane.add("Single Messaging", singleTab);
        tabbedPane.add("Constant Messaging", constantTab);
        tabbedPane.add("Settings", settingsTab);

        add(tabbedPane);
        setVisible(true);

        // Show feature tour
        DatabaseManager dbManager = new DatabaseManager();
        if (dbManager.getTour().equals("1")) {
            FeatureTour tour = new FeatureTour(this, List.of(
                    new TourStep(constantTab.getMessageDisplayPanel(), "In this area you " +
                            "can see messages you have sent or received",
                            -650, 50, 75, 0),
                    new TourStep(constantTab.getStartToggleServerButton(),
                            "With this button, you can start or stop the server",
                            -650, -100, 75, 0),
                    new TourStep(constantTab.getUserInputPanel(), "Here you " +
                            "can type in your message and send the message",
                            -650, 0, 75, 0),
                    new TourStep(constantTab.getScheduleTablePanel(), "Here you " +
                            "can add schedules for constant messaging",
                            -650, 0, 75, 0)
            ));
            tour.start();
        }
    }

    public static SocketServerWindow getInstance() {
        if (instance == null) {
            instance = new SocketServerWindow();
        }
        return instance;
    }
}