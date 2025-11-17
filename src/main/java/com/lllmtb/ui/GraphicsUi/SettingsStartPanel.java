package com.lllmtb.ui.GraphicsUi;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.ScreenHomeEvent;
import com.lllmtb.events.TheEvents.ScreenNewOrLoadEvent;
import com.lllmtb.events.TheEvents.StartGameEvent;

public class SettingsStartPanel extends JPanel {
    public EventBus eventBus;
    public SettingGamePanel gameSettingsPanel;
    public JPanel panelMap;

    public SettingsStartPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.PanelSetting();
        

    }

    public void PanelSetting() {

        this.setLayout(new BorderLayout(10, 10)); // zone nord,Sud,...

        // titre (image plus tard);Zone nord
        JLabel titre = new JLabel("CREER UNE NOUVELLE PARTIE");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24)); // bold pour gras
        this.add(titre, BorderLayout.NORTH);

        // bouton lancée et retour: zone sud
        JPanel ZoneSud = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // met les bouton a droite (flowLayout afficheles un apres les autre);
        JButton backButton = new JButton("Retour");
        JButton StarButton = new JButton("Lancer la partie");

        ZoneSud.add(backButton);
        ZoneSud.add(StarButton);
        this.add(ZoneSud, BorderLayout.SOUTH);


        //zone centre (les deux partit (parametre map/partit))
        JPanel PanelCentre = new JPanel(new GridLayout(1, 2, 10, 0));// 1 ligne 2 colonne , 10 de marge horizontale
        JPanel panelMap = createMapSettingsPanel();
        this.gameSettingsPanel = new SettingGamePanel();

        PanelCentre.add(panelMap);
        PanelCentre.add(gameSettingsPanel);
        this.add(PanelCentre, BorderLayout.CENTER);


        backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });

        StarButton.addActionListener(e -> {
            // recupere les donnée
            GameSettings settings = setSettings();
            eventBus.postQueued(new StartGameEvent(settings));
        });
    }


    public JPanel createMapSettingsPanel(){
        JPanel mapSettingsPanel = new JPanel();
        mapSettingsPanel.setBorder(BorderFactory.createTitledBorder("Paramètres de la map")); // permet d'afficher un titre;

        return mapSettingsPanel;
    }


    public GameSettings setSettings() {
        int nbEquipes = this.gameSettingsPanel.getNbEquipes();
        int nbVWorms = gameSettingsPanel.getNbWorms();
        String[] nomsEquipes = gameSettingsPanel.getNomsEquipes();
        return new GameSettings(nbEquipes, new String[] { "test" }, nbVWorms, false, null, false);
    }
}
