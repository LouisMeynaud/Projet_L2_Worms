package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class SettingsStartPanel extends JPanel {
    public EventBus eventBus;
    SettingMapPanel mapSettingPanel;
    SettingGamePanel gameSettingsPanel;

    /**
     * This panel contains both the map settings and game settings panels,
     * along with buttons to start the game or go back.
     * @param eventBus the event bus of the game
     */
    public SettingsStartPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        panelSetting();


    }

    /**
     * This panel contains both the map settings and game settings panels,
     * along with buttons to start the game or go back.
     */
    public void panelSetting() {

        this.setLayout(new BorderLayout(10, 10)); // North zone, South,...

        // Title (image later); Northern Zone
        JLabel titre = new JLabel("START A NEW GAME");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // Launch and return button: southern zone

        JPanel zoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // met les bouton a droite (flowLayout afficheles un apres les autre);
        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Back", "Arial", 16, Color.WHITE);
        JButton startButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Start", "Arial", 16, Color.WHITE);


        zoneSud.add(backButton);
        zoneSud.add(startButton);
        this.add(zoneSud, BorderLayout.SOUTH);


        //center zone (two part (setting game/map))
        JPanel panelCentre = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 line, 2 columns, 10 horizontal margin
        this.mapSettingPanel = new SettingMapPanel(eventBus);
        this.gameSettingsPanel = new SettingGamePanel();

        JScrollPane mapScrollPanel = new JScrollPane(this.mapSettingPanel);
        mapScrollPanel.setBorder(null);
        mapScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        JScrollPane gameScrollPanel = new JScrollPane(this.gameSettingsPanel);
        gameScrollPanel.setBorder(null);
        gameScrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        panelCentre.add(mapScrollPanel);
        panelCentre.add(gameScrollPanel);
        this.add(panelCentre, BorderLayout.CENTER);


        backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });

        startButton.addActionListener(e -> {
            eventBus.postQueued(new StartGameEvent(setSettings(gameSettingsPanel)));
        });
    }

    /**
     * Sets the game settings based on user input from the game settings panel.
     * @param gameSettingsPanel The panel containing game settings input fields.
     * @return A GameSettings object populated with the user's selections.
     */
    private GameSettings setSettings(SettingGamePanel gameSettingsPanel) {
        int nbEquipes = gameSettingsPanel.getNbEquipes();
        int nbVWorms = gameSettingsPanel.getNbWorms();
        String[] nomsEquipes = gameSettingsPanel.getNomsEquipes();
        boolean wind = gameSettingsPanel.getWind();
        boolean friendlyFire = gameSettingsPanel.getFriendlyFire();
        int turnDuration = gameSettingsPanel.getTurnDuration();
        int maxLife = gameSettingsPanel.getMaxLife();
        int maxJump = gameSettingsPanel.getMaxJumps();
        String gameName = gameSettingsPanel.getGameName();
        GameMode gameMode = gameSettingsPanel.getGameMode();
        boolean coinCheat = gameSettingsPanel.getCoinCheat();
        if (this.mapSettingPanel.loadMap) {
            String mapFileName = this.mapSettingPanel.panelListeMap.getSelectedMapName();
            return new GameSettings(gameName, nbEquipes, nomsEquipes, nbVWorms, maxLife, turnDuration, wind, mapFileName,
                    false, 0, null, maxJump, gameMode, friendlyFire, coinCheat);


        } else {
            LandScape terrainType = this.mapSettingPanel.generateMapPanel.getReliefType();
            int widthMap = this.mapSettingPanel.generateMapPanel.getWidthMapSlider();
            boolean flightIsland = this.mapSettingPanel.generateMapPanel.getflightIsland();
            return new GameSettings(gameName, nbEquipes, nomsEquipes, nbVWorms, maxLife, turnDuration, wind, null,
                    flightIsland, widthMap, terrainType, maxJump, gameMode, friendlyFire, coinCheat);


        }


    }
}
