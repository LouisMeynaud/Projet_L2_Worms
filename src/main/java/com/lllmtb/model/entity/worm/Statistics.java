package com.lllmtb.model.entity.worm;

public class Statistics {
    //Champs
    private static double maxHp = 100;
    private double hp;
    private int speed;
    private int strength;

    //Constructor
    public Statistics(double hp, int speed, int strength) {
        this.hp = hp;
        this.speed = speed;
        this.strength = strength;
    }

    //Getter
    public double getHp() {
        return this.hp;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getStrength() {
        return this.strength;
    }

    public static double getMaxHp() {
        return maxHp;
    }

    public static void setMaxHp(double maxHp) {
        Statistics.maxHp = maxHp;
    }


    //Setter
    public void setHp(double hp) {
        if (hp >= 0 && hp <= maxHp) {
            this.hp = hp;
        } else if (hp < 0) {
            this.hp = 0;
        } else if (hp > maxHp) {
            this.hp = maxHp;
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

    public void damageTake(double damage) {
        this.setHp(this.hp - damage);
    }

}
