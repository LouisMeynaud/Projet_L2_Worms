package com.lllmtb.terrain;

import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class GenerateMap {
    

    public static String saveFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("entré le nom du fichier d'enregistrement");

        boolean existe = false;
        String files;
        do {
            files = scanner.nextLine() + ".txt";

            String dossier = "src/main/java/com/lllmtb/terrain/MapFile/";

            File folder = new File(dossier);
            File[] fichiers = folder.listFiles();

            if (fichiers != null) {
                for (File f : fichiers) {
                    if (f.getName().equals(files)) {
                        System.out
                                .println("Fichier déjà existant. Entrez O pour l’écraser, N pour en créer un nouveau.");
                        if (scanner.nextLine().equals("N")) {
                            existe = true;
                            System.out.println("merci d'entré un nouveaux nom de fichier");
                        }
                    }

                }
            }
        } while (existe);
        scanner.close();
        return files;

    }

    public static int[] ligne(int width, int height, int pentemax, int longeurmax) {
        int hauteur = height / 2;
        int x = 0;
        int pente = 0;
        int longeur = 0;
        int[] ligne = new int[width];
        Random random = new Random();
        while (x < width) {
            do{pente = random.nextInt(pentemax - (-pentemax) + 1) + (-pentemax);
            longeur = random.nextInt(longeurmax + 1);}
            while(hauteur + pente * longeur >= height || hauteur + pente * longeur < 0);
            for (int i = 0; i < longeur && x < width; i++) {
                ligne[x] = hauteur;
                hauteur += pente;
                x++;
            }
        }
        return ligne;
    }

    public void remplissage(Map map,int[] lignehaute,int[] ligne_bassse){
        //
    }


}