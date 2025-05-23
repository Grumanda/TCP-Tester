package de.gozilalp.socket.gui.tabs;

import de.gozilalp.configSetup.DatabaseManager;
import de.gozilalp.socket.backend.Schedule;
import de.gozilalp.socket.backend.SocketServerHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * This class defines the tab where the user can also create schedules.
 *
 * @author grumanda
 */
public class SendConstantMessagesTab extends AbstractMessageTab {

    private static boolean serverActivated = false;
    private static JTabbedPane tabbedPaneRoot;
    private static SendConstantMessagesTab instance;
    private JPanel messageDisplayPanel;
    private JPanel userInputPanel;
    private JPanel messageDisplayAndSettingsPanel;
    private JPanel scheduleTablePanel;
    private JScrollPane messageAreaScrollPane;
    private DefaultTableModel model;
    private JTable table;
    private JButton startToggleServerButton;
    private JButton sendButton;
    private JButton addToTableButton;
    private JButton deleteButton;
    private JTextArea messageArea;
    private JTextField messageField;
    private SocketServerHandler socketServerHandler;

    private SendConstantMessagesTab() {
        setLayout(new BorderLayout());

        add(getUserInputPanel(), BorderLayout.NORTH);
        add(getMessageDisplayAndSettingsPanel(), BorderLayout.CENTER);
        add(getStartToggleServerButton(), BorderLayout.SOUTH);
    }

    private JPanel getMessageDisplayAndSettingsPanel() {
        if (messageDisplayAndSettingsPanel == null) {
            messageDisplayAndSettingsPanel = new JPanel();
            messageDisplayAndSettingsPanel.setLayout(new GridLayout(2, 1));
            messageDisplayAndSettingsPanel.add(getScheduleTablePanel());
            messageDisplayAndSettingsPanel.add(getMessageDisplayPanel());
        }
        return messageDisplayAndSettingsPanel;
    }

    public JPanel getScheduleTablePanel() {
        if (scheduleTablePanel == null) {
            scheduleTablePanel = new JPanel();
            scheduleTablePanel.setLayout(new BorderLayout());
            model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Payload");
            model.addColumn("Interval (s)");

            //database data import
            DatabaseManager dbManager = new DatabaseManager();
            List<Schedule> scheduleList = dbManager.getAllSchedules();
            for (Schedule schedule : scheduleList) {
                model.addRow(new Object[]{schedule.getNAME(), schedule.getPAYLOAD(),
                        schedule.getINTERVAL()});
            }

            table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);

            scheduleTablePanel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2));
            addToTableButton = new JButton("Add");
            addToTableButton.setBackground(Color.GREEN);
            addToTableButton.setEnabled(false);
            addToTableButton.addActionListener(e -> {
                AddScheduleToTableDialog dialog = new AddScheduleToTableDialog();
                dialog.getADD_BUTTON().addActionListener(e1 -> {
                    List<Schedule> allSchedules = dbManager.getAllSchedules();
                    boolean scheduleAlreadyExists = false;
                    for (Schedule schedule : allSchedules) {
                        if (schedule.getNAME().equals(dialog.getNAME_INPUT().getText())) {
                            scheduleAlreadyExists = true;
                            break;
                        }
                    }
                    if (dialog.isValidInput() && !scheduleAlreadyExists) {
                        String name = dialog.getNAME_INPUT().getText();
                        String payload = dialog.getPAYLOAD_INPUT().getText();
                        int interval = Integer.parseInt(dialog.getINTERVAL_INPUT().getText());
                        model.addRow(new Object[]{name, payload, interval});
                        socketServerHandler.startAutoMessage(name, payload, interval);
                        dbManager.addSchedule(name, payload, interval);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid inputs!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.setVisible(true);
            });

            deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED);
            deleteButton.setEnabled(false);
            deleteButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    socketServerHandler.stopAutoMessage(model.getValueAt(selectedRow, 0).toString());
                    dbManager.deleteScheduleByName(model.getValueAt(selectedRow, 0).toString());
                    model.removeRow(selectedRow);
                }
            });

            buttonPanel.add(deleteButton);
            buttonPanel.add(addToTableButton);

            scheduleTablePanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        return scheduleTablePanel;
    }

    public static SendConstantMessagesTab getInstance(JTabbedPane root) {
        if (instance == null) {
            instance = new SendConstantMessagesTab();
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
            for (int row = 0; row < model.getRowCount(); row++) {
                socketServerHandler.startAutoMessage(model.getValueAt(row, 0).toString(),
                        model.getValueAt(row, 1).toString(),
                        Integer.parseInt(model.getValueAt(row, 2).toString()));
            }
            messageArea.append("[INFO] SERVER STARTED\n");
            scrollDown();
            sendButton.setEnabled(true);
            tabbedPaneRoot.setEnabled(false);
            addToTableButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            serverActivated = false;
            startToggleServerButton.setBackground(Color.GREEN);
            startToggleServerButton.setText("START");
            socketServerHandler.stopInstance();
            messageArea.append("[INFO] SERVER STOPPED\n");
            scrollDown();
            sendButton.setEnabled(false);
            tabbedPaneRoot.setEnabled(true);
            addToTableButton.setEnabled(false);
            deleteButton.setEnabled(false);
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