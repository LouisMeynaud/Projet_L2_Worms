package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;


public class SettingGamePanel extends JPanel {
    public JTextField[] champsNomsEquipes;
    public final int MAX_EQUIPES = 4;
    public final int MAX_WORM = 6;
    public final int MAX_TIME = 40;
    public JSlider equipesSlider;
    public JSlider wormsSlider;
    public JSlider turnDurationSlider;
    public JCheckBox windCheckBox;


    
    /**
     * This page contains all the parameters related to the map.
     */
    public SettingGamePanel(){
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
        JLabel titre1 = new JLabel("PARAMETRE DE LA PARTIE");
        titre1.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre1, gbc);

        //numbers team
        gbc.gridwidth = 1; // take 1 columns
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel NbEquipesLabel = new JLabel("Nombre d'équipes :");
        contentGrid.add(NbEquipesLabel, gbc);

        gbc.gridx = 1; // second colone 
        this.equipesSlider = CreateSlider.createSlider(2, MAX_EQUIPES, 2, 1);
        contentGrid.add(equipesSlider, gbc);
        row++;

        //teams names
        champsNomsEquipes = new JTextField[MAX_EQUIPES];
        for (int i = 0; i < MAX_EQUIPES; i++) {
            gbc.gridx = 0;
            gbc.gridy = row;
            
            JLabel label = new JLabel("Nom Équipe " + (i + 1) + " :");
            contentGrid.add(label, gbc);
            
            gbc.gridx = 1;
            champsNomsEquipes[i] = new JTextField("Équipe " + (i + 1), 15);
            contentGrid.add(champsNomsEquipes[i], gbc);
            row++;
        }

        //numbers worms
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel NbWormsLabel = new JLabel("Nombre de vers :");
        contentGrid.add(NbWormsLabel, gbc);

        gbc.gridx = 1;
        this.wormsSlider = CreateSlider.createSlider(1, MAX_WORM, 3, 1);
        contentGrid.add(wormsSlider, gbc);
        row++;

        //turn duration
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel turnDurationLabel = new JLabel("Temps d'un tour en seconde:");
        contentGrid.add(turnDurationLabel, gbc);

        gbc.gridx = 1;
        this.turnDurationSlider = CreateSlider.createSlider(10, MAX_TIME, 10, 10);
        contentGrid.add(turnDurationSlider, gbc);
        row++;

        //physics parameter
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row++;
        JLabel titre2 = new JLabel("PARAMETRE PHYSIQUE");
        titre2.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre2, gbc);

        //wind
        gbc.gridwidth = 2; 
        gbc.gridx = 0;
        gbc.gridy = row;
        this.windCheckBox = new JCheckBox("Vent activé ?");
        contentGrid.add(this.windCheckBox, gbc);
        row++;
    }   

    /**
     * This method aims to simplify the creation of sliders without code duplication.
     * 
     * @param min the minimun value
     * @param max the maximum value
     * @param value the default value
     * @param spacing the spacing between two value
     * @return a Slider with desired parameter 
     */

    public int getNbEquipes() {
        return this.equipesSlider.getValue();
    }
    
    public int getNbWorms() {
        return this.wormsSlider.getValue();
    }

    public int getTurnDuration() {
        return this.turnDurationSlider.getValue();
    }
    
    public String[] getNomsEquipes() {
        int nbEquipes = getNbEquipes();
        String[] noms = new String[nbEquipes];
        for (int i = 0; i < nbEquipes; i++) {
            noms[i] = champsNomsEquipes[i].getText();
        }
        return noms;
    }
    
    public boolean getWind(){
        return this.windCheckBox.isSelected();
    }
}