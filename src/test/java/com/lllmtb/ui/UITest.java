package com.lllmtb.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UITest {
        @Test
        public void testConstructor() {
            UI ui = new TerminalUI();
            assertEquals(null,ui.world);
        }
        @Test
        public void testSetWorld() {
            UI ui = new TerminalUI();
            ui.setWorld(null);
            assertEquals(null,ui.world);
        }
}
