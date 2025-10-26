package com.lllmtb.physics;
import com.lllmtb.terrain.Map;

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
    
    public void setPos(Map m,double x, double y){
        if(m.inMap(x,y)) {
            this.x = x;
            this.y = y;
        }
    }
}
