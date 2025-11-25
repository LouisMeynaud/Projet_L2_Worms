package com.lllmtb.ui.graphical.SettingsPanel;

import java.awt.BorderLayout;
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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class PanelListeMap extends JPanel{

    private static final String NOM_FICHIER_INFO = "info.txt";
    private static final String NOM_FICHIER_IMAGE = "image.png";
    private JList<InfoLigne> maListe;

    private static class InfoLigne{
        String titre;
        String description;
        ImageIcon image;
        File fichierMap;

        public InfoLigne(String titre, String info, ImageIcon image, File fichierMap) {
            this.titre = titre;
            this.description = info;
            this.image = image;
            this.fichierMap = fichierMap; 
        }// On le sauvegarde
        
        @Override
        public String toString(){ // si quelque chose se passe mal dans le chargement du fichier on met ceci
            return this.titre;
        }
    
}
    public PanelListeMap(File[] tableauDeDossiers){
        this.setLayout(new BorderLayout());

        ArrayList<InfoLigne> donneeMap = new ArrayList<>();

        if(tableauDeDossiers != null){
            for(File dossier:tableauDeDossiers){
                if(dossier.isDirectory()){
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

        maListe.setCellRenderer(new ListRenderer());
        JScrollPane list = new JScrollPane(maListe);
        list.getVerticalScrollBar().setUnitIncrement(20);

        this.add(list,BorderLayout.CENTER);

        //ecoute le clique de la liste
        maListe.addListSelectionListener(e -> {
                InfoLigne elementSelectionne = maListe.getSelectedValue();
                if (elementSelectionne != null) {
                    System.out.println("vous avez cliquer sur:" + elementSelectionne.titre);
                    System.out.println("le fichier correspondant est:" + elementSelectionne.fichierMap.getAbsolutePath());
                    
                   
                }
            
        });
       
    }

    
    public File getFichierSelectionne() {
        InfoLigne selection = maListe.getSelectedValue();
        if (selection != null) {
            return selection.fichierMap;
        }
        return null; 
    }
    

    private InfoLigne loadInfoDossier(File dossier){
        String titre = dossier.getName();
        String description ="";
        ImageIcon image = null;

        try {
            File fichierTexte = new File(dossier,NOM_FICHIER_INFO);
            if(fichierTexte.exists()){
                FileReader fileReader = new FileReader(fichierTexte);
                BufferedReader buffereReader = new BufferedReader(fileReader);


                String ligne1 = buffereReader.readLine();
                if(ligne1 != null){
                    description = ligne1;
                }

                buffereReader.close();
                fileReader.close();

            }else{
                titre = dossier.getName();
                description = "Pas de fichier info";
            }
            File fichierImage = new File(dossier,NOM_FICHIER_IMAGE);//fait le cehmin vers le fichier 
            if(fichierImage.exists()){
                Image imgOriginale = ImageIO.read(fichierImage);//charge l'image
                Image imgRedimensionnee = imgOriginale.getScaledInstance(100, 100, Image.SCALE_FAST);
                image = new ImageIcon(imgRedimensionnee);
            }
            }catch (Exception e) {
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
        public Component getListCellRendererComponent(JList<? extends InfoLigne> list, InfoLigne valeur, int index,boolean isSelected, boolean cellHasFocus) {
            ///fdsssssssssssssssssssss
            labelTitre.setText(valeur.titre);
            labelDescription.setText(valeur.description);
            labelImage.setIcon(valeur.image);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }
}
    



