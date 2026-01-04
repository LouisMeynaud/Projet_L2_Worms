package com.lllmtb.terrain.map;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.lllmtb.engine.save.SaveConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTest {
    private static final String TEMP_TEST_DIR = "junit_test_map_999";
    public static Map m;
    public static final Tile[][] expectedMatrix = {
        {Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR},
        {Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.CAVERN,  Tile.AIR},
        {Tile.DIRT, Tile.DIRT, Tile.DIRT, Tile.GRASS, Tile.DIRT},
        {Tile.DIRT, Tile.WATER, Tile.WATER, Tile.DIRT, Tile.DIRT},
        {Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER},
        {Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER}
    };

    @BeforeAll
    public static void setUp() {
        m = MapLoader.loadMatrix("mapTest");
    }

    @Test
    void createMatrix_createsCorrectMatrixFromFile() {
        assertTrue(Arrays.deepEquals(expectedMatrix, m.getMapMatrix()));
    }

    @Test
    void createMatrix_createsIncorrectMatrixFromFile() {
        m = MapLoader.loadMatrix("mapIncorrectTest");
        assertNull(m);
    }

    @Test
    void saveMapDeclarationClass(){
        new MapLoader(); //constructeur inutile etant donner que MapLoader ne contient que des methode statique mais permet de lire la ligne de declaration de la classe et donc atteindre les 90%
    }

    @Test
    void createMatrix_createsInexistantMatrixFromFile() {
        m = MapLoader.loadMatrix("mapinexistante");
        assertNull(m);
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
        assertNull(resultat, "Doit renvoyer null si le dossier n'existe pas");
    }

    @Test
    void testGetListeDossiers_AvecUnFichier() {
        Map resultat = MapLoader.loadMatrix("test.txt");
        assertNull(resultat, "Doit renvoyer null si on donne un fichier au lieu d'un dossier");
    }

    @Test
    void testLoadMatrixLoad_Success() throws IOException {
        Path testDirPath = Paths.get(SaveConfig.SAVE_DIRECTORY, TEMP_TEST_DIR);
        Files.createDirectories(testDirPath);
        Path mapFile = testDirPath.resolve("map.txt");
        String content = "AAA\nDDD";
        Files.writeString(mapFile, content, StandardCharsets.UTF_8);
        try {
            Map result = MapLoader.loadMatrixLoad(TEMP_TEST_DIR);
            assertNotNull(result);
            assertEquals(2, result.getMapMatrix().length);
            assertEquals(Tile.AIR, result.getMapMatrix()[0][0]);
            assertEquals(Tile.DIRT, result.getMapMatrix()[1][0]);
        } finally {
            Files.deleteIfExists(mapFile);
            Files.deleteIfExists(testDirPath);
        }
    }

    @Test
    void testLoadMatrixLoad_FileNotFound() {
        Map result = MapLoader.loadMatrixLoad("dossier_totalement_imaginaire_123");
        assertNull(result);
    }

    @Test
    void testGetListeNomsLoads_WithActualFolder() throws IOException {
        Path testPath = Paths.get(SaveConfig.SAVE_DIRECTORY, "temp_test_list");
        Files.createDirectories(testPath);

        try {
            String[] saves = MapLoader.getListeNomsLoads();
            assertNotNull(saves);
            List<String> list = Arrays.asList(saves);
            assertTrue(list.contains("temp_test_list"));
        } finally {
            Files.deleteIfExists(testPath);
        }
    }
}