package com.lllmtb.ui.graphical.SettingsPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class NewOrLoadPanel extends JPanel {

    EventBus eventBus;

    /**
     *
     * @param eventBus the event bus of the game.
     *                 a simple panel allowing you to choose  to load or create a new game
     */
    public NewOrLoadPanel(EventBus eventBus) {
        this.eventBus = new EventBus();


        JButton newGameButton = Utilitaire.createImageButton(TextureManager.UI, "NewGame", 128, 64, 64, 48);
        newGameButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenSettingsGame());
        });
        this.add(newGameButton);

        JButton loadGameButton = Utilitaire.createImageButton(TextureManager.UI, "LoadGame", 128, 64, 64, 48);
        loadGameButton.addActionListener(e -> {
            //
        });
        this.add(loadGameButton);


        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Back", 128, 64, 64, 48);
    backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenHomeEvent());
        });
        this.add(backButton);
    }


}
