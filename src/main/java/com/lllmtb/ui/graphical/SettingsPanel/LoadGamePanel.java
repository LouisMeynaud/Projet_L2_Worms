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
import com.lllmtb.engine.save.Load;
import com.lllmtb.engine.save.SaveConfig;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.DeleteSaveEvent;
import com.lllmtb.events.game.LoadEvent;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.ui.graphical.Utilitaire.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire.Utilitaire;

public class LoadGamePanel extends JPanel {

    private PanelListe panelListe;
    private final EventBus eventBus;

    /**
     * Constructor for LoadGamePanel.
     * @param eventBus The EventBus for posting events.
     */
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



        this.panelListe = new PanelListe(Load.getListeNomsLoads(), SaveConfig.SAVE_DIRECTORY, true);
        this.add(panelListe, BorderLayout.CENTER);


        JPanel zoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        zoneSud.setOpaque(false);
        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Back", "Arial", 16, Color.WHITE);
        JButton deleteButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Delete", "Arial", 16, Color.WHITE);
        JButton starButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Start", "Arial", 16, Color.WHITE);



        backButton.addActionListener(e -> {
            this.eventBus.postQueued(new ScreenNewOrLoadEvent());
        });

        deleteButton.addActionListener(e -> {
            String dossierSelectionne = panelListe.getSelectedMapName();
            if (dossierSelectionne != null) {
                eventBus.postQueued(new DeleteSaveEvent(dossierSelectionne));
                this.remove(this.panelListe);
                this.panelListe = new PanelListe(Load.getListeNomsLoads(), SaveConfig.SAVE_DIRECTORY, true);
                this.add(this.panelListe, BorderLayout.CENTER);
                this.revalidate();
                this.repaint();
            }
        });


        starButton.addActionListener(e -> {
            String dossierSelectionne = panelListe.getSelectedMapName();
            if (dossierSelectionne != null) {
                Logger.logger.debug("Loading folder : {}", dossierSelectionne);
                eventBus.postQueued(new LoadEvent(dossierSelectionne));

            } else {
                Logger.logger.debug("No save folder selected to load.");
            }
        });

        zoneSud.add(backButton);
        zoneSud.add(deleteButton);
        zoneSud.add(starButton);

        this.add(zoneSud, BorderLayout.SOUTH);
    }
}