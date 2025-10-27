package com.lllmtb.terrain;

public class Testtempo {
    public static void main(String[] args){
        Map m = MapLoader.createMatrix2("mapTest");
        System.out.println(m.getHeight());
        System.out.println(m.getWidth());
        System.out.println(m.toString());

    }
}
