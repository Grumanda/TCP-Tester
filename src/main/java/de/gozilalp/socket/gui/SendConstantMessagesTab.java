package de.gozilalp.socket.gui;

import javax.swing.*;

public class SendConstantMessagesTab extends AbstractMessageTab {

    private static boolean serverActivated = false;

    public SendConstantMessagesTab(JTabbedPane tabbedPane) {

    }

    @Override
    public JTextArea getMessageArea() {
        return null;
    }

    @Override
    public void scrollDown() {
        //TODO
    }

    public static boolean isServerActivated() {
        return serverActivated;
    }
}
