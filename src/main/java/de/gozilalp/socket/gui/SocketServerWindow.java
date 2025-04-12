package de.gozilalp.socket.gui;

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

        addMenuBar();

        // Create tabbed Pane and add tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        SendSingleMessageTab singleTab = SendSingleMessageTab.getInstance(tabbedPane);
        SendConstantMessagesTab constantTab = SendConstantMessagesTab.getInstance(tabbedPane);
        tabbedPane.add("Single Messaging", singleTab);
        tabbedPane.add("Constant Messaging", constantTab);

        add(tabbedPane);
        setVisible(true);
    }

    public static SocketServerWindow getInstance() {
        if (instance == null) {
            instance = new SocketServerWindow();
        }
        return instance;
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Settings Menu
        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem changeLafItem = new JMenuItem("Change Look & Feel");
        changeLafItem.addActionListener(_ -> ChangeLafDialog.getInstance(getInstance()));

        JMenuItem configurePortItem = new JMenuItem("Configure Port");
        configurePortItem.addActionListener(_ -> {
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
        JMenu helpMenu = new JMenu("Help");
        JMenuItem reportBugItem = new JMenuItem("Report Bug");
        reportBugItem.addActionListener(_ -> {
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
        aboutItem.addActionListener(_ -> AboutDialog.getInstance(getInstance()));
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
}