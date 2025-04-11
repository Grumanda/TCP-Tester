package de.gozilalp.socket.gui.components;

import javax.swing.*;
import java.net.URL;

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
