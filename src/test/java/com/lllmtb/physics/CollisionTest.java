package com.lllmtb.physics;

import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import com.lllmtb.model.entity.worm.Worm;

class CollisionTest {

    Map map;
    ArrayList<PhysicsBody> bodies;

    @BeforeEach
    void setUp() {
        map = MapLoader.loadMatrix("MapTestColision"); 
        bodies = new ArrayList<>();
        Collision.init(bodies, map); 
    }

    
    //Crée un physiqueBody (1x1) pour les tests.
    private PhysicsBody createTestBody(double x, double y, double vx, double vy) {
        Position pos = new Position(x, y); 
        Vector vec = new Vector(vx, vy); 
        Worm testEntity = new Worm(pos); 
        
        PhysicsBody body = new PhysicsBody(testEntity, pos, vec, 0.5, 1, 1); 
        bodies.add(body);
        return body;
    }

    // Pour simplifier les nom de teste : Rate = pas de colision detecter
    // ==========================================================
    //             Tests pour haveCollisionX
    

    @Test
    @DisplayName("X: Touche à droite (Coin Bas)")
    void haveCollisionX_QuandToucheDroiteCoinBas() {
        PhysicsBody body = createTestBody(4, 5, 1, 0);
        Position nextPos = new Position(5, 5); 
        assertTrue(Collision.haveCollisionX(body, nextPos)); 
    }

    @Test
    @DisplayName("X: Touche à droite (Coin Haut uniquement)")
    void haveCollisionX_QuandToucheDroiteCoinHautUniquement() {
        PhysicsBody body = createTestBody(4, 4, 1, 0);
        Position nextPos = new Position(5, 4);
        assertTrue(Collision.haveCollisionX(body, nextPos));
    }

    @Test
    @DisplayName("X: Touche à gauche (Coin Bas)")
    void haveCollisionX_QuandToucheGaucheCoinBas() {
        PhysicsBody body = createTestBody(12, 5, -1, 0);
        Position nextPos = new Position(11, 5); 
        assertTrue(Collision.haveCollisionX(body, nextPos)); 
    }

    @Test
    @DisplayName("X: Touche à gauche (Coin Haut uniquement)")
    void haveCollisionX_QuandToucheGaucheCoinHautUniquement() {
        PhysicsBody body = createTestBody(12, 4, -1, 0);
        Position nextPos = new Position(11, 4);
        assertTrue(Collision.haveCollisionX(body, nextPos));
    }

    @Test
    @DisplayName("X: Rate (va à droite dans l'air)")
    void haveCollisionX_QuandRateDroiteDansAir() {
        PhysicsBody body = createTestBody(0, 0, 1, 0);
        Position nextPos = new Position(1, 0); 
        assertFalse(Collision.haveCollisionX(body, nextPos)); 
    }

    @Test
    @DisplayName("X: Rate (va à gauche dans l'air)")
    void haveCollisionX_QuandRateGaucheDansAir() {
        PhysicsBody body = createTestBody(2, 0, -1, 0);
        Position nextPos = new Position(1, 0); 
        assertFalse(Collision.haveCollisionX(body, nextPos)); 
    }

    @Test
    @DisplayName("X: Rate (hors carte à droite)")
    void haveCollisionX_QuandRateHorsCarteDroite() {
        PhysicsBody body = createTestBody(29, 5, 1, 0);
        Position nextPos = new Position(30, 5); 
        assertFalse(Collision.haveCollisionX(body, nextPos));
    }

    @Test
    @DisplayName("X: Rate (hors carte à gauche)")
    void haveCollisionX_QuandRateHorsCarteGauche() {
        PhysicsBody body = createTestBody(0, 5, -1, 0);
        Position nextPos = new Position(-1, 5);
        assertFalse(Collision.haveCollisionX(body, nextPos));
    }

    @Test
    @DisplayName("X: Rate (vitesse X nulle)")
    void haveCollisionX_QuandVitesseXEstNulle() {
        PhysicsBody body = createTestBody(10, 10, 0, 1);
        Position nextPos = new Position(10, 11); 
        assertFalse(Collision.haveCollisionX(body, nextPos)); 
    }

    // ===========================================================
    //              Tests pour haveCollisionY
    

    @Test
    @DisplayName("Y: Touche en haut (Coin Gauche)")
    void haveCollisionY_QuandToucheHautCoinGauche() {
        PhysicsBody body = createTestBody(5, 4, 0, 1);
        Position nextPos = new Position(5, 5); 
        assertTrue(Collision.haveCollisionY(body, nextPos)); 
    }

    @Test
    @DisplayName("Y: Touche en haut (Coin Droit uniquement)")
    void haveCollisionY_QuandToucheHautCoinDroitUniquement() {
        PhysicsBody body = createTestBody(4, 4, 0, 1);
        Position nextPos = new Position(4, 5);
        assertTrue(Collision.haveCollisionY(body, nextPos));
    }

