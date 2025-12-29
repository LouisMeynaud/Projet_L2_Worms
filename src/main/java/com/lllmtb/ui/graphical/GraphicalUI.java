package com.lllmtb.ui.graphical;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenLoadGame;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.graphical.GamePanel.GameScreen;
import com.lllmtb.ui.graphical.SettingsPanel.LoadGamePanel;
import com.lllmtb.ui.graphical.SettingsPanel.NewOrLoadPanel;
import com.lllmtb.ui.graphical.SettingsPanel.SettingsStartPanel;;

public class GraphicalUI implements UI {

    public JFrame windows;
    public EventBus eventBus;
    public CardLayout cardLayout; // the container for all pages
    public JPanel mainPanel;


    /**
     *
     * @param eventbus the event bus of the game
     *
     *                 This class represents the window; it's the container for all our other pages.
     *                 We'll display the home screen, the game panel, the start setting screen, the settings, etc., inside it.
     */
    public GraphicalUI(EventBus eventbus) {

        try {
            // recherche the style nimbus
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

        // the windows
        windows = new JFrame();
        windows.setTitle("Worms-MI4");
        windows.setExtendedState(JFrame.MAXIMIZED_BOTH); //sets the window to the maximum screen size
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windows.setResizable(true);
        windows.setMinimumSize(new Dimension(800, 400));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        HomeScreenPanel homeScreen = new HomeScreenPanel(this.eventBus);
        SettingsStartPanel startSettingScreen = new SettingsStartPanel(this.eventBus);
        GameScreen gamePanel = new GameScreen(eventbus);
        NewOrLoadPanel newOrLoadPanel = new NewOrLoadPanel(eventbus);
        LoadGamePanel loadGamePanel = new LoadGamePanel(eventbus);
        mainPanel.add(homeScreen, "homeScreen"); // the principal page
        mainPanel.add(startSettingScreen, "startSettingScreen"); // the game launch settings page
        mainPanel.add(gamePanel, "gamePanel"); // la page de jeu
        mainPanel.add(newOrLoadPanel, "newOrLoadPanel"); //The page allowing you to choose whether you want to load or create a game
        mainPanel.add(loadGamePanel, "loadGamePanel");
        windows.add(mainPanel); // the page containing the others, (the card layout).


        subscribeToNavigationEvents();
    }

    /**
     * Subscribes to navigation events to switch between different screens.
     */
    private void subscribeToNavigationEvents() {
        eventBus.subscribe(ScreenNewOrLoadEvent.class, event -> {
            cardLayout.show(mainPanel, "newOrLoadPanel");
            Logger.logger.debug("Show ScreenNewOrLoad");

        });

        eventBus.subscribe(ScreenHomeEvent.class, event -> {
            cardLayout.show(mainPanel, "homeScreen");
            Logger.logger.debug("Show HomeScreen");
        });

        eventBus.subscribe(StartGameEvent.class, event -> {
            cardLayout.show(mainPanel, "gamePanel");
            GameScreen gamePanel = new GameScreen(this.eventBus);
            mainPanel.add(gamePanel, "gamePanel"); // la page de jeu
            Logger.logger.debug("Show GameScreen");
        });

        eventBus.subscribe(ScreenSettingsGame.class, event -> {
            cardLayout.show(mainPanel, "startSettingScreen");
            Logger.logger.debug("Show SettingScreen");
        });

        eventBus.subscribe(ScreenLoadGame.class, event -> {
            cardLayout.show(mainPanel, "loadGamePanel");
            Logger.logger.debug("Show loadGamePanel");

        });
    }



    /**
     * makes the page visible.
     */
    @Override
    public void start() {
        windows.setVisible(true);
    }

    @Override
    public void close() {
        System.exit(0);
    }

}