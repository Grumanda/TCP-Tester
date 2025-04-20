package de.gozilalp.socket.gui.tabs;

import de.gozilalp.socket.gui.SocketServerWindow;
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

public class SettingsTab extends JPanel {

    private static SettingsTab instance;
    private static JTabbedPane tabbedPane;
    private JPanel buttonPanel;
    private JButton changeLafButton;
    private JButton changePortButton;
    private JButton reportButton;
    private JButton aboutButton;

    private SettingsTab() {
        setLayout(new BorderLayout());
        add(getButtonPanel(), BorderLayout.CENTER);
    }

    public static SettingsTab getInstance(JTabbedPane root) {
        if (instance == null) {
            instance = new SettingsTab();
        }
        tabbedPane = root;
        return instance;
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
            buttonPanel.setLayout(new GridLayout(4, 1, 0, 5));
            changeLafButton = new JButton("Change 'Look & Feel'");
            changeLafButton.setFont(new Font(getFont().getFontName(), Font.PLAIN, 30));
            changeLafButton.addActionListener(e ->
                    new ChangeLafDialog(SocketServerWindow.getInstance()));
            changePortButton = new JButton("Configure port");
            changePortButton.setFont(new Font(getFont().getFontName(), Font.PLAIN, 30));
            changePortButton.addActionListener(e ->
                    ConfigurePortDialog.getInstance(SocketServerWindow.getInstance()));
            reportButton = new JButton("Report a bug");
            reportButton.setFont(new Font(getFont().getFontName(), Font.PLAIN, 30));
            reportButton.addActionListener(e -> {
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
            aboutButton = new JButton("About");
            aboutButton.setFont(new Font(getFont().getFontName(), Font.PLAIN, 30));
            aboutButton.addActionListener(e -> AboutDialog.getInstance(
                    SocketServerWindow.getInstance()));

            buttonPanel.add(changeLafButton);
            buttonPanel.add(changePortButton);
            buttonPanel.add(reportButton);
            buttonPanel.add(aboutButton);
        }
        return buttonPanel;
    }
}
