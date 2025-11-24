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
import com.lllmtb.events.graphicsUi.ScreenNewOrLoadEvent;
import com.lllmtb.terrain.generation.LandScape;

public class SettingsStartPanel extends JPanel {
    public EventBus eventBus;
    SettingMapPanel mapSettingPanel;
    SettingGamePanel gameSettingsPanel;

    public SettingsStartPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        panelSetting();
        

    }

    public void panelSetting() {

        this.setLayout(new BorderLayout(10, 10)); // zone nord,Sud,...

        // titre (image plus tard);Zone nord
        JLabel titre = new JLabel("CREER UNE NOUVELLE PARTIE");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24)); // bold pour gras
        this.add(titre, BorderLayout.NORTH);

        // bouton lancée et retour: zone sud
        JPanel zoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // met les bouton a droite (flowLayout afficheles un apres les autre);
        JButton backButton = new JButton("Retour");
        JButton starButton = new JButton("Lancer la partie");

        zoneSud.add(backButton);
        zoneSud.add(starButton);
        this.add(zoneSud, BorderLayout.SOUTH);


        //zone centre (les deux partit (parametre map/partit))
        JPanel panelCentre = new JPanel(new GridLayout(1, 2, 10, 0));// 1 ligne 2 colonne , 10 de marge horizontale
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
    public GameSettings setSettings(SettingGamePanel gameSettingsPanel) {
        int nbEquipes = gameSettingsPanel.getNbEquipes();
        int nbVWorms = gameSettingsPanel.getNbWorms();
        String[] nomsEquipes = gameSettingsPanel.getNomsEquipes();
        boolean wind = gameSettingsPanel.getWind();
        if(this.mapSettingPanel.loadMap){
            String mapFileName = this.mapSettingPanel.panelListeMap.getFichierSelectionne().getName();
            return new GameSettings(nbEquipes, nomsEquipes, nbVWorms, wind, mapFileName, false,0,null);
        }
        else{
            LandScape terrainType = this.mapSettingPanel.generateMapPanel.getReliefType();
            int widthMap = this.mapSettingPanel.generateMapPanel.getWidthMapSlider();
            return new GameSettings(nbEquipes, nomsEquipes, nbVWorms, wind, null, false,widthMap,terrainType);
        }
        
        
    }
}
