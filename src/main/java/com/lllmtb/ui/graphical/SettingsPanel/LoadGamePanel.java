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
    private final EventBus systemBus;

    /**
     * Constructor for LoadGamePanel.
     * @param systemBus The SystemBus for posting events.
     */
    public LoadGamePanel(EventBus systemBus) {
        this.systemBus = systemBus;
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.DARK_GRAY);


        JLabel titre = new JLabel("CHARGER UNE PARTIE");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(Color.WHITE);
        titre.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 0, 10, 0));
        this.add(titre, BorderLayout.NORTH);



        this.panelListe = new PanelListe(Load.getListeNomsLoads(), SaveConfig.SAVE_DIRECTORY, true);
        this.add(panelListe, BorderLayout.CENTER);


        JPanel zoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        zoneSud.setOpaque(false);
        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Retour", "Arial", 16, Color.WHITE);
        JButton deleteButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Supprimer", "Arial", 16, Color.WHITE);
        JButton starButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Commencer", "Arial", 16, Color.WHITE);



        backButton.addActionListener(e -> {
            this.systemBus.postQueued(new ScreenNewOrLoadEvent());
        });

        deleteButton.addActionListener(e -> {
            String dossierSelectionne = panelListe.getSelectedMapName();
            if (dossierSelectionne != null) {
                systemBus.postQueued(new DeleteSaveEvent(dossierSelectionne));
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
                systemBus.postQueued(new LoadEvent(dossierSelectionne));

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