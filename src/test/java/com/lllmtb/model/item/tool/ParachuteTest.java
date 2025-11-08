package com.lllmtb.model.item.tool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParachuteTest {

        public static Parachute p;

        @BeforeEach
        public void setUp() {
            p = new Parachute();
        }

        @Test
        public void testConstructeur() {
            assertEquals(1, p.getTest());
        }
}