    @Test
    @DisplayName("Y: Touche en bas (Coin Gauche)")
    void haveCollisionY_QuandToucheBasCoinGauche() {
        PhysicsBody body = createTestBody(5, 10, 0, -1);
        Position nextPos = new Position(5, 9); 
        assertTrue(Collision.haveCollisionY(body, nextPos)); 
    }

    @Test
    @DisplayName("Y: Touche en bas (Coin Droit uniquement)")
    void haveCollisionY_QuandToucheBasCoinDroitUniquement() {
        PhysicsBody body = createTestBody(4, 10, 0, -1);
        Position nextPos = new Position(4, 9);
        assertTrue(Collision.haveCollisionY(body, nextPos));
    }

    @Test
    @DisplayName("Y: Rate (va en haut dans l'air)")
    void haveCollisionY_QuandRateHautDansAir() {
        PhysicsBody body = createTestBody(0, 0, 0, 1);
        Position nextPos = new Position(0, 1); 
        assertFalse(Collision.haveCollisionY(body, nextPos)); 
    }

    @Test
    @DisplayName("Y: Rate (va en bas dans l'air)")
    void haveCollisionY_QuandRateBasDansAir() {
        PhysicsBody body = createTestBody(0, 2, 0, -1);
        Position nextPos = new Position(0, 1); 
        assertFalse(Collision.haveCollisionY(body, nextPos)); 
    }

    @Test
    @DisplayName("Y: Rate (hors carte en haut)")
    void haveCollisionY_QuandRateHorsCarteHaut() {
        PhysicsBody body = createTestBody(0, 19, 0, 1);
        Position nextPos = new Position(0, 20);
        assertFalse(Collision.haveCollisionY(body, nextPos));
    }

    @Test
    @DisplayName("Y: Rate (hors carte en bas)")
    void haveCollisionY_QuandRateHorsCarteBas() {
        PhysicsBody body = createTestBody(0, 0, 0, -1);
        Position nextPos = new Position(0, -1);
        assertFalse(Collision.haveCollisionY(body, nextPos));
    }

    @Test
    @DisplayName("Y: Rate (vitesse Y nulle)")
    void haveCollisionY_QuandVitesseYEstNulle() {
        PhysicsBody body = createTestBody(10, 10, 1, 0);
        Position nextPos = new Position(11, 10); 
        assertFalse(Collision.haveCollisionY(body, nextPos)); 
    }


    // =============================================
    //           Tests pour haveCollisionXY

