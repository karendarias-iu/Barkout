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
        if(tecla==KeyEvent.VK_1){
            menu.selectLucas();
        }else if(tecla==KeyEvent.VK_2){
            menu.selectSalchicha();
        } else if (tecla==KeyEvent.VK_ENTER) {
            menu.startGame();
        } else if (tecla==KeyEvent.VK_ESCAPE) {
            if(panel.getGameState().equals("menu")){
                menu.close();
                System.exit(0);
            }
        }
    }
}
