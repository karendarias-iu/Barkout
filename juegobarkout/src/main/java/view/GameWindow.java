package view;

import javax.swing.*;

public class GameWindow extends JFrame {
    //creacion del panel del juego
    private GamePanel panel = new GamePanel(this);

    //Constructor por defecto
    public GameWindow() {
        //Configuraciones de la ventana
        setTitle("Barkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //agregar panel a la ventana para mostrar
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
