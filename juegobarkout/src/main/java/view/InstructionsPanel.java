package view;

import java.awt.*;

public class InstructionsPanel {
    public InstructionsPanel(){

    }

    public void draw(Graphics2D g2d){
        //configuraciones
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //definir fuente y color
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Courier New", Font.BOLD, 40));
    }

    private void dibujarCentrado(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        // mide con la fuente actual
        FontMetrics fm = g2d.getFontMetrics();
        //calcula la posicion en x donde sera centrado
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        //dibuja de forma centrada
        g2d.drawString(stringText,posX,posY);
    }
}
