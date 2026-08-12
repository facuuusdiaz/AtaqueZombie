package juego.GameUI;

import java.awt.Color;
import entorno.Entorno;

/**
 * Botón simple y reutilizable para menús (Play, Pausa, Instrucciones, etc.).
 * Cambia de color cuando el mouse está encima (hover), y el texto se resuelve
 * en cada draw() según el idioma actual (Lang.getCurrent()).
 */
public class Button {

    private double x, y;
    private double width, height;
    private Text label;
    private Color baseColor;
    private Color hoverColor;
    private Color textColor;

    public Button(double x, double y, double width, double height, Text label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.baseColor = new Color(35, 35, 35, 235);
        this.hoverColor = new Color(60, 130, 60, 235);
        this.textColor = Color.WHITE;
    }

    public void draw(Entorno e) {
        boolean hover = isHover(e.mouseX(), e.mouseY());
        Color fill = hover ? this.hoverColor : this.baseColor;
        Color border = hover ? Color.WHITE : new Color(140, 140, 140);

        e.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, fill);
        e.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, border);

        String text = this.label.get();
        e.cambiarFont("Arial", 18, this.textColor);
        // Centrado aproximado del texto (ajustá el factor si tu fuente es más ancha/angosta)
        double textX = this.x - (text.length() * 5.0);
        double textY = this.y + 6;
        e.escribirTexto(text, textX, textY);
    }

    public boolean isHover(int mx, int my) {
        double minX = this.x - this.width / 2;
        double maxX = this.x + this.width / 2;
        double minY = this.y - this.height / 2;
        double maxY = this.y + this.height / 2;
        return mx >= minX && mx <= maxX && my >= minY && my <= maxY;
    }

    public boolean isClicked(int mx, int my) {
        return isHover(mx, my);
    }
}
