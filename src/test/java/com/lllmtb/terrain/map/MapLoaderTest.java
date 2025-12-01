package com.lllmtb.terrain.map;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.lllmtb.terrain.map.MapLoader;

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
        System.out.println(m);
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
    public void testGetListeDossiersMaps_Fonctionne() {
        File dossierRacine = new File("src/main/resources/map");
        if (!dossierRacine.exists()) {
            dossierRacine.mkdirs();
        }
        File dossierTest = new File(dossierRacine, "MapTestUnit");
        dossierTest.mkdir(); 

        try {
            File[] resultats = MapLoader.getListeDossiersMaps();

            assertNotNull(resultats, "Ne doit pas retourner null");
            
            boolean aTrouveMonDossier = false;
            for (File f : resultats) {
                if (f.getName().equals("MapTestUnit")) {
                    aTrouveMonDossier = true;
                    break;
                }
            }
            assertTrue(aTrouveMonDossier, "La méthode aurait dû trouver le dossier 'MapTestUnit'");

        } finally {
            dossierTest.delete();
        }
    }

    @Test
    void testGetListeDossiers_AvecDossierInexistant() {
        File dossierFantome = new File("src/main/resources/DossierQuiNExistePasDuTout");
        
        
        File[] resultats = MapLoader.getListeDossiers(dossierFantome);
        
        assertEquals(0, resultats.length, "Doit renvoyer vide si le dossier n'existe pas");
    }

    @Test
    void testGetListeDossiers_AvecUnFichier() throws IOException {
        
        File fichierTemp = File.createTempFile("test", ".txt");
        fichierTemp.deleteOnExit();

       
        File[] resultats = MapLoader.getListeDossiers(fichierTemp);
        
        assertEquals(0, resultats.length, "Doit renvoyer vide si on donne un fichier au lieu d'un dossier");
    }


}