package com.lllmtb.ui.graphical.SettingsPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;

public class NewOrLoadPanel extends JPanel {

    EventBus eventBus;

    /**
     *
     * @param eventBus the event bus of the game.
     *                 a simple panel allowing you to choose  to load or create a new game
     */
    public NewOrLoadPanel(EventBus eventBus) {
        this.eventBus = new EventBus();


        JButton newGameButton = new JButton("New game");
        newGameButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenSettingsGame());
        });
        this.add(newGameButton);

        JButton loadGameButton = new JButton("Load game");
        loadGameButton.addActionListener(e -> {
            //
        });
        this.add(loadGameButton);


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenHomeEvent());
        });
        this.add(backButton);
    }


}
