package de.gozilalp.socket.gui.components;

import javax.swing.*;
import java.net.URL;

/**
 * This class defines the {@link JDialog} for the program.
 * It only sets some basic settings like the icon.
 *
 * @author grumanda
 */
public class SocketServerDialog extends JDialog {

    protected static final URL ICON_URL = SocketServerJFrame.class.getResource(
            "/socket_server_logo.png");

    public SocketServerDialog() {
        ImageIcon icon = new ImageIcon(ICON_URL);
        setIconImage(icon.getImage());
        setSize(400, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }
}