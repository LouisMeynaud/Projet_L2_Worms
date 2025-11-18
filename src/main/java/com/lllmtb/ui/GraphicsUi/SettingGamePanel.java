package com.lllmtb.ui.GraphicsUi;

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
    public JSlider equipesSlider;
    public JSlider warmsSlider;
    public JCheckBox windCheckBox;

    public SettingGamePanel(){
        this.setBorder(BorderFactory.createTitledBorder("Paramètres de la partit"));
        this.setLayout(new BorderLayout()); 
        
        
        JPanel contentGrid = new JPanel(new GridBagLayout()); 
        this.add(contentGrid, BorderLayout.NORTH);
        

        //outil qui gere le GridBagLayout
        GridBagConstraints grillePannel = new GridBagConstraints();
        grillePannel.anchor = GridBagConstraints.WEST; 
        grillePannel.insets = new Insets(5, 5, 5, 5); //marge
        grillePannel.weightx = 1.0; // la grille peut s'etirer horizontalement
        grillePannel.fill = GridBagConstraints.HORIZONTAL; //force les composans a remplir la case

        
        
        int row = 0; 

        //parametre partit
        grillePannel.gridwidth = 2; // le titre prend 2 colonne
        grillePannel.gridx = 0;
        grillePannel.gridy = row++;
        JLabel titre1 = new JLabel("PARAMETRE DE LA PARTIE");
        titre1.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre1, grillePannel);

        //nombre d'equipe
        grillePannel.gridwidth = 1; // prend 1 colonne
        grillePannel.gridx = 0;
        grillePannel.gridy = row;
        JLabel NbEquipesLabel = new JLabel("Nombre d'équipes :");
        contentGrid.add(NbEquipesLabel, grillePannel);

        grillePannel.gridx = 1; // 2 eme colone 
        this.equipesSlider = createSlider(2, MAX_EQUIPES, 2, 1);
        contentGrid.add(equipesSlider, grillePannel);
        row++;

        //nom des equipes
        champsNomsEquipes = new JTextField[MAX_EQUIPES];
        for (int i = 0; i < MAX_EQUIPES; i++) {
            grillePannel.gridx = 0;
            grillePannel.gridy = row;
            
            JLabel label = new JLabel("Nom Équipe " + (i + 1) + " :");
            contentGrid.add(label, grillePannel);
            
            grillePannel.gridx = 1;
            champsNomsEquipes[i] = new JTextField("Équipe " + (i + 1), 15);
            contentGrid.add(champsNomsEquipes[i], grillePannel);
            row++;
        }

        //nombre de worms
        grillePannel.gridx = 0;
        grillePannel.gridy = row;
        JLabel NbWormsLabel = new JLabel("Nombre de vers :");
        contentGrid.add(NbWormsLabel, grillePannel);

        grillePannel.gridx = 1;
        this.warmsSlider = createSlider(1, MAX_WORM, 3, 1);
        contentGrid.add(warmsSlider, grillePannel);
        row++;

        //parametre physique
        grillePannel.gridwidth = 2;
        grillePannel.gridx = 0;
        grillePannel.gridy = row++;
        JLabel titre2 = new JLabel("PARAMETRE PHYSIQUE");
        titre2.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre2, grillePannel);

        //vent
        grillePannel.gridwidth = 2; 
        grillePannel.gridx = 0;
        grillePannel.gridy = row;
        this.windCheckBox = new JCheckBox("Vent activé ?");
        contentGrid.add(this.windCheckBox, grillePannel);
        row++;
    }   

  
    private JSlider createSlider(int min, int max, int value, int spacing) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);  // value pour la position de depart
        
        slider.setMajorTickSpacing(spacing); 
        slider.setSnapToTicks(true); 
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        return slider;
    }

    public int getNbEquipes() {
        return this.equipesSlider.getValue();
    }
    
    public int getNbWorms() {
        return this.warmsSlider.getValue();
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