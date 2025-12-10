package com.lllmtb.ui.graphical.SettingsPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenLoadGame;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class NewOrLoadPanel extends JPanel {

    EventBus eventBus;

    /**
     * Constructor of a panel that allows to choose between creating a new game or loading an existing one.
     * @param eventBus
     */
    public NewOrLoadPanel(EventBus eventBus) {
        this.eventBus = new EventBus();



        JButton newGameButton = Utilitaire.createImageButton(TextureManager.UI, "NewGame", 128, 64, 64, 48, "New Game","Arial",16);
        newGameButton.addActionListener(e -> {

            eventBus.postQueued(new ScreenSettingsGame());
        });
        this.add(newGameButton);

        JButton loadGameButton = Utilitaire.createImageButton(TextureManager.UI, "LoadGame", 128, 64, 64, 48, "Load Game","Arial",16);
        loadGameButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenLoadGame());
        });
        this.add(loadGameButton);



        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Back", 128, 64, 64, 48, "Back","Arial",16);
    backButton.addActionListener(e -> {

            eventBus.postQueued(new ScreenHomeEvent());
        });
        this.add(backButton);
    }


}
