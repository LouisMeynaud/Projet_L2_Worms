package com.lllmtb.ui.GraphicsUi;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.ScreenNewOrLoadEvent;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.lllmtb.events.TheEvents.ScreenHomeEvent;
import com.lllmtb.events.TheEvents.ScreenSettingsGame;
import com.lllmtb.events.TheEvents.StartGameEvent;
import com.lllmtb.ui.*;
import com.lllmtb.ui.GraphicsUi.GamePanel.GameScreen;
import com.lllmtb.ui.GraphicsUi.GamePanel.GameWorldPanel;
import com.lllmtb.ui.GraphicsUi.SettingsPanel.NewOrLoadPanel;
import com.lllmtb.ui.GraphicsUi.SettingsPanel.SettingsStartPanel;;

public class WindowsUi implements  UI{
    
    public JFrame windows;
    public EventBus eventBus;
    public CardLayout cardLayout;// le controleur de page
    public JPanel mainPanel;
    
    public WindowsUi(EventBus eventbus){

        try {
            // on cherche le "style" nimbus
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    // On applique Nimbus
                    UIManager.setLookAndFeel(info.getClassName());
                    break; 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.eventBus = eventbus;
        
        windows = new JFrame();
        windows.setTitle("Worms-MI4");
        windows.setExtendedState(JFrame.MAXIMIZED_BOTH);//met la fenetre a la taille maximal de l'ecran
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        HomeScreenPanel homeScreen = new HomeScreenPanel(this.eventBus);
        SettingsStartPanel startSettingScreen = new SettingsStartPanel(this.eventBus);
        GameScreen gamePanel = new GameScreen(eventbus);
        NewOrLoadPanel newOrLoadPanel = new NewOrLoadPanel(eventbus);
        mainPanel.add(homeScreen,"homeScreen");
        mainPanel.add(startSettingScreen,"startSettingScreen");
        mainPanel.add(gamePanel,"gamePanel");
        mainPanel.add(newOrLoadPanel,"newOrLoadPanel");
        windows.add(mainPanel);

        subscribeToNavigationEvents();
    }



    private void subscribeToNavigationEvents() {
        
    
        eventBus.subscribe(ScreenNewOrLoadEvent.class, event -> {
                cardLayout.show(mainPanel, "newOrLoadPanel");
            
        });

        eventBus.subscribe(ScreenHomeEvent.class, event->{
            cardLayout.show(mainPanel,"homeScreen");
        });

        eventBus.subscribe(StartGameEvent.class, event -> {
            cardLayout.show(mainPanel, "gamePanel");
        });

        eventBus.subscribe(ScreenSettingsGame.class, event -> {
                cardLayout.show(mainPanel, "startSettingScreen");
        });
    }


    @Override
    public void start() {
        windows.setVisible(true);
    }
    public void close(){
    }

    public EventBus getBus(){
        return this.eventBus;
    }
}   
