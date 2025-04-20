package de.gozilalp.featureTour;

import de.gozilalp.configSetup.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * This class is what we call "Pfusch" in germany.
 * It is a component to show the user a short feature tour.
 * It defines how the feature tour looks like.
 *
 * @author grumanda
 */
public class FeatureTour {

    private final JFrame frame;
    private final List<TourStep> steps;
    private int currentStep = 0;
    private final JPanel glassPane = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentStep >= steps.size()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRect(0, 0, getWidth(), getHeight());

            JComponent target = steps.get(currentStep).target();
            Rectangle bounds = SwingUtilities.convertRectangle(target.getParent(),
                    target.getBounds(), this);

            g2.setComposite(AlphaComposite.Clear);
            g2.fillRoundRect(bounds.x - 10, bounds.y - 10, bounds.width + 20,
                    bounds.height + 20, 20, 20);
            g2.dispose();
        }
    };

    public FeatureTour(JFrame frame, List<TourStep> steps) {
        this.frame = frame;
        this.steps = steps;

        glassPane.setOpaque(false);
        glassPane.setLayout(null);
    }

    public void start() {
        frame.setGlassPane(glassPane);
        glassPane.setVisible(true);
        showStep(currentStep);
    }

    private void showStep(int index) {
        glassPane.removeAll();
        if (index >= steps.size()) {
            glassPane.setVisible(false);
            DatabaseManager dbManager = new DatabaseManager();
            dbManager.setTour("2");
            return;
        }

        TourStep step = steps.get(index);
        JComponent target = step.target();

        Rectangle bounds = SwingUtilities.convertRectangle(target.getParent(),
                target.getBounds(), glassPane);

        JLabel label = new JLabel("<html><div style='text-align: center; width:200px;'>" + step.text()
                + "</div></html>", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        JButton next = new JButton(index == steps.size() - 1 ? "Finish" : "Continue");
        next.addActionListener((ActionEvent e) -> showStep(++currentStep));

        JPanel popup = new JPanel();
        popup.setLayout(new BorderLayout());
        popup.setBackground(new Color(30, 30, 30));
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        popup.add(label, BorderLayout.CENTER);
        popup.add(next, BorderLayout.SOUTH);

        popup.setSize(220 + step.width(), 100 + step.height());
        popup.setLocation(bounds.x + step.x() + bounds.width + 10, bounds.y + step.y());
        glassPane.add(popup);
        popup.revalidate();
        popup.repaint();
        glassPane.revalidate();
        glassPane.repaint();
    }
}