package controller;

import view.GameMenu;
import view.GamePanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    private GameMenu menu;
    private GamePanel panel;
    public InputHandler(GameMenu menu, GamePanel panel){
        this.menu = menu;
        this.panel = panel;
    }
    @Override
    public void keyPressed(KeyEvent e){
        int tecla = e.getKeyCode();
        String estadoActual = panel.getGameState();

        //eventos cuando se esta en estado de menu
        if (estadoActual.equals("menu")) {
            if(tecla == KeyEvent.VK_1){
                menu.selectLucas();
            } else if(tecla == KeyEvent.VK_2){
                menu.selectSalchicha();
            } else if (tecla == KeyEvent.VK_ENTER) {
                menu.startGame(); // Esto cambiará el estado a "instructions" en el update de GamePanel
            } else if (tecla == KeyEvent.VK_ESCAPE) {
                menu.close();
                System.exit(0);
            }
        } else if (estadoActual.equals("instructions")) {
            //eventos cuando se esta en la ventana de instrucciones
            if (tecla == KeyEvent.VK_ENTER) {
                //dado enter se pasa al juego
                panel.setGameState("game");
                //se para el loop del soundtrack y se vuelve a iniciar
                SoundManager.stopLoop();
                SoundManager.playLoop("soundtrack.wav");
            }
        }
    }
}
