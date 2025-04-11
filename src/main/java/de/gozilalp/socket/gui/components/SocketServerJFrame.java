package de.gozilalp.socket.gui.components;

import javax.swing.*;
import java.net.URL;

public class SocketServerJFrame extends JFrame {

    private static final URL ICON_URL = SocketServerJFrame.class.getResource(
            "/socket_server_logo.png");

    public SocketServerJFrame() {
        setTitle("Socket Server");
        ImageIcon icon = new ImageIcon(ICON_URL);
        setIconImage(icon.getImage());
    }
}
