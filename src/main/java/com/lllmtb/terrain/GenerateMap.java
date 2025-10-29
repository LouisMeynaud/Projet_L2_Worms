package com.lllmtb.terrain;

import com.lllmtb.physics.*;

import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

// note tempo 
//pente + forte = plus chaotique
//longueur plus haute = terrain plus valoner

//verification a faire lors de la creation de la map ; espace de terrain remplie ; meme quantité de terre des deux coter , crée un moyen de faire des petite 'ile' sans que sa soit le bordel
public class GenerateMap {
    public static Map createMatrix(int width, int height, int pentemax, int longeurmax, int heightWater) {
        Map map = new Map(height, width, saveFile());

        int[] ligneHaute = ligne(width, height, pentemax, longeurmax);
        int[] ligneBasse = ligne(width, height, pentemax, longeurmax);

        for (int i = 0; i < width; i++) {
            if (ligneBasse[i] > ligneHaute[i]) {
                int temp = ligneBasse[i];
                ligneBasse[i] = ligneHaute[i];
                ligneHaute[i] = temp;
            }
        }
        System.out.println(Arrays.toString(ligneHaute));
        System.out.println(Arrays.toString(ligneBasse));

        remplissage(map, ligneHaute, ligneBasse);
        System.out.println("test");
        for (int i = 0; i <= heightWater; i++) {
            for (int j = 0; j < width; j++) {
                Position position = new Position(j, i);
                if (map.isTileAt(position, Tile.AIR)) {
                    map.setTile(position, Tile.WATER);
                }
            }
        }
        return map;

    }

    public static String saveFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("entré le nom du fichier d'enregistrement");

        boolean existe = false;
        String files;
        do {
            existe = false;
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
        int hauteur = height / 5;
        int x = 0;
        int pente = 0;
        int longeur = 0;
        int[] ligne = new int[width];
        Random random = new Random();
        while (x < width) {
            do {
                if (random.nextInt(101) < 40) { // 40% de chance d'avoir un plateau , parametre modifiable par la suite
                    pente = 0;
                } else {
                    pente = random.nextInt(pentemax - (-pentemax) + 1) + (-pentemax);
                }
                longeur = random.nextInt(longeurmax + 1);
            } while (hauteur + pente * longeur >= height || hauteur + pente * longeur < 0);
            for (int i = 0; i < longeur && x < width; i++) {
                ligne[x] = hauteur;
                hauteur += pente;
                x++;
            }
        }
        return ligne;
    }

    public static void remplissage(Map map, int[] ligneHaute, int[] ligneBassse) {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);
                if (i > ligneHaute[j] || i < ligneBassse[j]) {
                    map.setTile(position, Tile.AIR);
                } else {
                    map.setTile(position, Tile.DIRT);
                }
            }
        }
    }

}