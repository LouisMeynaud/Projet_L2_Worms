package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenGenerateMap;
import com.lllmtb.events.ui.graphical.ScreenLoadMap;
import com.lllmtb.terrain.map.MapLoader;

public class SettingMapPanel extends JPanel{
    public CardLayout cardLayout;
    public GenerateMapPanel generateMapPanel;
    public PanelListeMap panelListeMap;
    public EventBus eventBus;
    public boolean loadMap = true;
    
    /**
     * 
     * @param eventBus the event bus of the game
     * This page contains  2 panel and 2 button for choose the panel
     * 
     * 1: panelListeMap for choose a map
     * 2: generateMapPanel for create a map
     * 
     */
    public SettingMapPanel(EventBus eventBus){
        this.eventBus = eventBus;
        this.setBorder(BorderFactory.createTitledBorder(""));
        cardLayout = new CardLayout();
        this.setLayout(new BorderLayout());
        JPanel cardPanel= new JPanel(this.cardLayout);
        this.add(cardPanel);

        JButton loadButton = new JButton("Load Map");
        JButton generateButton = new JButton("Generate Map");

        this.generateMapPanel = new GenerateMapPanel();
        this.panelListeMap = new PanelListeMap(MapLoader.getListeDossiersMaps());
        
        
        
        cardPanel.add(this.panelListeMap,"panelListeMap");
        cardPanel.add(this.generateMapPanel,"generateMapPanel");
        

        eventBus.subscribe(ScreenGenerateMap.class, event -> {
                cardLayout.show(cardPanel, "generateMapPanel");
                System.out.println("event generate recuperer");
            
        });

        eventBus.subscribe(ScreenLoadMap.class, event -> {
                cardLayout.show(cardPanel, "panelListeMap");
                System.out.println("event load recuperer");
            
        });

        JPanel boutonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonPanel.add(loadButton);
        boutonPanel.add(generateButton);
        this.add(boutonPanel,BorderLayout.NORTH);
        


        loadButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenLoadMap());
            this.loadMap = true;
        });

        generateButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenGenerateMap());
            this.loadMap = false;
        });


        
    }

    public GenerateMapPanel getGenerateMapPanel(){
        return this.generateMapPanel;
    }
}
