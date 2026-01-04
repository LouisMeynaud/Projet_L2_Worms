package com.lllmtb.ui.graphical.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.lllmtb.events.ui.graphical.NotificationEvent;
import com.lllmtb.ui.graphical.Utilitaire.TextureManager;

public class Popup {

    private static class PopupElement {
        String message;
        BufferedImage image;
        long timeStart;

        public PopupElement(String message, BufferedImage image) {
            this.message = message;
            this.image = image;
            this.timeStart = System.currentTimeMillis();
        }
    }

    public static final List<PopupElement> actualPopups = new ArrayList<>();

    private static final int maxTime = 5000;
    private static final int xStart = 20;
    private static final int yStart = 150;

    /**
     *  Add a notification to the list of pop-ups to display.
     * @param event the event that corresponds to the notification
     */
    public static void add(NotificationEvent event) {
        BufferedImage imgTrouvee = TextureManager.NOTIFICATIONS.get(event.getImageName());
        actualPopups.add(new PopupElement(event.getMessage(), imgTrouvee));
    }

    /**
     * draw the Popop on the ui.
     * @param g the brushes
     */
    public static void draw(Graphics2D g) {
        if (actualPopups.isEmpty()) return;

        long tempsActuel = System.currentTimeMillis();
        g.setFont(new Font("Arial", Font.BOLD, 14));

        for (int i = 0; i < actualPopups.size(); i++) {
            PopupElement popupElement = actualPopups.get(i);

            if (tempsActuel - popupElement.timeStart > maxTime) {
                actualPopups.remove(i);
                i--;
                continue;
            }

            int y = yStart + (i * 60);

            int largeurTexte = g.getFontMetrics().stringWidth(popupElement.message);
            int largeurBox = 60 + largeurTexte + 20;

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(xStart, y, largeurBox, 50);


            if (popupElement.image != null) {
                g.drawImage(popupElement.image, xStart + 10, y + 5, 40, 40, null);
            }

            g.setColor(Color.WHITE);
            g.drawString(popupElement.message, xStart + 60, y + 32);
        }
    }
}