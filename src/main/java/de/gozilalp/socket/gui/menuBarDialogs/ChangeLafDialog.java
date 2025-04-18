package de.gozilalp.socket.gui.menuBarDialogs;

import de.gozilalp.Main;
import de.gozilalp.configSetup.ConfigCommander;
import de.gozilalp.configSetup.ConfigData;
import de.gozilalp.configSetup.DefinedLAFs;
import de.gozilalp.configSetup.WrongConfigValueException;
import de.gozilalp.socket.gui.components.SocketServerDialog;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class defines the ChangeLafDialog which is shown by clicking on
 * 'Change Look & Feel' in the MenuBar.
 *
 * @author grumanda
 */
public class ChangeLafDialog extends SocketServerDialog {

    private static ChangeLafDialog instance;

    private ChangeLafDialog() {
        setTitle("Change Look & Feel");
        setLayout(new BorderLayout());
        setSize(400, 300);

        // Create Radio Button Group
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
        ButtonGroup buttonGroup = new ButtonGroup();
        List<JRadioButton> radioButtonList = new ArrayList<>();
        List<DefinedLAFs> definedLafsList = Arrays.stream(DefinedLAFs.values()).toList();
        for (DefinedLAFs design : definedLafsList) {
            JRadioButton radioButton = new JRadioButton(design.getCONFIG_VALUE());
            buttonGroup.add(radioButton);
            radioButtonPanel.add(radioButton);
            radioButtonList.add(radioButton);
            if (design.getCONFIG_VALUE().equals(ConfigData.LAF.getValue())) {
                radioButton.setSelected(true);
            }
        }

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            for (JRadioButton radioButton : radioButtonList) {
                if (radioButton.isSelected()) {
                    try {
                        ConfigCommander.getInstance().writeNewValue(ConfigData.LAF.getKEY(),
                                radioButton.getText());
                        dispose();
                        Main.start();
                        for (Window window : JFrame.getWindows()) {
                            SwingUtilities.updateComponentTreeUI(window);
                        }
                    } catch (WrongConfigValueException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        getRootPane().setDefaultButton(applyButton);

        // Add panels to dialog
        add(radioButtonPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static ChangeLafDialog getInstance(JFrame root) {
        if (instance == null) {
            instance = new ChangeLafDialog();
        }
        instance.setLocationRelativeTo(root);
        instance.setVisible(true);
        return instance;
    }
}