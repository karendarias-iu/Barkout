package controller;

import view.GameWindow;
/**
 * Barkout
 *
 * Es un videojuego donde nuestro personaje es una mascota
 * son 2 personajes (lucas o salchicha)
 * la misión es no dejar que las ardillas se lleven el balón
 * si usas el hotDog ganas 100 puntos
 * el hueso te da super velocidad por 8 segundos
 *
 * @author Karen Daiana Arias Cardona
 * @author Juan Manuel Galeano
 * @author John Ever Arredondo Raigosa
 * @version 1.0  (20/05/2026)
 *
 */
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });
    }
}
