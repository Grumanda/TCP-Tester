package de.gozilalp.socket.gui.tabs;

import de.gozilalp.socket.backend.SocketServerHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This class defines the tab where the user can only send and receive messages.
 * This class is no longer in use because the {@link SendConstantMessagesTab} also has the same functions.
 *
 * @deprecated
 * @author grumanda
 */
public class SendSingleMessageTab extends AbstractMessageTab {

    private static SendSingleMessageTab instance;
    private static boolean serverActivated = false;
    private static JTabbedPane tabbedPaneRoot;
    private JPanel messageDisplayPanel;
    private JPanel userInputPanel;
    private JScrollPane messageAreaScrollPane;
    private JButton startToggleServerButton;
    private JButton sendButton;
    private JTextArea messageArea;
    private JTextField messageField;
    private SocketServerHandler socketServerHandler;

    private SendSingleMessageTab() {
        setLayout(new BorderLayout());

        add(getUserInputPanel(), BorderLayout.NORTH);
        add(getMessageDisplayPanel(), BorderLayout.CENTER);
        add(getStartToggleServerButton(), BorderLayout.SOUTH);
    }

    public static SendSingleMessageTab getInstance(JTabbedPane root) {
        if (instance == null) {
            instance = new SendSingleMessageTab();
        }
        tabbedPaneRoot = root;
        return instance;
    }

    public JPanel getMessageDisplayPanel() {
        if (messageDisplayPanel == null) {
            messageDisplayPanel = new JPanel();
            messageDisplayPanel.setLayout(new BorderLayout());

            messageDisplayPanel.add(getMessageAreaScrollPane(), BorderLayout.CENTER);
        }
        return messageDisplayPanel;
    }

    private JScrollPane getMessageAreaScrollPane() {
        if (messageAreaScrollPane == null) {
            messageAreaScrollPane = new JScrollPane(getMessageArea());
        }
        return messageAreaScrollPane;
    }

    public JButton getStartToggleServerButton() {
        if (startToggleServerButton == null) {
            startToggleServerButton = new JButton("START");
            startToggleServerButton.setBackground(Color.GREEN);
            startToggleServerButton.setForeground(Color.BLACK);
            startToggleServerButton.setFont(new Font("Arial", Font.BOLD, 24));
            startToggleServerButton.addActionListener(this::startToggleServerButtonAction);
        }
        return startToggleServerButton;
    }

    public JPanel getUserInputPanel() {
        if (userInputPanel == null) {
            userInputPanel = new JPanel();
            userInputPanel.setLayout(new BorderLayout());
            messageField = new JTextField();
            sendButton = new JButton("SEND");
            sendButton.addActionListener(this::sendButtonAction);
            sendButton.setEnabled(false);
            InputMap inputMap = sendButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = sendButton.getActionMap();
            inputMap.put(KeyStroke.getKeyStroke("ENTER"), "performAction");
            actionMap.put("performAction", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendButton.doClick();
                }
            });

            userInputPanel.add(messageField, BorderLayout.CENTER);
            userInputPanel.add(sendButton, BorderLayout.EAST);
        }
        return userInputPanel;
    }

    private void startToggleServerButtonAction(ActionEvent event) {
        if (!serverActivated) {
            serverActivated = true;
            startToggleServerButton.setBackground(Color.RED);
            startToggleServerButton.setText("STOP");
            socketServerHandler = SocketServerHandler.getInstance(this);
            messageArea.append("[INFO] SERVER STARTED\n");
            scrollDown();
            sendButton.setEnabled(true);
            tabbedPaneRoot.setEnabled(false);
        } else {
            serverActivated = false;
            startToggleServerButton.setBackground(Color.GREEN);
            startToggleServerButton.setText("START");
            socketServerHandler.stopInstance();
            messageArea.append("[INFO] SERVER STOPPED\n");
            scrollDown();
            sendButton.setEnabled(false);
            tabbedPaneRoot.setEnabled(true);
        }
    }

    private void sendButtonAction(ActionEvent event) {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            socketServerHandler.sendMessage(message);
            messageField.setText("");
        }
    }

    @Override
    public JTextArea getMessageArea() {
        if (messageArea == null) {
            messageArea = new JTextArea();
            messageArea.setEditable(false);
        }
        return messageArea;
    }

    @Override
    public void scrollDown() {
        JScrollBar scrollBar = getMessageAreaScrollPane().getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    public static boolean isServerActivated() {
        return serverActivated;
    }
}