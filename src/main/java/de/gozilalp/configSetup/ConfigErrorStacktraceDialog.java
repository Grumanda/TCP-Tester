package de.gozilalp.configSetup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigErrorStacktraceDialog extends JDialog {

    public ConfigErrorStacktraceDialog(String error) {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("Socket Server - Error");

        JTextArea errorText = new JTextArea(error);
        errorText.setEditable(false);
        errorText.setWrapStyleWord(true);
        errorText.setLineWrap(true);
        errorText.setOpaque(false);
        errorText.setBorder(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton resetConfigButton = new JButton("Reset config");
        resetConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ConfigCommander.createNewConfigFile();
                } catch (WrongConfigValueException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(resetConfigButton);

        add(errorText, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
