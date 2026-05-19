package view;

import controller.GameController;

import javax.swing.*;

public class GameWindow extends JFrame {
    private GamePanel panel = new GamePanel(this);
    private GameController controller = new GameController(panel,panel.getMenuPanel());
    public GameWindow(){
        setTitle("Barkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
