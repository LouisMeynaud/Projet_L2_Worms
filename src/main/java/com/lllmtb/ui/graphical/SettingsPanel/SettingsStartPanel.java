package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.terrain.generation.LandScape;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class SettingsStartPanel extends JPanel {
    public EventBus eventBus;
    SettingMapPanel mapSettingPanel;
    SettingGamePanel gameSettingsPanel;

    public SettingsStartPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        panelSetting();


    }

    public void panelSetting() {

        this.setLayout(new BorderLayout(10, 10)); // North zone, South,...

        // Title (image later); Northern Zone
        JLabel titre = new JLabel("START A NEW GAME");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // Launch and return button: southern zone

        JPanel zoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // met les bouton a droite (flowLayout afficheles un apres les autre);
        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Back", 128, 64, 64, 48);
        JButton starButton = Utilitaire.createImageButton(TextureManager.UI, "Start", 128, 64, 64, 48);


        zoneSud.add(backButton);
        zoneSud.add(starButton);
        this.add(zoneSud, BorderLayout.SOUTH);


        //center zone (two part (setting game/map))
        JPanel panelCentre = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 line, 2 columns, 10 horizontal margin
        this.mapSettingPanel = new SettingMapPanel(eventBus);
        this.gameSettingsPanel = new SettingGamePanel();

        panelCentre.add(mapSettingPanel);
        panelCentre.add(gameSettingsPanel);
        this.add(panelCentre, BorderLayout.CENTER);


        backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });

        starButton.addActionListener(e -> {
            eventBus.postQueued(new StartGameEvent(setSettings(gameSettingsPanel)));
        });
    }


    private GameSettings setSettings(SettingGamePanel gameSettingsPanel) {
        int nbEquipes = gameSettingsPanel.getNbEquipes();
        int nbVWorms = gameSettingsPanel.getNbWorms();
        String[] nomsEquipes = gameSettingsPanel.getNomsEquipes();
        boolean wind = gameSettingsPanel.getWind();
        boolean friendlyFire = gameSettingsPanel.getFriendlyFire(); //TODO : ajouter le friendlyFire au setting une fois les modif necessaire faite dans team etc
        int turnDuration = gameSettingsPanel.getTurnDuration();
        int maxLife = gameSettingsPanel.getMaxLife();
        int maxJump = gameSettingsPanel.getMaxJumps();
        String gameName = gameSettingsPanel.getGameName();
        if (this.mapSettingPanel.loadMap) {
            String mapFileName = this.mapSettingPanel.panelListeMap.getFichierSelectionne().getName();
            return new GameSettings(gameName, nbEquipes, nomsEquipes, nbVWorms, maxLife, turnDuration, wind, mapFileName, false, 0, null, maxJump);


        } else {
            LandScape terrainType = this.mapSettingPanel.generateMapPanel.getReliefType();
            int widthMap = this.mapSettingPanel.generateMapPanel.getWidthMapSlider();
            boolean flightIsland = this.mapSettingPanel.generateMapPanel.getflightIsland();
            return new GameSettings(gameName, nbEquipes, nomsEquipes, nbVWorms, maxLife, turnDuration, wind, null, flightIsland, widthMap, terrainType, maxJump);


        }


    }
}
