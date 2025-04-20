package de.gozilalp.socket.gui.tabs;

import de.gozilalp.socket.gui.components.SocketServerDialog;
import javax.swing.*;
import java.awt.*;

/**
 * This class defines a dialog which is shown when the user wants to add a schedule
 * in {@link SendConstantMessagesTab}.
 *
 * @author grumanda
 */
public class AddScheduleToTableDialog extends SocketServerDialog {

    private final JTextField NAME_INPUT;
    private final JTextField PAYLOAD_INPUT;
    private final JTextField INTERVAL_INPUT;
    private final JButton ADD_BUTTON;

    public AddScheduleToTableDialog() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setTitle("Add Schedule");

        JPanel componentsPanel = new JPanel();
        componentsPanel.setLayout(new GridBagLayout());
        JLabel nameLabel = new JLabel("Name:");
        JLabel payloadLabel = new JLabel("Payload:");
        JLabel intervalLabel = new JLabel("Interval (in s):");
        NAME_INPUT = new JTextField();
        PAYLOAD_INPUT = new JTextField();
        INTERVAL_INPUT = new JTextField();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        componentsPanel.add(nameLabel, gbc);
        gbc.gridy = 1;
        componentsPanel.add(payloadLabel, gbc);
        gbc.gridy = 2;
        componentsPanel.add(intervalLabel, gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        componentsPanel.add(NAME_INPUT, gbc);
        gbc.gridy = 1;
        componentsPanel.add(PAYLOAD_INPUT, gbc);
        gbc.gridy = 2;
        componentsPanel.add(INTERVAL_INPUT, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        ADD_BUTTON = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(ADD_BUTTON);

        add(componentsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTextField getNAME_INPUT() {
        return NAME_INPUT;
    }

    public JTextField getPAYLOAD_INPUT() {
        return PAYLOAD_INPUT;
    }

    public JTextField getINTERVAL_INPUT() {
        return INTERVAL_INPUT;
    }

    public JButton getADD_BUTTON() {
        return ADD_BUTTON;
    }

    /**
     * Checks if the values of the textFields are valid.
     * @return boolean
     */
    public boolean isValidInput() {
        if (!NAME_INPUT.getText().isEmpty() || !PAYLOAD_INPUT.getText().isEmpty()
                || !INTERVAL_INPUT.getText().isEmpty()) {
            try {
                Integer.parseInt(INTERVAL_INPUT.getText());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}