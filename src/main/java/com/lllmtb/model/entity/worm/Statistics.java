package com.lllmtb.model.entity.worm;

public class Statistics {
    //Champs
    private static int maxHp = 100;
    private int hp;
    private int speed;
    private int strength;

    //Constructor
    public Statistics(int hp, int speed, int strength) {
        this.hp = hp;
        this.speed = speed;
        this.strength = strength;
    }

    //Getter
    public int getHp() {
        return this.hp;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getStrength() {
        return this.strength;
    }

    public static int getMaxHp() {
        return maxHp;
    }


    //Setter
    public void setHp(int hp) {
        if (hp >= 0) {
            this.hp = hp;
        } else {
            this.hp = 0;
        }
    }

    public void setSpeed(int speed) {
        if (speed >= 0) {
            this.speed = speed;
        }

    }

    public void setStrength(int strength) {
        if (strength >= 0) {
            this.strength = strength;
        }

    }

    //Method
    public boolean isAlive() {
        return this.hp > 0;
    }

    public void damageTake(int damage){
        this.setHp(this.hp - damage);
    }

}
