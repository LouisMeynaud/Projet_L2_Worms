package com.lllmtb.ui.GraphicsUi;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



import com.lllmtb.engine.GameSettings;
import com.lllmtb.ui.*;;

public class WindowsUi extends  UI{
    
    public JFrame windows;
    
    public WindowsUi(){
        windows = new JFrame();
        windows.setTitle("Worms-MI4");
        //Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        //windows.setSize(dimension.width, dimension.height);
        windows.setSize(500,500);
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //windows.setLocation(0,0);
        homeScreen();
        windows.setVisible(true);
    }

    public void homeScreen(){
        //creation des bouton et du panneau
        JButton startButton = new JButton("start");
        JButton SettingButton = new JButton("game Setting");
        JButton CloseButton = new JButton("close");
        JPanel panel = new JPanel();
        //ajout des bouton au panneau
        panel.add(startButton);
        panel.add(SettingButton);
        panel.add(CloseButton);
        //ajout du panneau a la fenetre
        this.windows.add(panel);

    }
    
    
    
    
    public GameSettings promptSettings(){
        return null;
    }

    public boolean readyToPlay(){
        return false;
    }
    public void close(){
    }
}   
