package view;

import controller.SoundManager;
import model.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameIntro {
    //nombre, posicion en y y tamaño del titulo del juego (Barkout)
    private String gameName = "Barkout";
    private int posGameNameY = 300;
    private int fontSize = 95;

    //punto de referencia en y donde se dibujan los nombres de los autores y se usa la imagen del logo uam
    private int posAuthorsNamesY = 600;
    private int posLogo = posAuthorsNamesY + 360;
    //imagen del logo uam
    private BufferedImage logo;

    //bandera para determinar si la animacion de la intro ha acabado
    private boolean introEnded = false;

    //bandera usada para reproducir el audio una sola vez
    private boolean playedSound = false;

    //variable para manejar los tiempos
    private long initialTime;

    /**
     * Variable que maneja el estado de la animacion en el cual se encuentra
     * (1) Muestra el nombre del juego por 5 segundos, al segundo 2 incia el sonido de la intro
     * (2) Desplaza el titulo hacia arriba
     * (3) Desplaza el punto de referencia de autores hacia arriba, de forma que se muetran los nombres y el logo
     */
    private int animationPhase = 0;

    //Constructor por defecto
    public GameIntro() {
        //guarda el tiempo en ms que han pasado desde la ejecucion del progama
        initialTime = System.nanoTime();
        //se carga la imagen del logo uam
        logo = Entity.uploadImage("logo_uam.png");
    }

    /**
     * Funcion para actualizar valores
     * Se llama para actualizar coordenadas y realizar acciones segun el tiempo transcurrido
     */
    public void update() {
        //tiempo actual en ms
        long actualTime = System.nanoTime();
        //tiempo transcurrido de la fase en segundos
        long timePhase = (actualTime - initialTime) / 1_000_000_000L;

        //mientras no haya pasado un segundo la pantalla sigue en negro
        if (timePhase == 1) {
            //se pasa a la fase 1
            animationPhase = 1;
        }

        //switch case segun la fase de animacion
        switch (animationPhase) {
            case 1:
                //transcurridos 2 segundos
                if (timePhase >= 2) {
                    //se carga la cancion 1 vez haciendo uso de la bandera
                    if(!playedSound){
                        SoundManager.playSound("introSound.wav");
                        playedSound = true;
                    }
                }
                //transcurridos 5 segundos
                if (timePhase >= 5) {
                    //pasa a fase 2
                    animationPhase = 2;
                }
                break;
            case 2:
                //disminuye el tamaño hasta llegar a 60
                if (fontSize >= 60) {
                    fontSize--;
                }

                //desplaza el nombre del juego hasta que desaparezca
                if (posGameNameY > 0) {
                    posGameNameY -= 2;
                } else {
                    //cambia a fase 3
                    animationPhase = 3;
                }
                break;
            case 3:
                //desplaza el punto de referencia hacia arriba hasta que el lgoo desaparezca
                if (posLogo >= 0) {
                    posAuthorsNamesY -= 2;
                    posLogo = posAuthorsNamesY + 360;
                } else {
                    //una vez llegada se hace la bandera de introEnded a true para hacer saber que ha terminado la animacion
                    introEnded = true;
                }
                break;
        }
    }

    /**
     * Getter del estado de animacion de la intro
     * @return introEnded estado de animacion
     */
    public boolean isIntroEnded() {
        return introEnded;
    }

    /**
     * Metodo para dibujar la intro
     * @param g2d recibe el elemento en el cual se va a dibujar
     */
    public void draw(Graphics2D g2d) {
        //Configuracion del panel donde se dibuja
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //color y fuente de texto
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Courier New", Font.BOLD, fontSize));

        //siempre y cuando la animacion este o sea mayor a la primera fase
        if (animationPhase >= 1) {
            //fase 1 y 3 (Dibuja el nombre del juego de forma centrada)
            if (animationPhase < 3) {
                //lo dibuja centrado en la posicion en y del mismo
                drawCenterText(g2d, gameName, 800, posGameNameY);
            } else if (animationPhase == 3) {
                //dibuja el nombre de los autores y logo segun el punto de referencia
                drawCenterText(g2d, "Autores", 800, posAuthorsNamesY);
                g2d.setFont(new Font("Courier New", Font.BOLD, fontSize - 20));
                drawCenterText(g2d, "Karen Daiana Arias Cardona", 800, posAuthorsNamesY + 60);
                drawCenterText(g2d, "John Ever Arredondo Raigosa", 800, posAuthorsNamesY + 120);
                drawCenterText(g2d, "Juan Manuel Galeano", 800, posAuthorsNamesY + 180);
                g2d.setFont(new Font("Courier New", Font.BOLD, fontSize - 10));
                drawCenterText(g2d, "Programacion Orientada", 800, posAuthorsNamesY + 300);
                drawCenterText(g2d, "a objetos", 800, posAuthorsNamesY + 340);
                if (logo != null) {
                    g2d.drawImage(logo, 200, posLogo, null);
                }
            }
        }
    }

    /**
     * Funcion para dibujar texto centrado
     * @param g2d elemento donde se va a dibujar
     * @param stringText texto a dibujar
     * @param panelWidth ancho del panel
     * @param posY posicion en y donde se dibuja
     */
    private void drawCenterText(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        FontMetrics fm = g2d.getFontMetrics();
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        g2d.drawString(stringText, posX, posY);
    }
}
