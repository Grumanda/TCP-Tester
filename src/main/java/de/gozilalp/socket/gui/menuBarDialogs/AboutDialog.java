package de.gozilalp.socket.gui.menuBarDialogs;

import de.gozilalp.socket.gui.components.SocketServerDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class defines the AboutDialog which is shown by clicking on 'About' in the MenuBar.
 *
 * @author grumanda
 */
public class AboutDialog extends SocketServerDialog {

    private static AboutDialog instance;

    private AboutDialog() {
        setTitle("About");
        setSize(700, 600);
        setLayout(new BorderLayout());

        add(getImagePanel(), BorderLayout.NORTH);
        add(getInfoPanel(), BorderLayout.CENTER);
    }

    public static AboutDialog getInstance(JFrame root) {
        if (instance == null) {
            instance = new AboutDialog();
        }
        instance.setVisible(true);
        instance.setLocationRelativeTo(root);
        return instance;
    }

    private JPanel getImagePanel() {
        JPanel imagePanel = new JPanel();
        ImageIcon imageIcon = new ImageIcon(ICON_URL);
        Image scaledImg = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(new ImageIcon(scaledImg));
        imagePanel.add(imgLabel);
        return imagePanel;
    }

    private JPanel getInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        // static Labels
        JLabel programNameLabel = new JLabel("Program Name:");
        JLabel versionLabel = new JLabel("Version:");
        JLabel developerLabel = new JLabel("Developer:");
        JLabel licenseLabel = new JLabel("License:");
        JLabel emptyLabel = new JLabel(" ");
        JLabel libLabel = new JLabel("The program uses the following libraries:");
        // inputLabels
        JLabel programInput = new JLabel(AboutInformation.PROGRAM_NAME);
        JLabel versionInput = new JLabel(AboutInformation.VERSION);
        JLabel developerInput = new JLabel(AboutInformation.DEVELOPER);
        JLabel licenseInput = new JLabel(AboutInformation.LICENSE);
        JLabel flatLafBibInput = new JLabel(AboutInformation.FLATLAF_LINK);
        flatLafBibInput.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        flatLafBibInput.addMouseListener(flatLafOpenBibListener);
        JLabel flatIntelliJThemeBibInput = new JLabel(AboutInformation.FLATLAF_INTELLIJ_LINK);
        flatIntelliJThemeBibInput.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        flatIntelliJThemeBibInput.addMouseListener(flatLafOpenBibListener);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15,5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(programNameLabel, gbc);
        gbc.gridy = 1;
        infoPanel.add(versionLabel, gbc);
        gbc.gridy = 2;
        infoPanel.add(developerLabel, gbc);
        gbc.gridy = 3;
        infoPanel.add(licenseLabel, gbc);
        gbc.gridy = 4;
        infoPanel.add(emptyLabel, gbc);
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        infoPanel.add(libLabel, gbc);
        gbc.gridy = 6;
        infoPanel.add(flatLafBibInput, gbc);
        gbc.gridy = 7;
        infoPanel.add(flatIntelliJThemeBibInput, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.gridx = 1;
        infoPanel.add(programInput, gbc);
        gbc.gridy = 1;
        infoPanel.add(versionInput, gbc);
        gbc.gridy = 2;
        infoPanel.add(developerInput, gbc);
        gbc.gridy = 3;
        infoPanel.add(licenseInput, gbc);

        return infoPanel;
    }

    /**
     * Defines a {@link MouseAdapter} which opens a link to the repo of the library.
     */
    private final MouseAdapter flatLafOpenBibListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                URI uri = new URI(AboutInformation.FLATLAF_URI);
                Desktop.getDesktop().browse(uri);
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    };
}