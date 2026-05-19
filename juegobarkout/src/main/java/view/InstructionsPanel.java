package view;

import java.awt.*;

public class InstructionsPanel {

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Courier New", Font.BOLD, 40));
    }

    private void dibujarCentrado(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        FontMetrics fm = g2d.getFontMetrics();
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        g2d.drawString(stringText, posX, posY);
    }
}
