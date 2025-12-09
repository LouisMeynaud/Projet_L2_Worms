package com.lllmtb.terrain.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TileTest {


    @Test
    public void testGetByFileSymbol_ValidSymbols() {
        assertEquals(Tile.DIRT, Tile.getByFileSymbol('D'));
        assertEquals(Tile.WATER, Tile.getByFileSymbol('W'));
        assertEquals(Tile.AIR, Tile.getByFileSymbol('A'));
        assertEquals(Tile.CAVERN, Tile.getByFileSymbol('C'));
        assertEquals(Tile.GRASS, Tile.getByFileSymbol('G'));
    }
    @Test
    public void testGetByFileSymbol_InvalidSymbol() {
        try {
            Tile.getByFileSymbol('X');
        } catch (IllegalArgumentException e) {
            assertEquals("Attention , la lettre :X n'a pas de Tile correspondante.", e.getMessage());
        }
    }
    @Test
    public void testIsSolid() {
        assertTrue(Tile.DIRT.isSolid());
        assertTrue(Tile.GRASS.isSolid());
        assertFalse(Tile.WATER.isSolid());
        assertFalse(Tile.AIR.isSolid());
        assertFalse(Tile.CAVERN.isSolid());}

    @Test
    public void testGetConsoleSymbol() {
        assertEquals('#', Tile.DIRT.getConsoleSymbol());
        assertEquals('o', Tile.WATER.getConsoleSymbol());
        assertEquals(' ', Tile.AIR.getConsoleSymbol());
        assertEquals('~', Tile.CAVERN.getConsoleSymbol());
        assertEquals('_', Tile.GRASS.getConsoleSymbol()); }

    @Test
    public void testGetFileSymbol() {
        assertEquals('D', Tile.DIRT.getFileSymbol());
        assertEquals('W', Tile.WATER.getFileSymbol());
        assertEquals('A', Tile.AIR.getFileSymbol());
        assertEquals('C', Tile.CAVERN.getFileSymbol());
        assertEquals('G', Tile.GRASS.getFileSymbol());
    }

}

