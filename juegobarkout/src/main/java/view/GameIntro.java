package view;

import java.awt.*;

public class GameIntro {
    //Variables titulo del juego (Barkout)

    //posicion inicial en y
    private int positionTitleY = 300;
    //nombre
    private String titleName = "Barkout";
    //tamaño de texto
    private int fontSize = 95;
    //variable bandera para determinar el final del intro del juego
    private boolean introEnded = false;

    //Variables tiempos de animacion
    //tiempo inicial en segundos
    private long initialTime;

    /**
     * Variable que maneja 4 fases de la animacion de la intro
     * (1) muestra el nombre del juego por 5 segundos
     * (2) reduce el tamaño del nombre del juego y lo desplaza hacia arriba
     * (3) muestra y desplaza el nombre de los autores
     * (4) una vez terminada la fase anterior se muestra el logo de la uam
     */
    private int animationPhase = 0;

    //variable punto de referencia para el texto de los autores
    private int posYAuthorsText = 600;

    //Constructor del GameIntro
    public GameIntro(){
        //almacena el tiempo en ms una vez se llame GameIntro
        initialTime = System.nanoTime();
    }

    /**
     * Funcion que se llama constantemente para:
     * Actualizar posiciones segun la fase de animacion
     * Hacer cambios segun el tiempo transcurrido
     */
    public void update(){
        //variable que obtiene el tiempo actual y se le hace una diferencia para obtener el tiempo de animacion
        long actualTime = System.nanoTime();
        long timePhase =(actualTime-initialTime) / 1_000_000_000L;

        if (timePhase==1){
            animationPhase = 1;
        }

        //Casos segun la fase de animacion
        switch (animationPhase){
            case 1:
                //cuando pasen 5 segundos pasa a fase 2
                if(timePhase>=5){
                    animationPhase = 2;
                }
                break;
            case 2:
                //disminuye el tamaño de la letra
                if(fontSize>=60){
                    fontSize--;
                }
                //sube el texto
                if(positionTitleY>0){
                    positionTitleY-=2;
                }else{
                    //una vez llega al final pasa a la fase 3
                    animationPhase = 3;
                }
                break;
            case 3:
                if((posYAuthorsText+190)>=0){
                    posYAuthorsText-=2;
                }else{
                    introEnded = true;
                }
                break;
        }
    }

    //retorna el valor de la variable para determinar si ha acabado la animacion
    public boolean isIntroEnded(){
        return introEnded;
    }

    //metodo propio que se llama con el repaint()
    public void draw(Graphics2D g2d){
        //configuraciones
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //definir fuente y color
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Courier New", Font.BOLD, fontSize));

        //logica
        if(animationPhase>=1){
            if(animationPhase<3){
                dibujarCentrado(g2d,titleName,800,positionTitleY);
            }else{
                dibujarCentrado(g2d,"Autores",800,posYAuthorsText);
                g2d.setFont(new Font("Courier New", Font.BOLD, fontSize-20));
                dibujarCentrado(g2d,"Karen Daiana Arias Cardona",800,posYAuthorsText+60);
                dibujarCentrado(g2d,"John Ever Arredondo Raigosa",800,posYAuthorsText+120);
                dibujarCentrado(g2d,"Juan Manuel Galeano",800,posYAuthorsText+180);
            }
        }
    }

    /**
     * Funcion para dibujar el texto de forma centrada
     * @param g2d se le pasa el lienzo en donde se dibuja
     * @param stringText lo que se muestra
     * @param panelWidth ancho del panel
     * @param posY posicion en y donde se dibuja el texto
     */
    private void dibujarCentrado(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        // mide con la fuente actual
        FontMetrics fm = g2d.getFontMetrics();
        //calcula la posicion en x donde sera centrado
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        //dibuja de forma centrada
        g2d.drawString(stringText,posX,posY);
    }
}
