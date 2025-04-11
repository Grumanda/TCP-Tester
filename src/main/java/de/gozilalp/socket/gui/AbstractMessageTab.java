package de.gozilalp.socket.gui;

import javax.swing.*;

public abstract class AbstractMessageTab extends JPanel {

    public abstract JTextArea getMessageArea();

    public abstract void scrollDown();
}
