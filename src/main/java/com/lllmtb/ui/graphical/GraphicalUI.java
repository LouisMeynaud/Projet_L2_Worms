package com.lllmtb.ui.graphical;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.LoadEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.ui.graphical.*;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.graphical.GamePanel.GameScreen;
import com.lllmtb.ui.graphical.OtherPanel.EndGameScreenPanel;
import com.lllmtb.ui.graphical.OtherPanel.HomeScreenPanel;
import com.lllmtb.ui.graphical.SettingsPanel.LoadGamePanel;
import com.lllmtb.ui.graphical.SettingsPanel.NewOrLoadPanel;
import com.lllmtb.ui.graphical.SettingsPanel.SettingsStartPanel;
import com.lllmtb.ui.graphical.Utilitaire.MusicSettingsScreenPanel;

public class GraphicalUI implements UI {

    public JFrame windows;
    public EventBus eventBus;
    public CardLayout cardLayout; // the container for all pages
    public JPanel mainPanel;
    public JPanel loadGamePanel;
    private EndGameScreenPanel endGameScreen;

    /**
     * @param eventbus the event bus of the game
     * This class represents the window; it's the container for all our other pages.
     * We'll display the home screen, the game panel, the start setting screen, the settings, etc., inside it.
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
            Logger.logger.error("Could not set Nimbus Look and Feel", e);
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
        MusicSettingsScreenPanel musicSettingsScreen = new MusicSettingsScreenPanel(this.eventBus);
        NewOrLoadPanel newOrLoadPanel = new NewOrLoadPanel(eventbus);
        this.loadGamePanel = new LoadGamePanel(eventbus);
        endGameScreen = new EndGameScreenPanel(eventbus);
        mainPanel.add(homeScreen, "homeScreen"); // the principal page
        mainPanel.add(startSettingScreen, "startSettingScreen"); // the game launch settings page
        mainPanel.add(newOrLoadPanel, "newOrLoadPanel"); //The page allowing you to choose whether you want to load or create a game
        mainPanel.add(loadGamePanel, "loadGamePanel");
        mainPanel.add(musicSettingsScreen, "musicSettingsScreen"); // the page containing the music settings
        mainPanel.add(endGameScreen, "endGameScreen");  // the endgame page
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
            // Remove old game panel to prevent memory leak
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof GameScreen) mainPanel.remove(comp);
            }
            GameScreen gamePanel = new GameScreen(this.eventBus);
            mainPanel.add(gamePanel, "gamePanel");
            cardLayout.show(mainPanel, "gamePanel");
            Logger.logger.debug("Show GameScreen");
        });

        eventBus.subscribe(LoadEvent.class, event -> {
            GameScreen gamePanel = new GameScreen(this.eventBus);
            mainPanel.add(gamePanel, "gamePanel"); // la page de jeu
            cardLayout.show(mainPanel, "gamePanel");
            Logger.logger.debug("Show GameScreen");
        });

        eventBus.subscribe(ScreenSettingsGame.class, event -> {
            cardLayout.show(mainPanel, "startSettingScreen");
            Logger.logger.debug("Show SettingScreen");
        });

        eventBus.subscribe(ScreenLoadGame.class, event -> {
            this.loadGamePanel =  new LoadGamePanel(this.eventBus);
            mainPanel.add(loadGamePanel, "loadGamePanel");
            cardLayout.show(mainPanel, "loadGamePanel");
            Logger.logger.debug("Show loadGamePanel");

        });

        eventBus.subscribe(ScreenMusicSettingsEvent.class, event -> {
            cardLayout.show(mainPanel, "musicSettingsScreen");
            Logger.logger.debug("Show musicSettingsScreen");

        });

        eventBus.subscribe(ScreenEndGameEvent.class, event -> {
            endGameScreen.setWinner(event.getTeam());
            cardLayout.show(mainPanel, "endGameScreen");
            Logger.logger.debug("Show endGameScreen");

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