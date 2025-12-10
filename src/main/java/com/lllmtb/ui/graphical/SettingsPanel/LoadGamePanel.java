package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class LoadGamePanel extends JPanel {

    EventBus eventBus;

    /**
     * Constructor of a panel that allows to choose between creating a new game or loading an existing one.
     * @param eventBus
     */
    public LoadGamePanel(EventBus eventBus) {
        this.eventBus = new EventBus();
        this.setLayout(new BorderLayout(10, 10)); // North zone, South,...


        JLabel titre = new JLabel("LOAD A GAME");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);


        JPanel zoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // met les bouton a droite (flowLayout afficheles un apres les autre);
        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Back", 128, 64, 64, 48,"Back","Arial",16);
        JButton starButton = Utilitaire.createImageButton(TextureManager.UI, "Start", 128, 64, 64, 48,"Start","Arial",16);


        zoneSud.add(backButton);
        zoneSud.add(starButton);
        this.add(zoneSud, BorderLayout.SOUTH);

        starButton.addActionListener(e -> {
            //
        });

        backButton.addActionListener(e -> {

            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });
    }


}
