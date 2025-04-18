package de.gozilalp.socket.gui;

import de.gozilalp.configSetup.DatabaseManager;
import de.gozilalp.featureTour.FeatureTour;
import de.gozilalp.featureTour.TourStep;
import de.gozilalp.socket.gui.components.SocketServerJFrame;
import de.gozilalp.socket.gui.menuBarDialogs.AboutDialog;
import de.gozilalp.socket.gui.menuBarDialogs.ChangeLafDialog;
import de.gozilalp.socket.gui.menuBarDialogs.ConfigurePortDialog;
import de.gozilalp.socket.gui.tabs.SendConstantMessagesTab;
import de.gozilalp.socket.gui.tabs.SendSingleMessageTab;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This class defines the main window of the program.
 *
 * @author grumanda
 */
public class SocketServerWindow extends SocketServerJFrame {

    private static SocketServerWindow instance;
    private JMenuBar menuBar;
    private JMenu settingsMenu;
    private JMenu helpMenu;

    private SocketServerWindow() {
        // Settings
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addMenuBar();

        // Create tabbed Pane and add tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        SendSingleMessageTab singleTab = SendSingleMessageTab.getInstance(tabbedPane);
        SendConstantMessagesTab constantTab = SendConstantMessagesTab.getInstance(tabbedPane);
        tabbedPane.add("Single Messaging", singleTab);
        tabbedPane.add("Constant Messaging", constantTab);

        add(tabbedPane);
        setVisible(true);

        // Show feature tour
        DatabaseManager dbManager = new DatabaseManager();
        if (dbManager.getTour().equals("1")) {
            FeatureTour tour = new FeatureTour(this, List.of(
                    new TourStep(settingsMenu, "Here you can edit the 'Look & Feel'" +
                            " and change the port for your socket server",
                            0, 20, 75, 0),
                    new TourStep(helpMenu, "You can look about some information" +
                            " about the software and report a bug here",
                            0, 50, 100, 0),
                    new TourStep(singleTab.getMessageDisplayPanel(), "In this area you " +
                            "can see messages you have sent or received",
                            -650, 50, 75, 0),
                    new TourStep(singleTab.getStartToggleServerButton(),
                            "With this button, you can start or stop the server",
                            -650, -100, 75, 0),
                    new TourStep(singleTab.getUserInputPanel(), "Here you " +
                            "can type in your message and send the message",
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

    private void addMenuBar() {
        menuBar = new JMenuBar();

        // Settings Menu
        settingsMenu = new JMenu("Settings");
        JMenuItem changeLafItem = new JMenuItem("Change Look & Feel");
        changeLafItem.addActionListener(e -> ChangeLafDialog.getInstance(getInstance()));

        JMenuItem configurePortItem = new JMenuItem("Configure Port");
        configurePortItem.addActionListener(e -> {
            if (SendSingleMessageTab.isServerActivated()
                    || SendConstantMessagesTab.isServerActivated()) {
                JOptionPane.showMessageDialog(getInstance(),
                        "Please stop the server before you change the port!",
                        "INFO", JOptionPane.INFORMATION_MESSAGE);
            } else {
                ConfigurePortDialog.getInstance(getInstance());
            }
        });
        settingsMenu.add(changeLafItem);
        settingsMenu.add(configurePortItem);
        menuBar.add(settingsMenu);

        // Help Menu
        helpMenu = new JMenu("Help");
        JMenuItem reportBugItem = new JMenuItem("Report Bug");
        reportBugItem.addActionListener(e -> {
            try {
                URI uri = new URI("https://github.com/Grumanda/TCP-Tester/issues");
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        helpMenu.add(reportBugItem);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> AboutDialog.getInstance(getInstance()));
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
}