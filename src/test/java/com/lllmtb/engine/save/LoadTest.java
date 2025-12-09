package com.lllmtb.engine.save;

import java.io.FileNotFoundException;

public class LoadTest{
    public static void main(String[] args) throws FileNotFoundException {
        GameSave gameSave = Load.loadGameSave("null");
        Load.reforgeGameSave(gameSave);
        gameSave.toString();
    }
}
