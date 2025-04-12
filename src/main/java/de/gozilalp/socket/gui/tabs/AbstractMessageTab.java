package de.gozilalp.socket.gui.tabs;

import javax.swing.*;

/**
 * This class defines a abstract class which is used in {@link SendSingleMessageTab}
 * and {@link SendConstantMessagesTab}.
 *
 * @author grumanda
 */
public abstract class AbstractMessageTab extends JPanel {

    public abstract JTextArea getMessageArea();

    public abstract void scrollDown();
}