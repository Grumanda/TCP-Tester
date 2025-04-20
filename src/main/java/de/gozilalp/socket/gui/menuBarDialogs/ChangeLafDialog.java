package de.gozilalp.socket.gui.menuBarDialogs;

import de.gozilalp.Main;
import de.gozilalp.configSetup.ConfigData;
import de.gozilalp.configSetup.DatabaseManager;
import de.gozilalp.configSetup.DefinedLAFs;
import de.gozilalp.socket.gui.components.SocketServerDialog;
import de.gozilalp.socket.gui.components.SocketServerJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
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
    private JLabel previewPicture;
    private List<JRadioButton> radioButtonList;
    private static final URL FLAT_ARC_DARK = ChangeLafDialog.class.getResource(
            "/FLAT_ARC_DARK_IJTHEME.png");
    private static final URL FLAT_ARC_ORANGE = ChangeLafDialog.class.getResource(
            "/FLAT_ARC_ORANGE_IJTHEME.png");
    private static final URL FLAT_ATOM_ONE_LIGHT = ChangeLafDialog.class.getResource(
            "/FLAT_ATOM_ONE_LIGHT_IJTHEME.png");
    private static final URL FLAT_DARK_LAF = ChangeLafDialog.class.getResource(
            "/FLAT_DARK_LAF.png");
    private static final URL FLAT_LIGHT_LAF = ChangeLafDialog.class.getResource(
            "/FLAT_LIGHT_LAF.png");
    private static final URL FLAT_LIGHT_OWL = ChangeLafDialog.class.getResource(
            "/FLAT_LIGHT_OWL_IJTHEME.png");
    private static final URL FLAT_MATERIAL_DESIGN_DARK = ChangeLafDialog.class.getResource(
            "/FLAT_MATERIAL_DESIGN_DARK_IJTHEME.png");
    private static final URL FLAT_MATERIAL_LIGHTER = ChangeLafDialog.class.getResource(
            "/FLAT_MATERIAL_LIGHTER_IJTHEME.png");
    private static final URL FLAT_MONOKAI_PRO = ChangeLafDialog.class.getResource(
            "/FLAT_MONOKAI_PRO_IJTHEME.png");
    private static final URL FLAT_ONE_DARK = ChangeLafDialog.class.getResource(
            "/FLAT_ONE_DARK_IJTHEME.png");

    public ChangeLafDialog(JFrame root) {
        ChangeLafDialog instance = this;
        setTitle("Change Look & Feel");
        setLayout(new BorderLayout());
        setLocationRelativeTo(root);
        setSize(800, 600);

        // Create Radio Button Group
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
        JLabel gabLabel = new JLabel(" ");
        radioButtonPanel.add(gabLabel);
        ButtonGroup buttonGroup = new ButtonGroup();
        radioButtonList = new ArrayList<>();
        List<DefinedLAFs> definedLafsList = Arrays.stream(DefinedLAFs.values()).toList();
        for (DefinedLAFs design : definedLafsList) {
            JRadioButton radioButton = new JRadioButton(design.getCONFIG_VALUE());
            radioButton.addActionListener(this::changePictureAction);
            buttonGroup.add(radioButton);
            radioButtonPanel.add(radioButton);
            radioButtonList.add(radioButton);
            if (design.getCONFIG_VALUE().equals(ConfigData.LAF.getValue())) {
                radioButton.setSelected(true);
            }
        }
        JPanel middleWrapperPanel = new JPanel(new GridBagLayout());
        middleWrapperPanel.add(radioButtonPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            for (JRadioButton radioButton : radioButtonList) {
                if (radioButton.isSelected()) {
                    try {
                        if (ConfigData.isValidLafValue(radioButton.getText())) {
                            DatabaseManager dbManager = new DatabaseManager();
                            dbManager.setLaf(radioButton.getText());
                        } else {
                            // should not happen, but you never know
                            JOptionPane.showMessageDialog(instance, "Invalid LaF!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
//                        ConfigCommander.getInstance().writeNewValue(ConfigData.LAF.getKEY(),
//                                radioButton.getText());
                        dispose();
                        Main.start();
                        for (Window window : JFrame.getWindows()) {
                            SwingUtilities.updateComponentTreeUI(window);
                        }
                    } catch (Exception ex) {
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

        //preview picture
        previewPicture = new JLabel();
        previewPicture.setHorizontalAlignment(SwingConstants.CENTER);
        changePictureAction(null);
        previewPicture.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (JRadioButton radioButton : radioButtonList) {
                    if (radioButton.isSelected()) {
                        ImageIcon image = null;
                        switch (radioButton.getText()) {
                            case "FLAT_ARC_DARK_IJTHEME" ->
                                    image = new ImageIcon(FLAT_ARC_DARK);
                            case "FLAT_ARC_ORANGE_IJTHEME" ->
                                    image = new ImageIcon(FLAT_ARC_ORANGE);
                            case "FLAT_ATOM_ONE_LIGHT_IJTHEME" ->
                                    image = new ImageIcon(FLAT_ATOM_ONE_LIGHT);
                            case "FLAT_DARK_LAF" ->
                                    image = new ImageIcon(FLAT_DARK_LAF);
                            case "FLAT_LIGHT_LAF" ->
                                    image = new ImageIcon(FLAT_LIGHT_LAF);
                            case "FLAT_LIGHT_OWL_IJTHEME" ->
                                    image = new ImageIcon(FLAT_LIGHT_OWL);
                            case "FLAT_MATERIAL_DESIGN_DARK_IJTHEME" ->
                                    image = new ImageIcon(FLAT_MATERIAL_DESIGN_DARK);
                            case "FLAT_MATERIAL_LIGHTER_IJTHEME" ->
                                    image = new ImageIcon(FLAT_MATERIAL_LIGHTER);
                            case "FLAT_MONOKAI_PRO_IJTHEME" ->
                                    image = new ImageIcon(FLAT_MONOKAI_PRO);
                            case "FLAT_ONE_DARK_IJTHEME" ->
                                    image = new ImageIcon(FLAT_ONE_DARK);
                        }
                        JDialog dialog = new JDialog();
                        dialog.setSize(1000, 900);
                        JLabel pictureLabel = new JLabel();
                        pictureLabel.setIcon(image);
                        dialog.add(pictureLabel);
                        dialog.setLocationRelativeTo(instance);
                        dialog.setModal(true);
                        dialog.setVisible(true);
                    }
                }
            }
        });


        // Add panels to dialog
        add(middleWrapperPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(previewPicture, BorderLayout.NORTH);

        setVisible(true);
    }

    private void changePictureAction(ActionEvent event) {
        ImageIcon newImage = null;
        for (JRadioButton radioButton : radioButtonList) {
            if (radioButton.isSelected()) {
                switch (radioButton.getText()) {
                    case "FLAT_ARC_DARK_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_ARC_DARK);
                    case "FLAT_ARC_ORANGE_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_ARC_ORANGE);
                    case "FLAT_ATOM_ONE_LIGHT_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_ATOM_ONE_LIGHT);
                    case "FLAT_DARK_LAF" ->
                            newImage = new ImageIcon(FLAT_DARK_LAF);
                    case "FLAT_LIGHT_LAF" ->
                            newImage = new ImageIcon(FLAT_LIGHT_LAF);
                    case "FLAT_LIGHT_OWL_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_LIGHT_OWL);
                    case "FLAT_MATERIAL_DESIGN_DARK_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_MATERIAL_DESIGN_DARK);
                    case "FLAT_MATERIAL_LIGHTER_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_MATERIAL_LIGHTER);
                    case "FLAT_MONOKAI_PRO_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_MONOKAI_PRO);
                    case "FLAT_ONE_DARK_IJTHEME" ->
                            newImage = new ImageIcon(FLAT_ONE_DARK);
                }
            }
        }
        Image scaledImg = newImage.getImage().getScaledInstance(247, 198,
                Image.SCALE_SMOOTH);
        previewPicture.setIcon(new ImageIcon(scaledImg));
        repaint();
    }
}