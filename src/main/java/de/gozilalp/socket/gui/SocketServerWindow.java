package de.gozilalp.socket.gui;

import de.gozilalp.socket.gui.components.SocketServerDialog;
import de.gozilalp.socket.gui.components.SocketServerJFrame;
import de.gozilalp.socket.gui.menuBarDialogs.AboutDialog;
import de.gozilalp.socket.gui.menuBarDialogs.ChangeLafDialog;
import de.gozilalp.socket.gui.menuBarDialogs.ConfigurePortDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        SendConstantMessagesTab constantTab = new SendConstantMessagesTab(tabbedPane);
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
        changeLafItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeLafDialog.getInstance(getInstance());
            }
        });

        JMenuItem configurePortItem = new JMenuItem("Configure Port");
        configurePortItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SendSingleMessageTab.isServerActivated()
                        || SendConstantMessagesTab.isServerActivated()) {
                    JOptionPane.showMessageDialog(getInstance(),
                            "Please stop the server before you change the port!",
                            "INFO", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    ConfigurePortDialog.getInstance(getInstance());
                }
            }
        });
        settingsMenu.add(changeLafItem);
        settingsMenu.add(configurePortItem);
        menuBar.add(settingsMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem reportBugItem = new JMenuItem("Report Bug");
        reportBugItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URI uri = new URI("https://github.com/Grumanda/TCP-Tester/issues");
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.browse(uri);
                    }
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        helpMenu.add(reportBugItem);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog.getInstance(getInstance());
            }
        });
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
}
