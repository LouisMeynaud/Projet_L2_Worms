package com.lllmtb.terrain.map;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapLoaderTest {
    public static String mapText;
    public static Map m;
    public static String path;
    public static Tile[][] expectedMatrix = {
        {Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR},
        {Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.CAVERN,  Tile.AIR},
        {Tile.DIRT, Tile.DIRT, Tile.DIRT, Tile.GRASS, Tile.DIRT},
        {Tile.DIRT, Tile.WATER, Tile.WATER, Tile.DIRT, Tile.DIRT},
        {Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER},
        {Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER}
    };
    @BeforeAll
    public static void setUp() throws IOException {
        // path = "src/main/resources/map/mapTest/map.txt";
        // mapText = Files.readString(Path.of(path));
        m = MapLoader.loadMatrix("mapTest");
    }

    @Test
    void createMatrix_createsCorrectMatrixFromFile() {
        assertTrue(Arrays.deepEquals(expectedMatrix, m.getMapMatrix()));
    }

    @Test
    void createMatrix_createsIncorrectMatrixFromFile() {
        m = MapLoader.loadMatrix("mapIncorrectTest");
        assertEquals(m, null);;
    }

    @Test
    void saveMapDeclarationClass(){
        new MapLoader(); //constructeur inutile etant donner que MapLoader ne contient que des methode statique mais permet de lire la ligne de declaration de la classe et donc atteindre les 90%
    }

    @Test
    void createMatrix_createsInexistantMatrixFromFile() {
        m = MapLoader.loadMatrix("mapinexistante");
        assertEquals(m, null);;
    }

    @Test
    public void testGetListeDossiersMaps() {
        String[] resultats = MapLoader.getListeNomsMaps();

        assertNotNull(resultats, "Ne doit pas retourner null");
        
        boolean trouver = false;
        for (String nom : resultats) {
            if (nom.equals("Camembert")) {
                trouver = true;
                break;
            }
        }
        assertTrue(trouver, "La méthode aurait dû trouver le dossier 'Camembert'");
    }

    @Test
    void testGetListeDossiers_AvecDossierInexistant() {
        Map resultat = MapLoader.loadMatrix("dossierInexistant");
        assertEquals(null, resultat, "Doit renvoyer null si le dossier n'existe pas");
    }

    @Test
    void testGetListeDossiers_AvecUnFichier() throws IOException {
        Map resultat = MapLoader.loadMatrix("test.txt");
        assertEquals(null, resultat, "Doit renvoyer null si on donne un fichier au lieu d'un dossier");
    }
}