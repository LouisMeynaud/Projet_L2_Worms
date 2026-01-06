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
import com.lllmtb.ui.graphical.Utilitaire.Utilitaire;

public class GenerateMapPanel extends JPanel {

    public JTextField[] champsNomsEquipes;
    public static final int MAX_WIDTH = 400;
    public JSlider widthMapSlider;
    public JSlider reliefSlider;
    public JCheckBox flightIslandCheckBox;

    /**
     * Constructor of the settings panel of map.
     */
    public GenerateMapPanel() {
        this.setLayout(new BorderLayout());


        JPanel contentGrid = new JPanel(new GridBagLayout());
        this.add(contentGrid, BorderLayout.NORTH);


        //Tool that manages the GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); //margin
        gbc.weightx = 1.0; // The grid can stretch horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL; //forces the components to fill the box


        int row = 0;

        //map parameter
        gbc.gridwidth = 2; // The title takes up 2 columns
        gbc.gridx = 0;
        gbc.gridy = row++;
        JLabel titre1 = new JLabel("PARAMETRES DE LA MAP");
        titre1.setFont(new Font("Arial", Font.BOLD, 16));
        contentGrid.add(titre1, gbc);

        //MapWidth
        gbc.gridwidth = 1; // take one column
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel mapWidthLabel = new JLabel("Largeur map :");
        contentGrid.add(mapWidthLabel, gbc);

        gbc.gridx = 1; // second columns

        this.widthMapSlider = Utilitaire.createSlider(150, MAX_WIDTH, 200, 50);
        contentGrid.add(widthMapSlider, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel reliefLabel = new JLabel("Type de relief :");
        contentGrid.add(reliefLabel, gbc);

        gbc.gridx = 1;
        this.reliefSlider = createReliefSlider();
        contentGrid.add(reliefSlider, gbc);
        row++;

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row;
        this.flightIslandCheckBox = new JCheckBox("Ile volante");
        contentGrid.add(this.flightIslandCheckBox, gbc);
        row++;
    }

    /**
     *
     * @return a slider to choose the map's topography
     */
    private JSlider createReliefSlider() {

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 4, 1);

        slider.setMajorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);


        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        labelTable.put(0, new JLabel("Super Plat"));
        labelTable.put(1, new JLabel("Plat"));
        labelTable.put(2, new JLabel("Vallonné"));
        labelTable.put(3, new JLabel("Accidenté"));
        labelTable.put(4, new JLabel("Anarchique"));


        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);

        return slider;
    }


    /**
     * Gets the selected relief type from the slider.
     * @return the selected LandScape type.
     */
    public LandScape getReliefType() {
        int val = this.reliefSlider.getValue();
        return switch (val) {
            case 0 -> LandScape.SUPERFLAT;
            case 1 -> LandScape.FLAT;
            case 2 -> LandScape.HILLY;
            case 3 -> LandScape.JAGGED;
            case 4 -> LandScape.CHAOTIC;
            default -> LandScape.HILLY;
        };
    }
    /**
     * Gets the width of the map from the slider.
     * @return the width of the map.
     */
    public int getWidthMapSlider() {
        return this.widthMapSlider.getValue();
    }
    /**
     * Gets if the flight island option is selected.
     * @return true if flight island is selected, false otherwise.
     */
    public boolean getflightIsland() {
        return this.flightIslandCheckBox.isSelected();
    }

}

