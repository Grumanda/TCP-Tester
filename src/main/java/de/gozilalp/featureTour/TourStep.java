package de.gozilalp.featureTour;

import javax.swing.*;

public record TourStep(JComponent target, String text, int x, int y, int width, int height) {}
