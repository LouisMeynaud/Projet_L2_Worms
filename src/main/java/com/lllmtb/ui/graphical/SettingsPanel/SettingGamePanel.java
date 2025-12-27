package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.lllmtb.physics.GameMode;
import com.lllmtb.ui.graphical.Utilitaire;


public class SettingGamePanel extends JPanel {
    public JTextField[] champsNomsEquipes;
    public static final int MAX_EQUIPES = 4;
    public static final int MAX_WORM = 6;
    public static final int MAX_TIME = 40;
    public static final int MAX_JUMP = 5;
    public static final int MAX_LIFE = 500;

    public JSlider equipesSlider;
    public JSlider wormsSlider;
    public JSlider turnDurationSlider;
    public JCheckBox windCheckBox;
    public JCheckBox friendlyFireCheckBox;
    public JSlider maxJumpSlider;
    public JSlider maxLifeSlider;
    public JTextField gameTitleField;
    public JComboBox<GameMode> gameModeBox;


    /**
     * This page contains all the parameters related to the map.
     */
    public SettingGamePanel() {
        this.setBorder(BorderFactory.createTitledBorder(""));
        this.setLayout(new BorderLayout());


        JPanel contentGrid = new JPanel(new GridBagLayout());
        this.add(contentGrid, BorderLayout.NORTH);


        //tool that manages the GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); //margin
        gbc.weightx = 1.0; // the grid can stretch horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL; //forces the components to fill the box


        int row = 0;

        //game settings
        gbc.gridwidth = 2; // The title takes up 2 columns
        gbc.gridx = 0;
        gbc.gridy = row++;
        JLabel titre1 = new JLabel("PARAMETRES DE LA PARTIE");
        titre1.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre1, gbc);


        //gameName
        this.gameTitleField = new JTextField("Nom de la partie", 15);
        gbc.gridx = 0;
        gbc.gridy = row;

        JLabel labelGame = new JLabel("Nom de la partie :");
        contentGrid.add(labelGame, gbc);

        gbc.gridx = 1;
        contentGrid.add(gameTitleField, gbc);
        row++;


        //numbers team
        gbc.gridwidth = 1; // take 1 column
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel nbEquipesLabel = new JLabel("Nombre d'équipes :");
        contentGrid.add(nbEquipesLabel, gbc);


        gbc.gridx = 1; //second colone
        this.equipesSlider = Utilitaire.createSlider(2, MAX_EQUIPES, 2, 1);

        contentGrid.add(equipesSlider, gbc);
        row++;


        //teams names
        champsNomsEquipes = new JTextField[MAX_EQUIPES];
        for (int i = 0; i < MAX_EQUIPES; i++) {
            gbc.gridx = 0;
            gbc.gridy = row;

            JLabel labelEquipe = new JLabel("Nom Équipe " + (i + 1) + " :");
            contentGrid.add(labelEquipe, gbc);

            gbc.gridx = 1;
            champsNomsEquipes[i] = new JTextField("" + (i + 1), 15);
            contentGrid.add(champsNomsEquipes[i], gbc);
            row++;
        }

        //numbers worms
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel nbWormsLabel = new JLabel("Nombre de vers :");
        contentGrid.add(nbWormsLabel, gbc);

        gbc.gridx = 1;
        this.wormsSlider = Utilitaire.createSlider(1, MAX_WORM, 3, 1);

        contentGrid.add(wormsSlider, gbc);
        row++;

        //maximum life
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel maxLife = new JLabel("Vie maximum :");
        contentGrid.add(maxLife, gbc);

        gbc.gridx = 1;
        this.maxLifeSlider = Utilitaire.createSlider(100, MAX_LIFE, 100, 100);
        contentGrid.add(maxLifeSlider, gbc);
        row++;

        //turn duration
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel turnDurationLabel = new JLabel("Temps d'un tour en seconde :");
        contentGrid.add(turnDurationLabel, gbc);

        gbc.gridx = 1;

        this.turnDurationSlider = Utilitaire.createSlider(10, MAX_TIME, 30, 10);

        contentGrid.add(turnDurationSlider, gbc);
        row++;

        //physics parameter
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row++;
        JLabel titre2 = new JLabel("PARAMETRES PHYSIQUE");
        titre2.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre2, gbc);

        //wind
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row;
        this.windCheckBox = new JCheckBox("Vent activé");
        contentGrid.add(this.windCheckBox, gbc);
        row++;

        //tir allié
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row;
        this.friendlyFireCheckBox = new JCheckBox("Tir allié");
        contentGrid.add(this.friendlyFireCheckBox, gbc);
        row++;



        //turn duration
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel maxJumpLabel = new JLabel("Maximum de saut :");
        contentGrid.add(maxJumpLabel, gbc);

        gbc.gridx = 1;
        this.maxJumpSlider = Utilitaire.createSlider(1, MAX_JUMP, 1, 1);
        contentGrid.add(maxJumpSlider, gbc);
        row++;

        //gameMode
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel gameModeLabel = new JLabel("Mode de jeu :");
        contentGrid.add(gameModeLabel, gbc);

        gbc.gridx = 1;
        this.gameModeBox = new JComboBox<>(GameMode.values());
        contentGrid.add(gameModeBox, gbc);
        row++;

    }


    ////////////GETTERS/////////////////

    /**
     * Gets the number of teams from the slider.
     * @return the number of teams.
     */
    public int getNbEquipes() {
        return this.equipesSlider.getValue();
    }

    /**
     * Gets the number of worms per team from the slider.
     * @return the number of worms per team.
     */
    public int getNbWorms() {
        return this.wormsSlider.getValue();
    }

    /**
     * Gets the maximum number of jumps from the slider.
     * @return the maximum number of jumps.
     */
    public int getMaxJumps() {
        return this.maxJumpSlider.getValue();
    }

    /**
     * Gets the turn duration from the slider.
     * @return the turn duration in seconds.
     */
    public int getTurnDuration() {
        return this.turnDurationSlider.getValue();
    }

    /**
     * gets the maximum life from the slider.
     * @return the maximum life.
     */
    public int getMaxLife() {
        return this.maxLifeSlider.getValue();
    }

    /**
     * Gets the names of the teams from the text fields.
     * @return an array of team names.
     */
    public String[] getNomsEquipes() {
        int nbEquipes = getNbEquipes();
        String[] noms = new String[nbEquipes];
        for (int i = 0; i < nbEquipes; i++) {
            noms[i] = champsNomsEquipes[i].getText();
        }
        return noms;
    }

    /**
     * Gets the game name from the text field.
     * @return the game name.
     */
    public String getGameName() {
        return gameTitleField.getText();
    }

    /**
     * Gets if the wind option is selected.
     * @return true if wind is selected, false otherwise.
     */
    public boolean getWind() {
        return this.windCheckBox.isSelected();
    }

    /**
     * gets if the friendlyFire option is selected
     * @return ture if selected.
     */
    public boolean getFriendlyFire() {
        return this.friendlyFireCheckBox.isSelected();
    }

    public GameMode getGameMode() {
        return (GameMode) this.gameModeBox.getSelectedItem();
    }
}