package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.terrain.map.MapLoader;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class LoadGamePanel extends JPanel {

    private PanelListe panelListe;
    private final EventBus eventBus;

    public LoadGamePanel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.DARK_GRAY);


        JLabel titre = new JLabel("LOAD A GAME");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(Color.WHITE);
        titre.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 0, 10, 0));
        this.add(titre, BorderLayout.NORTH);



        this.panelListe = new PanelListe(MapLoader.getListeNomsMaps());
        this.add(panelListe, BorderLayout.CENTER);


        JPanel zoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        zoneSud.setOpaque(false);
        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Back", "Arial", 16, Color.WHITE);
        JButton starButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Start", "Arial", 16, Color.WHITE);



        backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });


        starButton.addActionListener(e -> {
            String dossierSelectionne = panelListe.getSelectedMapName();
            if (dossierSelectionne != null) {
                Logger.logger.debug("Chargement du dossier : " + dossierSelectionne);
                // TODO : mettre la logique une fois loadGame fait

            } else {
                Logger.logger.debug("aucun dossier choçisi");
            }
        });

        zoneSud.add(backButton);
        zoneSud.add(starButton);

        this.add(zoneSud, BorderLayout.SOUTH);
    }
}