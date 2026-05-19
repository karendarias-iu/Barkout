package view;

import model.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameMenu {
    private int posTitleY = 50;
    private int posImagesY = 80;
    private int posLucasImageX1 = 125;
    private int posSalchichaImageX2 = 50 + posLucasImageX1 + 250;
    private BufferedImage imageMenuLucas;
    private BufferedImage imageMenuSalchicha;
    private String character = "lucas";

    private boolean activeMenu = true;
    private boolean startButtonSelected = false;
    private int posSelectionSquareX = posLucasImageX1 - 10;
    private int posSelectionSquareY = posImagesY - 10;
    private Color colorSquare = Color.lightGray;

    public GameMenu() {
        imageMenuLucas = Entity.uploadImage("perro/lucas/menu.png");
        imageMenuSalchicha = Entity.uploadImage("perro/salchicha/menu.png");
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Courier New", Font.BOLD, 50));
        dibujarCentrado(g2d, "Seleccione el personaje", 800, posTitleY);
        g2d.fillRect(posSelectionSquareX, posSelectionSquareY, 270, 270);
        if (imageMenuLucas != null && imageMenuSalchicha != null) {
            g2d.drawImage(imageMenuLucas, posLucasImageX1, posImagesY, null);
            g2d.drawImage(imageMenuSalchicha, posSalchichaImageX2, posImagesY, null);
        }
        g2d.setFont(new Font("Courier New", Font.BOLD, 30));
        g2d.drawString("tecla (1)", posLucasImageX1 + 30, posImagesY + 285);
        g2d.drawString("tecla (2)", posSalchichaImageX2 + 30, posImagesY + 285);
        g2d.fillRect(150, 400, 500, 60);
        g2d.setColor(Color.gray);
        g2d.fillRect(155, 405, 490, 50);
        g2d.setColor(Color.WHITE);
        dibujarCentrado(g2d, "Iniciar", 800, 440);
        g2d.fillRect(150, 500, 500, 60);
        g2d.setColor(Color.gray);
        g2d.fillRect(155, 505, 490, 50);
        g2d.setColor(Color.WHITE);
        dibujarCentrado(g2d, "Salir", 800, 540);
        g2d.setFont(new Font("Courier New", Font.BOLD, 20));
        g2d.drawString("(Enter)", 670, 440);
        g2d.drawString("(Esc)", 670, 540);
    }

    private void dibujarCentrado(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        FontMetrics fm = g2d.getFontMetrics();
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        g2d.drawString(stringText, posX, posY);
    }

    public void selectLucas() {
        posSelectionSquareX = posLucasImageX1 - 10;
        character = "lucas";
    }

    public void selectSalchicha() {
        posSelectionSquareX = posSalchichaImageX2 - 10;
        character = "salchicha";
    }

    public void close() {
        activeMenu = false;
    }

    public void startGame() {
        startButtonSelected = true;
    }

    public boolean isStartButtonSelected() {
        return startButtonSelected;
    }

    public boolean isActiveMenu() {
        return activeMenu;
    }

    public String getCharacter() {
        return character;
    }
}
