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

public class WindowsUi extends  UI{
    
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
        windows.setSize(500,500);
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //windows.setLocation(0,0);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        JPanel homeScreen = createHomeScreen();
        JPanel startSettingScreen = createStartSettingScreen();
        mainPanel.add(homeScreen,"homeScreen");
        mainPanel.add(startSettingScreen,"startSettingScreen");
        windows.add(mainPanel);

        subscribeToNavigationEvents();

        windows.setVisible(true);
    }

    public JPanel createHomeScreen(){
        //creation des bouton et du panneau
        JButton startButton = new JButton("start");
        JButton SettingButton = new JButton("game Setting");
        JButton CloseButton = new JButton("close");
        JPanel panel = new JPanel();
        //ajout des bouton au panneau
        panel.add(startButton);

        startButton.addActionListener(e -> {
            eventBus.postQueued(new ShowStartSettingsEvent());
        });
        panel.add(SettingButton);
        panel.add(CloseButton);
        //ajout du panneau a la fenetre
        return panel;

    }

    public JPanel createStartSettingScreen(){
        JButton starButton = new JButton("Start");
        JButton backButton = new JButton("Back");
        JPanel panel = new JPanel();
        panel.add(backButton);
        backButton.addActionListener(e -> {
            eventBus.postQueued(new ShowHomeScreenEvent());
        });

        panel.add(starButton);
        starButton.addActionListener(e -> {
            // recupere les donnée
            eventBus.postQueued(new StartGameEvent());
        });
        return panel;

    }









    private void subscribeToNavigationEvents() {
        
        // On s'abonne à l'événement qu'on a créé et agit quand on le recois...
        eventBus.subscribe(ShowStartSettingsEvent.class, event -> {
                System.out.println("bouton cliquer");
                cardLayout.show(mainPanel, "startSettingScreen");
            
        });

        eventBus.subscribe(ShowHomeScreenEvent.class, event->{
            cardLayout.show(mainPanel,"homeScreen");
        });

        eventBus.subscribe(StartGameEvent.class, event -> {
            setSettings();
        });
    }

    public GameSettings setSettings(){
        return new GameSettings(1, null, 1, false, null, false);
    }
    
    
    
    
    public GameSettings promptSettings(){
        return null;
    }

    public boolean readyToPlay(){
        return false;
    }
    public void close(){
    }

    public EventBus getBus(){
        return this.eventBus;
    }
}   
