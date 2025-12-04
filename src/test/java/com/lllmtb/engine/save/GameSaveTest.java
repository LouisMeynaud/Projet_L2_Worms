package com.lllmtb.engine.save;

import com.lllmtb.engine.Save.GameSave;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameSaveTest {
    @Test
    void constructorTest(){
        GameSave saveGame = new GameSave();
        assertNotNull(saveGame);
    }

}
