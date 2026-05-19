package view;

import model.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameIntro {
    private int positionTitleY = 300;
    private String titleName = "Barkout";
    private int fontSize = 95;
    private boolean introEnded = false;
    private long initialTime;
    private int animationPhase = 0;
    private int posYAuthorsText = 600;
    private int posLogo = posYAuthorsText + 360;
    private BufferedImage logo;

    public GameIntro() {
        initialTime = System.nanoTime();
        logo = Entity.uploadImage("logo_uam.png");
    }


    public void update() {
        long actualTime = System.nanoTime();
        long timePhase = (actualTime - initialTime) / 1_000_000_000L;

        if (timePhase == 1) {
            animationPhase = 1;
        }

        switch (animationPhase) {
            case 1:
                if (timePhase >= 5) {
                    animationPhase = 2;
                }
                break;
            case 2:
                if (fontSize >= 60) {
                    fontSize--;
                }

                if (positionTitleY > 0) {
                    positionTitleY -= 2;
                } else {
                    animationPhase = 3;
                }
                break;
            case 3:
                if (posLogo >= 0) {
                    posYAuthorsText -= 2;
                    posLogo = posYAuthorsText + 360;
                } else {
                    introEnded = true;
                }
                break;
        }
    }

    public boolean isIntroEnded() {
        return introEnded;
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Courier New", Font.BOLD, fontSize));

        if (animationPhase >= 1) {
            if (animationPhase < 3) {
                dibujarCentrado(g2d, titleName, 800, positionTitleY);
            } else if (animationPhase == 3) {
                dibujarCentrado(g2d, "Autores", 800, posYAuthorsText);
                g2d.setFont(new Font("Courier New", Font.BOLD, fontSize - 20));
                dibujarCentrado(g2d, "Karen Daiana Arias Cardona", 800, posYAuthorsText + 60);
                dibujarCentrado(g2d, "John Ever Arredondo Raigosa", 800, posYAuthorsText + 120);
                dibujarCentrado(g2d, "Juan Manuel Galeano", 800, posYAuthorsText + 180);
                g2d.setFont(new Font("Courier New", Font.BOLD, fontSize - 10));
                dibujarCentrado(g2d, "Programacion Orientada", 800, posYAuthorsText + 300);
                dibujarCentrado(g2d, "a objetos", 800, posYAuthorsText + 340);
                if (logo != null) {
                    g2d.drawImage(logo, 200, posLogo, null);
                }
            }
        }
    }

    private void dibujarCentrado(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        FontMetrics fm = g2d.getFontMetrics();
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        g2d.drawString(stringText, posX, posY);
    }
}
