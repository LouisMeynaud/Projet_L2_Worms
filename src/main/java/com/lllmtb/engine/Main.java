package com.lllmtb.engine; 

import com.lllmtb.engine.GameEngine; 
import com.lllmtb.events.EventBus;
import com.lllmtb.ui.ConsoleUI;
import com.lllmtb.ui.UI; 
import com.lllmtb.ui.GraphicsUi.WindowsUi;

public class Main {


     public static void main(String[] args) {
         boolean graphique = true;
        EventBus eventbus = new EventBus();
        UI ui;
        if(graphique){
        ui = new WindowsUi(eventbus);}
        else{
            ui = new ConsoleUI(eventbus);
        }
        GameEngine engine = new GameEngine(eventbus);
        ui.start(); 
    }
}