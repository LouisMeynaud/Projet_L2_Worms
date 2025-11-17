package com.lllmtb.ui.GraphicsUi;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;


public class SettingGamePanel extends JPanel {
    public JPanel NomsEquipesPanel;
    public JTextField[] champsNomsEquipes;
    public JLabel[] labelsEquipes;
    public final int MAX_EQUIPES = 4;
    public final int MAX_WORM = 6;

    public JSlider equipesSlider;
    public JSlider warmsSlider;

    public SettingGamePanel(){
        this.setBorder(BorderFactory.createTitledBorder("Paramètres de la partit")); // permet d'afficher un titre;
        this.setLayout(new BorderLayout()); 
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));

        JLabel titre1 = new JLabel("PARAMETRE DE LA PARTIE");
        titre1.setFont(new Font("Arial", Font.BOLD, 16));
        contentBox.add(titre1);
        contentBox.add(nbEquipePanel());
        contentBox.add(nomsEquipesPanel());
        contentBox.add(nbWormsPanel());
        JLabel titre2 = new JLabel("PARAMETRE PHYSIQUE");
        titre2.setFont(new Font("Arial",Font.BOLD,16));
        contentBox.add(titre2);

        this.add(contentBox,BorderLayout.NORTH);

    }   


    public JPanel nbEquipePanel(){
        //nombre d'equipes
        JPanel nbEquipePanel = new JPanel();
        nbEquipePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // le layout "paragraphe"
        this.equipesSlider = new JSlider(JSlider.HORIZONTAL,2,MAX_EQUIPES,2); // slider allant de 2 a MAX_EQUIPE
        this.equipesSlider.setMajorTickSpacing(1); //cran toute les 1 valeurs
        this.equipesSlider.setSnapToTicks(true); //curseur ne peux aller entre 2 cran
        this.equipesSlider.setPaintTicks(true);//affiche les cran
        this.equipesSlider.setPaintLabels(true);//affiche les chiffre sous les cran

        JLabel NbEquipesLabel = new JLabel("Nombre d'équipes :");
        nbEquipePanel.add(NbEquipesLabel);
        nbEquipePanel.add(equipesSlider);

        return nbEquipePanel;
    }

     public JPanel nbWormsPanel(){
        //nombre d'equipes
        JPanel nbWormsPanel = new JPanel();
        nbWormsPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // le layout "paragraphe"
        this.warmsSlider= new JSlider(JSlider.HORIZONTAL, 1, MAX_WORM, 3); // slider allant de 2 a MAX_Worms
        warmsSlider.setMajorTickSpacing(1); //cran toute les 1 valeurs
        warmsSlider.setSnapToTicks(true); //curseur ne peux aller entre 2 cran
        warmsSlider.setPaintTicks(true);//affiche les cran
        warmsSlider.setPaintLabels(true);//affiche les chiffre sous les cran

        JLabel NbWormsLabel = new JLabel("Nombre d'équipes :");
        nbWormsPanel.add(NbWormsLabel);
        nbWormsPanel.add(warmsSlider);

        return nbWormsPanel;
    }

    
    public JPanel nomsEquipesPanel(){
        JPanel nomsEquipesPanel = new JPanel();
        nomsEquipesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        champsNomsEquipes = new JTextField[MAX_EQUIPES];
        labelsEquipes = new JLabel[MAX_EQUIPES];

        for (int i = 0; i < MAX_EQUIPES; i++) {
            labelsEquipes[i] = new JLabel("Nom Équipe " + (i + 1) + " :");
            champsNomsEquipes[i] = new JTextField(15);
            
            nomsEquipesPanel.add(labelsEquipes[i]);
            nomsEquipesPanel.add(champsNomsEquipes[i]);
        }

        return nomsEquipesPanel;


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

}
