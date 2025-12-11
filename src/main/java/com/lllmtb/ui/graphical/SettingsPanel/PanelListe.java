package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;


public class PanelListe extends JPanel {

    private static final String NOM_FICHIER_INFO = "info.txt";
    private static final String NOM_FICHIER_IMAGE = "image.png";
    private final JList<InfoLigne> maListe;

    private static class InfoLigne {
        String titre;
        String description;
        ImageIcon image;
        File fichierMap;

        public InfoLigne(String titre, String info, ImageIcon image, File fichierMap) {
            this.titre = titre;
            this.description = info;
            this.image = image;
            this.fichierMap = fichierMap;
        }

        @Override
        public String toString() { // si quelque chose se passe mal dans le chargement du fichier on met ceci
            return this.titre;
        }

    }

    /**
     * Constructor of the panel that displays the list of maps available in the maps folder.
     * @param tableauDeDossiers an array of files representing the map directories.
     */
    public PanelListe(File[] tableauDeDossiers) {
        setBackground(Color.DARK_GRAY);
        this.setLayout(new BorderLayout());

        ArrayList<InfoLigne> donneeMap = new ArrayList<>();

        if (tableauDeDossiers != null) {
            for (File dossier : tableauDeDossiers) {
                if (dossier.isDirectory()) {
                    InfoLigne info = loadInfoDossier(dossier);
                    donneeMap.add(info);
                }
            }
        }

        //JList ne prend que des tab , on convertit donneeMap en tab
        InfoLigne[] tableauDeDonnees = new InfoLigne[donneeMap.size()];
        for (int i = 0; i < donneeMap.size(); i++) {
            tableauDeDonnees[i] = donneeMap.get(i);
        }
        this.maListe = new JList<>(tableauDeDonnees);
        this.maListe.setOpaque(false);

        maListe.setCellRenderer(new ListRenderer());
        JScrollPane list = new JScrollPane(maListe);
        list.setOpaque(false);

        this.add(list, BorderLayout.CENTER);

    }

    /**
     * Gets the selected map file.
     * @return the selected map file, or null if none is selected.
     */
    public File getFichierSelectionne() {
        InfoLigne selection = maListe.getSelectedValue();
        if (selection != null) {
            return selection.fichierMap;
        }
        return null;
    }


    private InfoLigne loadInfoDossier(File dossier) {
        String titre = dossier.getName();
        String description = "";
        ImageIcon image = null;

        try {
            File fichierTexte = new File(dossier, NOM_FICHIER_INFO);
            if (fichierTexte.exists()) {
                FileReader fileReader = new FileReader(fichierTexte);
                BufferedReader buffereReader = new BufferedReader(fileReader);


                String ligne1 = buffereReader.readLine();
                if (ligne1 != null) {
                    description = ligne1;
                }

                buffereReader.close();
                fileReader.close();

            } else {
                titre = dossier.getName();
                description = "Pas de fichier info";
            }
            File fichierImage = new File(dossier, NOM_FICHIER_IMAGE); //fait le chemin vers le fichier
            if (fichierImage.exists()) {
                Image imgOriginale = ImageIO.read(fichierImage); //charge l'image
                Image imgRedimensionnee = imgOriginale.getScaledInstance(100, 100, Image.SCALE_FAST);
                image = new ImageIcon(imgRedimensionnee);
            }
        } catch (Exception e) {
            System.out.println("Probleme avec le dossier:" + dossier.getName());
            e.printStackTrace();
            titre = "erreur de lecture";

        }
        return new InfoLigne(titre, description, image, dossier);
    }


    private static class ListRenderer extends JPanel implements ListCellRenderer<InfoLigne> {

        JLabel labelImage = new JLabel();
        JLabel labelTitre = new JLabel();
        JLabel labelDescription = new JLabel();
        JPanel panelTextes = new JPanel();

        public ListRenderer() {
            this.setLayout(new BorderLayout(10, 10));
            this.setOpaque(true);

            labelImage.setPreferredSize(new Dimension(100, 100));
            labelImage.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(labelImage, BorderLayout.WEST);

            panelTextes.setLayout(new GridLayout(2, 1));
            panelTextes.setOpaque(false);

            this.labelTitre.setFont(new Font("Arial", Font.BOLD, 24));
            labelDescription.setFont(new Font("Arial", Font.PLAIN, 16));

            this.panelTextes.add(labelTitre);
            this.panelTextes.add(labelDescription);

            this.add(panelTextes, BorderLayout.CENTER);
        }

        @Override

        public Component getListCellRendererComponent(JList<? extends InfoLigne> list, InfoLigne valeur, int index, boolean isSelected,
                                                      boolean cellHasFocus) {


            labelTitre.setText(valeur.titre);
            labelDescription.setText(valeur.description);
            labelImage.setIcon(valeur.image);
            setBackground(Color.GRAY);

            if (isSelected) {
                setOpaque(true);
            } else {
                setOpaque(false);
            }

            return this;
        }
    }
}



