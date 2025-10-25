package com.lllmtb.physics;

public class Position {
    private double x, y;
    
    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getPosx(){
        return this.x;
    }

    public double getPosy(){
        return this.y;
    }
    
    public void setPos(double x, double y){
        if(isInScreen(x,y)) {
            this.x = x;
            this.y = y;
        }
    }
}
