package view;

import javax.swing.*;

public class GameWindow extends JFrame {
    public GameWindow(){
        setTitle("Barkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
