package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.lllmtb.engine.logger.Logger;


public class PanelListe extends JPanel {

    private static final String NOM_FICHIER_INFO = "info.txt";
    private static final String NOM_FICHIER_IMAGE = "image.png";
    private final JList<InfoLigne> maListe;

    private static class InfoLigne {
        final String titre;
        final String description;
        final ImageIcon image;
        final String nomMap;

        public InfoLigne(String titre, String info, ImageIcon image, String nomMap) {
            this.titre = titre;
            this.description = info;
            this.image = image;
            this.nomMap = nomMap;
        }

        @Override
        public String toString() { // si quelque chose se passe mal dans le chargement du fichier on met ceci
            return this.titre;
        }

    }

    /**
     * Constructor of the panel that displays the list of maps available in the maps folder.
     * @param nomsMaps an array of Strings representing the map directory names.
     * @param nomDossier the name of the dossier
     * @param isExternal true if the search file is not in ressource , false else.
     */
    public PanelListe(String[] nomsMaps, String nomDossier, boolean isExternal) {
        setBackground(Color.DARK_GRAY);
        this.setLayout(new BorderLayout());

        ArrayList<InfoLigne> donneeMap = new ArrayList<>();

        if (nomsMaps != null) {
            for (String nom : nomsMaps) {
                InfoLigne info = loadInfoRessource(nom, nomDossier, isExternal);
                donneeMap.add(info);
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
     * Gets the selected map name.
     * @return the selected map name, or null if none is selected.
     */
    public String getSelectedMapName() {
        InfoLigne selection = maListe.getSelectedValue();
        if (selection != null) {
            return selection.nomMap;
        }
        return null;
    }


    private InfoLigne loadInfoRessource(String nomDossier, String mainDossier, boolean isExternal) {
        String titre = nomDossier;
        String description = "";
        ImageIcon image = null;

        try {
            if (isExternal) {
                File folder = new File(mainDossier + nomDossier);

                File infoFile = new File(folder, NOM_FICHIER_INFO);
                if (infoFile.exists()) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(infoFile));
                    description = bufferedReader.readLine();
                    bufferedReader.close();
                }
                File imgFile = new File(folder, NOM_FICHIER_IMAGE);
                if (imgFile.exists()) {
                    Image img = ImageIO.read(imgFile);
                    image = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                }
            } else {

                String cheminInfo = mainDossier + nomDossier + "/" + NOM_FICHIER_INFO;
                InputStream isText = getClass().getResourceAsStream(cheminInfo);

                if (isText != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(isText));
                    String ligne1 = reader.readLine();
                    if (ligne1 != null) {
                        description = ligne1;
                    }
                    reader.close();
                } else {
                    description = "Pas de fichier info";
                }

            String cheminImage = mainDossier + nomDossier + "/" + NOM_FICHIER_IMAGE;
            URL imageUrl = getClass().getResource(cheminImage);

            if (imageUrl != null) {
                Image imgOriginale = ImageIO.read(imageUrl);
                Image imgRedimensionnee = imgOriginale.getScaledInstance(100, 100, Image.SCALE_FAST);
                image = new ImageIcon(imgRedimensionnee);
            }

                }
        } catch (Exception e) {
            Logger.logger.error("Problem loading map: {}", nomDossier, e);
            titre = "erreur de lecture";
        }
        return new InfoLigne(titre, description, image, nomDossier);
    }


    private static class ListRenderer extends JPanel implements ListCellRenderer<InfoLigne> {

        final JLabel labelImage = new JLabel();
        final JLabel labelTitre = new JLabel();
        final JLabel labelDescription = new JLabel();
        final JPanel panelTextes = new JPanel();

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

            setOpaque(isSelected);

            return this;
        }
    }
}



