package com.lllmtb.ui.graphical;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.LoadEvent;
import com.lllmtb.events.game.SaveEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.ui.graphical.*;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.graphical.GamePanel.GameScreen;
import com.lllmtb.ui.graphical.OtherPanel.EndGameScreenPanel;
import com.lllmtb.ui.graphical.OtherPanel.HomeScreenPanel;
import com.lllmtb.ui.graphical.SettingsPanel.LoadGamePanel;
import com.lllmtb.ui.graphical.SettingsPanel.NewOrLoadPanel;
import com.lllmtb.ui.graphical.SettingsPanel.SettingsStartPanel;
import com.lllmtb.ui.graphical.Utilitaire.MusicSettingsScreenPanel;
import com.lllmtb.ui.graphical.Utilitaire.SoundManager;
import com.lllmtb.events.game.CloseGameEvent;
import com.lllmtb.engine.GameWorld;

public class GraphicalUI implements UI {

    public JFrame windows;
    public EventBus systemBus;
    public CardLayout cardLayout; // the container for all pages
    public JPanel mainPanel;
    public JPanel loadGamePanel;
    private EndGameScreenPanel endGameScreen;
    private GameWorld currentWorld = null;

    /**
     * @param systemBus the system bus of the game
     * This class represents the window; it's the container for all other pages.
     * We'll display the home screen, the game panel, the start setting screen, the settings, etc., inside it.
     */
    public GraphicalUI(EventBus systemBus) {

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

        this.systemBus = systemBus;

        // the windows
        windows = new JFrame();
        windows.setTitle("Worms-MI4");
        windows.setExtendedState(JFrame.MAXIMIZED_BOTH); //sets the window to the maximum screen size
        windows.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        windows.setResizable(true);
        windows.setMinimumSize(new Dimension(800, 400));
        windows.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            systemBus.postSync(new CloseGameEvent());
        }
        });



        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        HomeScreenPanel homeScreen = new HomeScreenPanel(this.systemBus);
        SettingsStartPanel startSettingScreen = new SettingsStartPanel(this.systemBus);
        MusicSettingsScreenPanel musicSettingsScreen = new MusicSettingsScreenPanel(this.systemBus);
        NewOrLoadPanel newOrLoadPanel = new NewOrLoadPanel(systemBus);
        this.loadGamePanel = new LoadGamePanel(systemBus);
        endGameScreen = new EndGameScreenPanel(systemBus);
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
        systemBus.subscribe(ScreenNewOrLoadEvent.class, event -> {
            cardLayout.show(mainPanel, "newOrLoadPanel");
            Logger.logger.debug("Show ScreenNewOrLoad");

        });

        systemBus.subscribe(ScreenHomeEvent.class, event -> {
            cardLayout.show(mainPanel, "homeScreen");
            Logger.logger.debug("Show HomeScreen");
        });

        systemBus.subscribe(StartGameEvent.class, event -> {
            // Remove old game panel to prevent memory leak
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof GameScreen) mainPanel.remove(comp);
            }
            GameScreen gamePanel = new GameScreen(this.systemBus);
            mainPanel.add(gamePanel, "gamePanel");
            cardLayout.show(mainPanel, "gamePanel");
            Logger.logger.debug("Show GameScreen");
        });

        systemBus.subscribe(LoadEvent.class, event -> {
            GameScreen gamePanel = new GameScreen(this.systemBus);
            mainPanel.add(gamePanel, "gamePanel"); // la page de jeu
            cardLayout.show(mainPanel, "gamePanel");
            Logger.logger.debug("Show GameScreen");
        });

        systemBus.subscribe(ScreenSettingsGame.class, event -> {
            cardLayout.show(mainPanel, "startSettingScreen");
            Logger.logger.debug("Show SettingScreen");
        });

        systemBus.subscribe(ScreenLoadGame.class, event -> {
            this.loadGamePanel =  new LoadGamePanel(this.systemBus);
            mainPanel.add(loadGamePanel, "loadGamePanel");
            cardLayout.show(mainPanel, "loadGamePanel");
            Logger.logger.debug("Show loadGamePanel");

        });

        systemBus.subscribe(ScreenMusicSettingsEvent.class, event -> {
            cardLayout.show(mainPanel, "musicSettingsScreen");
            Logger.logger.debug("Show musicSettingsScreen");

        });

        systemBus.subscribe(ScreenEndGameEvent.class, event -> {
            endGameScreen.setWinner(event.getTeam());
            cardLayout.show(mainPanel, "endGameScreen");
            Logger.logger.debug("Show endGameScreen");

        });

        systemBus.subscribe(WorldCreatedEvent.class, event -> {
            this.currentWorld = event.world();
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
        if (currentWorld != null) {
            currentWorld.getBus().postSync(new SaveEvent());
            Logger.logger.info("Game saved before closing.");
        }
        SoundManager.close();
        windows.dispose();
        Logger.logger.info("game closed.");
        System.exit(0);
    }
}