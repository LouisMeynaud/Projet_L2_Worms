package com.lllmtb.engine; 

import com.lllmtb.engine.GameEngine; 
import com.lllmtb.engine.ScreenLoop;
import com.lllmtb.events.EventBus;
import com.lllmtb.ui.ConsoleUI;
import com.lllmtb.ui.UI; 
import com.lllmtb.ui.GraphicsUi.WindowsUi;

public class Main {


     public static void main(String[] args) {
         boolean graphique = false;
        EventBus eventbus = new EventBus();
        UI ui;
        if(graphique){
        ui = new WindowsUi(eventbus);}
        else{
            ui = new ConsoleUI(eventbus);
        }
        GameEngine engine = new GameEngine(eventbus);
        ScreenLoop screenLoop = new ScreenLoop(eventbus); 
        Thread gameThread = new Thread(screenLoop); //permet d'excecuter la methode run de screenloop
        gameThread.start();
        ui.start(); 
    }
}