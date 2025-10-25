package com.lllmtb.terrain;

public class Testtempo {
    public void main(String[] args){
    Map m = MapLoader.createMatrix("mapTest.txt");
    System.out.println(m.getHeight());
    System.out.println(m.getWidth());
    System.out.println(m);
    }
}
