package de.gozilalp.socket.gui.menuBarDialogs;

import de.gozilalp.Main;
import de.gozilalp.configSetup.ConfigData;
import de.gozilalp.configSetup.DatabaseManager;
import de.gozilalp.socket.gui.components.SocketServerDialog;
import javax.swing.*;
import java.awt.*;

/**
 * This class defines the ConfigurePortDialog which is shown by clicking on
 * 'Configure port' in the MenuBar.
 *
 * @author grumanda
 */
public class ConfigurePortDialog extends SocketServerDialog {

    private static ConfigurePortDialog instance;

    private ConfigurePortDialog() {
        setTitle("Configure Port");
        setLayout(new BorderLayout());
        setSize(400, 300);

        // TextField
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel portLabel = new JLabel("Port:");
        portLabel.setHorizontalAlignment(SwingUtilities.CENTER);
        portLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField textField = new JTextField(ConfigData.PORT.getValue());
        textField.setFont(new Font("Arial", Font.PLAIN, 24));
        textField.setHorizontalAlignment(SwingUtilities.CENTER);

        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(portLabel, gbc);
        gbc.gridy = 1;
        inputPanel.add(textField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            if (!ConfigData.isValidPortValue(textField.getText())) {
                JOptionPane.showMessageDialog(getInstance(null), "Invalid port value!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
//                ConfigCommander.getInstance().writeNewValue(ConfigData.PORT.getKEY(),
//                        textField.getText());
                if (ConfigData.isValidPortValue(textField.getText())) {
                    DatabaseManager dbManager = new DatabaseManager();
                    dbManager.setPort(textField.getText());
                } else {
                    JOptionPane.showMessageDialog(instance, "Invalid Port!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
                Main.start();
                dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        getRootPane().setDefaultButton(applyButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static ConfigurePortDialog getInstance(JFrame root) {
        if (instance == null) {
            instance = new ConfigurePortDialog();
        }
        instance.setVisible(true);
        instance.setLocationRelativeTo(root);
        return instance;
    }
}