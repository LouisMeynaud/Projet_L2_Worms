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

public class SettingsStartPanel extends JPanel {
    public EventBus eventBus;
    SettingMapPanel mapSettingPanel;
    SettingGamePanel gameSettingsPanel;

    public SettingsStartPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        PanelSetting();
        

    }

    public void PanelSetting() {

        this.setLayout(new BorderLayout(10, 10)); // North zone, South,...

        // Title (image later); Northern Zone
        JLabel titre = new JLabel("START A NEW GAME");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24)); 
        this.add(titre, BorderLayout.NORTH);

        // Launch and return button: southern zone
        JPanel ZoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // met les bouton a droite (flowLayout afficheles un apres les autre);
        JButton backButton = new JButton("Back");
        JButton StarButton = new JButton("Start the game");

        ZoneSud.add(backButton);
        ZoneSud.add(StarButton);
        this.add(ZoneSud, BorderLayout.SOUTH);


        //center zone (two part (setting game/map))
        JPanel PanelCentre = new JPanel(new GridLayout(1, 2, 10, 0));// 1 line, 2 columns, 10 horizontal margin
        this.mapSettingPanel = new SettingMapPanel(eventBus);
        this.gameSettingsPanel = new SettingGamePanel();

        PanelCentre.add(mapSettingPanel);
        PanelCentre.add(gameSettingsPanel);
        this.add(PanelCentre, BorderLayout.CENTER);


        backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });

        StarButton.addActionListener(e -> {
            eventBus.postQueued(new StartGameEvent(setSettings(gameSettingsPanel)));
        });
    }
    /**
     * 
     * @param gameSettingsPanel retrieves the parameters chosen in the gameSetting panel
     * @returnreturns a type setting for creating the game.
     */
    public GameSettings setSettings(SettingGamePanel gameSettingsPanel) {
        int nbEquipes = gameSettingsPanel.getNbEquipes();
        int nbVWorms = gameSettingsPanel.getNbWorms();
        String[] nomsEquipes = gameSettingsPanel.getNomsEquipes();
        boolean wind = gameSettingsPanel.getWind();
        int turnDuration = gameSettingsPanel.getTurnDuration();
        if(this.mapSettingPanel.loadMap){
            String mapFileName = this.mapSettingPanel.panelListeMap.getFichierSelectionne().getName();
            return new GameSettings(nbEquipes, nomsEquipes, nbVWorms, turnDuration, wind, mapFileName, false,0,null);
        }
        else{
            LandScape terrainType = this.mapSettingPanel.generateMapPanel.getReliefType();
            int widthMap = this.mapSettingPanel.generateMapPanel.getWidthMapSlider();
            return new GameSettings(nbEquipes, nomsEquipes, nbVWorms, turnDuration, wind, null, false,widthMap,terrainType);
        }
        
        
    }
}
