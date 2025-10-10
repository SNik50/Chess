package main;
//1:48:30
//https://youtu.be/jzCxywhTAUI?si=F-2VC4HkCX0fSAIK

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close window -> shut down program
        window.setResizable(false);

        //Add gamepanel to the window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); //adjusting size

        window.setLocationRelativeTo(null); //show up at the center of the monitor
        window.setVisible(true);

        gp.launchGame();



        }
}