    @Test
    @DisplayName("XY: Touche en Bas-Gauche")
    void haveCollisionXY_QuandToucheBasGauche() {
        PhysicsBody body = createTestBody(12, 10, -1, -1);
        Position nextPos = new Position(11, 9); 
        assertTrue(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Touche en Haut-Gauche")
    void haveCollisionXY_QuandToucheHautGauche() {
        PhysicsBody body = createTestBody(12, 4, -1, 1);
        Position nextPos = new Position(11, 5); 
        assertTrue(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Touche en Bas-Droite")
    void haveCollisionXY_QuandToucheBasDroite() {
        PhysicsBody body = createTestBody(4, 10, 1, -1);
        Position nextPos = new Position(5, 9); 
        assertTrue(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Touche en Haut-Droite")
    void haveCollisionXY_QuandToucheHautDroite() {
        PhysicsBody body = createTestBody(4, 4, 1, 1);
        Position nextPos = new Position(5, 5); 
        assertTrue(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Rate (Haut-Droite dans l'air)")
    void haveCollisionXY_QuandRateHautDroiteDansAir() {
        PhysicsBody body = createTestBody(0, 0, 1, 1);
        Position nextPos = new Position(1, 1); 
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Rate (Bas-Gauche dans l'air)")
    void haveCollisionXY_QuandRateBasGaucheDansAir() {
        PhysicsBody body = createTestBody(2, 2, -1, -1);
        Position nextPos = new Position(1, 1); 
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }
    
    @Test
    @DisplayName("XY: Rate (Haut-Gauche dans l'air)")
    void haveCollisionXY_QuandRateHautGaucheDansAir() {
        PhysicsBody body = createTestBody(2, 2, -1, 1);
        Position nextPos = new Position(1, 3); 
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }
    
    @Test
    @DisplayName("XY: Rate (Bas-Droite dans l'air)")
    void haveCollisionXY_QuandRateBasDroiteDansAir() {
        PhysicsBody body = createTestBody(2, 2, 1, -1);
        Position nextPos = new Position(3, 1); 
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Rate (Haut-Droite hors carte)")
    void haveCollisionXY_QuandRateHautDroiteHorsCarte() {
        PhysicsBody body = createTestBody(29, 19, 1, 1);
        Position nextPos = new Position(30, 20); 
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Rate (Bas-Gauche hors carte)")
    void haveCollisionXY_QuandRateBasGaucheHorsCarte() {
        PhysicsBody body = createTestBody(0, 0, -1, -1);
        Position nextPos = new Position(-1, -1);
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Rate (Haut-Gauche hors carte)")
    void haveCollisionXY_QuandRateHautGaucheHorsCarte() {
        PhysicsBody body = createTestBody(0, 19, -1, 1);
        Position nextPos = new Position(-1, 20);
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }

    @Test
    @DisplayName("XY: Rate (Bas-Droite hors carte)")
    void haveCollisionXY_QuandRateBasDroiteHorsCarte() {
        PhysicsBody body = createTestBody(29, 0, 1, -1);
        Position nextPos = new Position(30, -1);
        assertFalse(Collision.haveCollisionXY(body, nextPos)); 
    }

    // ========================================================
    //        Tests pour haveCollisionMap

    @Test
    @DisplayName("Map: Touche statiquement (Coin Bas-Gauche)")
    void haveCollisionMap_QuandToucheStatiqueBasGauche() {
        PhysicsBody body = createTestBody(5, 5, 0, 0); 
        assertTrue(Collision.haveCollisionMap(body, body.getPos()));
    }

    @Test
    @DisplayName("Map: Touche statiquement (Coin Bas-Droit)")
    void haveCollisionMap_QuandToucheStatiqueBasDroit() {
        PhysicsBody body = createTestBody(10, 5, 0, 0); 
        assertTrue(Collision.haveCollisionMap(body, body.getPos()));
    }

    @Test
    @DisplayName("Map: Touche statiquement (Coin Haut-Gauche)")
    void haveCollisionMap_QuandToucheStatiqueHautGauche() {
        PhysicsBody body = createTestBody(5, 8, 0, 0); 
        assertTrue(Collision.haveCollisionMap(body, body.getPos()));
    }

    @Test
    @DisplayName("Map: Touche statiquement (Coin Haut-Droit)")
    void haveCollisionMap_QuandToucheStatiqueHautDroit() {
        PhysicsBody body = createTestBody(10, 8, 0, 0);
        assertTrue(Collision.haveCollisionMap(body, body.getPos()));
    }

    @Test
    @DisplayName("Map: Rate (statique dans l'air)")
    void haveCollisionMap_QuandRateStatiqueDansAir() {
        PhysicsBody body = createTestBody(0, 0, 0, 0);
        assertFalse(Collision.haveCollisionMap(body, body.getPos())); 
    }

    @Test
    @DisplayName("Map: Hors carte (doit retirer le corps)")
    void haveCollisionMap_QuandHorsCarte_DoitRetirerCorps() {
        PhysicsBody body = createTestBody(-5, -5, 0, 0);
        assertEquals(1, bodies.size());
        assertFalse(Collision.haveCollisionMap(body, body.getPos()));
        assertEquals(0, bodies.size(), "Le corps hors carte aurait dû être retiré");
    }

    @Test
    @DisplayName("Map: Touche Y (vX=0), doit arrêter Y")
    void haveCollisionMap_QuandToucheYSeulement() {
        PhysicsBody body = createTestBody(5, 10, 0, -1);
        assertTrue(Collision.haveCollisionMap(body, new Position(5, 9)));
        assertEquals(0, body.getVector().getY(), "Le vecteur Y aurait dû être mis à 0");
    }



    @Test
    @DisplayName("Map: Touche XY, doit arrêter X et Y")
    void haveCollisionMap_QuandToucheXYSeulement() {
        PhysicsBody body = createTestBody(0, 0, 1, -1);
        Position nextPos = new Position(4, 9); 
        
        assertTrue(Collision.haveCollisionMap(body, nextPos));
        assertEquals(0, body.getVector().getX(), "Le vecteur X aurait dû être mis à 0");
        assertEquals(0, body.getVector().getY(), "Le vecteur Y aurait dû être mis à 0");
    }

    @Test
    @DisplayName("Map: Touche X (vY=0), doit arrêter X")
    void haveCollisionMap_QuandToucheXSeulement() {
        PhysicsBody body = createTestBody(4, 5, 1, 0);
        assertTrue(Collision.haveCollisionMap(body, new Position(5, 5)));
        assertEquals(0, body.getVector().getX(), "Le vecteur X aurait dû être mis à 0");
    }

    @Test
    @DisplayName("Map: Rate (dans l'air), ne doit rien faire")
    void haveCollisionMap_QuandRateDansAir() {
        PhysicsBody body = createTestBody(0, 0, 1, 1);
        assertFalse(Collision.haveCollisionMap(body, new Position(1, 1)));
    }
    


    // ==========================================================================
    //                    Test de Classe
   
    @Test
    @DisplayName("test: Constructeur (pour Jacoco)")
    void testConstructeurPourCouverture() {
        new Collision();
    }
}