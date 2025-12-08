package com.lllmtb.engine.save;

import com.lllmtb.engine.Save.GameSave;
import com.lllmtb.engine.Save.Load;

import java.io.FileNotFoundException;

public class LoadTest{
    public static void main(String[] args) throws FileNotFoundException {
        GameSave gameSave = Load.loadGameSave("null");
    }
}
