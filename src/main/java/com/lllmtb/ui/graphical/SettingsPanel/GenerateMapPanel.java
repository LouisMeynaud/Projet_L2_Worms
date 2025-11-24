package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.lllmtb.terrain.generation.LandScape;

public class GenerateMapPanel extends JPanel{

    public JTextField[] champsNomsEquipes;
    public final int MAX_WIDTH = 400;
    public final int MAX_WORM = 6;
    public JSlider WidthMapSlider;
    public JSlider reliefSlider;
    public JCheckBox windCheckBox;
    
    public GenerateMapPanel(){
        this.setLayout(new BorderLayout()); 
        
        
        JPanel contentGrid = new JPanel(new GridBagLayout()); 
        this.add(contentGrid, BorderLayout.NORTH);
        

        //outil qui gere le GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.insets = new Insets(5, 5, 5, 5); //marge
        gbc.weightx = 1.0; // la grille peut s'etirer horizontalement
        gbc.fill = GridBagConstraints.HORIZONTAL; //force les composans a remplir la case

        
        
        int row = 0; 

        //parametre de la map
        gbc.gridwidth = 2; // le titre prend 2 colonne
        gbc.gridx = 0;
        gbc.gridy = row++;
        JLabel titre1 = new JLabel("PARAMETRE DE LA MAP");
        titre1.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre1, gbc);

        //largeurMap
        gbc.gridwidth = 1; // prend 1 colonne
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel mapWidthLabel = new JLabel("largeur map");
        contentGrid.add(mapWidthLabel, gbc);

        gbc.gridx = 1; // 2 eme colone 
        this.WidthMapSlider = createSlider(150, MAX_WIDTH, 200, 50);
        contentGrid.add(WidthMapSlider, gbc);
        row++;

    

        

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel reliefLabel = new JLabel("Type de relief :");
        contentGrid.add(reliefLabel, gbc);

        gbc.gridx = 1;
        this.reliefSlider = createReliefSlider(); 
        contentGrid.add(reliefSlider, gbc);
        row++;


     }   

  
    private JSlider createSlider(int min, int max, int value, int spacing) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);  
        
        slider.setMajorTickSpacing(spacing); 
        slider.setSnapToTicks(true); 
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        return slider;
    }

    private JSlider createReliefSlider() {
        
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 3, 1);
        
        slider.setMajorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        
        
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        
        labelTable.put(0, new JLabel("Plat"));
        labelTable.put(1, new JLabel("Vallonné"));
        labelTable.put(2, new JLabel("Accidenté"));
        labelTable.put(3, new JLabel("Anarchique"));
        
        
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true); 
        
        return slider;
    }

    public LandScape getReliefType() {
        int val = this.reliefSlider.getValue();
        switch (val) {
            case 0: return LandScape.FLAT;
            case 1: return LandScape.HILLY;
            case 2: return LandScape.JAGGED;
            case 3: return LandScape.CHAOTIC;
            default: return LandScape.HILLY;
        }
    }

    public int getWidthMapSlider() {
        return this.WidthMapSlider.getValue();
    }

}

