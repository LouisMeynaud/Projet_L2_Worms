package com.lllmtb.ui.GraphicsUi;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.ShowStartSettingsEvent;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.events.TheEvents.ShowHomeScreenEvent;
import com.lllmtb.events.TheEvents.StartGameEvent;
import com.lllmtb.ui.*;;

public class WindowsUi implements  UI{
    
    public JFrame windows;
    public EventBus eventBus;
    public CardLayout cardLayout;// le controleur de page
    public JPanel mainPanel;
    
    public WindowsUi(EventBus eventbus){
        this.eventBus = eventbus;
        windows = new JFrame();
        windows.setTitle("Worms-MI4");
        //Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        //windows.setSize(dimension.width, dimension.height);
        windows.setExtendedState(JFrame.MAXIMIZED_BOTH);//met la fenetre a la taille maximal de l'ecran
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //windows.setLocation(0,0);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        HomeScreenPanel homeScreen = new HomeScreenPanel(this.eventBus);
        SettingsGamePanel startSettingScreen = new SettingsGamePanel(this.eventBus);
        GamePanel gamePanel = new GamePanel(eventbus);
        mainPanel.add(homeScreen,"homeScreen");
        mainPanel.add(startSettingScreen,"startSettingScreen");
        mainPanel.add(gamePanel,"gamePanel");
        windows.add(mainPanel);

        subscribeToNavigationEvents();
    }



    private void subscribeToNavigationEvents() {
        
    
        eventBus.subscribe(ShowStartSettingsEvent.class, event -> {
                System.out.println("bouton cliquer");
                cardLayout.show(mainPanel, "startSettingScreen");
            
        });

        eventBus.subscribe(ShowHomeScreenEvent.class, event->{
            cardLayout.show(mainPanel,"homeScreen");
        });

        eventBus.subscribe(StartGameEvent.class, event -> {
            cardLayout.show(mainPanel, "gamePanel");
        });
    }


    public boolean readyToPlay(){
        return false;
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
