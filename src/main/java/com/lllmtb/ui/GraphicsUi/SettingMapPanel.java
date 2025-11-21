package com.lllmtb.ui.GraphicsUi;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Event;
import java.awt.FlowLayout;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.ScreenGenerateMap;
import com.lllmtb.events.TheEvents.ScreenLoadMap;
import com.lllmtb.events.TheEvents.ScreenNewOrLoadEvent;

public class SettingMapPanel extends JPanel{
    public CardLayout cardLayout;
    public EventBus eventBus;
    
    public SettingMapPanel(EventBus eventBus){
        this.eventBus = eventBus;
        this.setBorder(BorderFactory.createTitledBorder("Paramètres de la map"));
        cardLayout = new CardLayout();
        this.setLayout(new BorderLayout());
        JPanel cardPanel= new JPanel(this.cardLayout);
        this.add(cardPanel);

        JButton LoadButton = new JButton("load");
        JButton GenerateButton = new JButton("generateMap");

        JPanel generateMapPanel = new GenerateMapPanel();
        JPanel loadMapPanel = new LoadMapPannel();
        cardPanel.add(loadMapPanel,"loadMapPanel");
        cardPanel.add(generateMapPanel,"generateMapPanel");
        

        eventBus.subscribe(ScreenGenerateMap.class, event -> {
                cardLayout.show(cardPanel, "generateMapPanel");
                System.out.println("event generate recuperer");
            
        });

        eventBus.subscribe(ScreenLoadMap.class, event -> {
                cardLayout.show(cardPanel, "loadMapPanel");
                System.out.println("event load recuperer");
            
        });

        JPanel boutonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonPanel.add(LoadButton);
        boutonPanel.add(GenerateButton);
        this.add(boutonPanel,BorderLayout.NORTH);
        


        LoadButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenLoadMap());
        });

        GenerateButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenGenerateMap());
        });


        
    }
}
