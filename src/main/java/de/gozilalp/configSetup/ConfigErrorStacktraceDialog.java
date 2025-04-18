package de.gozilalp.configSetup;

import javax.swing.*;
import java.awt.*;

/**
 * This class defines a dialog. This dialog is shown when a {@link WrongConfigValueException}
 * is caught. It displays the StackTrace and gives the user the opportunity to reset the config file.
 *
 * @author grumanda
 */
public class ConfigErrorStacktraceDialog extends JDialog {

    public ConfigErrorStacktraceDialog(String error) {
        // Settings
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("Socket Server - Error");

        // TextArea where the error is shown
        JTextArea errorText = new JTextArea(error);
        errorText.setEditable(false);
        errorText.setWrapStyleWord(true);
        errorText.setLineWrap(true);
        errorText.setOpaque(false);
        errorText.setBorder(null);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> System.exit(0));
        JButton resetConfigButton = new JButton("Reset config");
        resetConfigButton.addActionListener(e -> {
            try {
                ConfigCommander.createNewConfigFile();
            } catch (WrongConfigValueException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonPanel.add(okButton);
        buttonPanel.add(resetConfigButton);

        add(errorText, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}