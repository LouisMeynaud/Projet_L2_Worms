package com.lllmtb.terrain;

public interface Imap {
     public static enum Bloc {
        DIRT, WATER, AIR
    };

    public int getWidth(); 
    public int getHeight();
    public Bloc[][] getMapMatrix();
    public void setMap(int i,int j,Bloc bloc);
    public boolean isBloc(Bloc bloc,int x,int y);
    public boolean inMap(double x,double y);
    public String toString();
}
