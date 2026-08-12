package juego.GameUI;

import java.awt.Color;
import entorno.Entorno;

/**
 * Pantalla de opciones. Por ahora solo tiene el switch de idioma.
 * (Pantalla completa no está soportada por el framework Entorno sin
 * reescribir el sistema de coordenadas de todo el juego, así que se dejó
 * afuera por ahora.)
 */
public class OptionsMenu {

    private double langX = 400, langY = 260, langWidth = 280, langHeight = 55;
    private Button backButton;

    public OptionsMenu() {
        this.backButton = new Button(400, 450, 200, 55, Text.BACK);
    }

    public void draw(Entorno e) {
        e.dibujarRectangulo(400, 300, 800, 600, 0, new Color(10, 10, 10));
        e.dibujarRectangulo(400, 300, 500, 380, 0, new Color(28, 28, 28, 235));
        e.dibujarRectangulo(400, 300, 500, 380, 0, new Color(110, 110, 110));

        e.cambiarFont("Arial", 26, Color.GREEN);
        e.escribirTexto(Text.OPTIONS_TITLE.get(), 305, 140);

        // Botón de idioma (dibujado a mano para poder combinar etiqueta + valor actual)
        boolean hover = isInsideLangButton(e.mouseX(), e.mouseY());
        Color fill = hover ? new Color(60, 130, 60, 235) : new Color(35, 35, 35, 235);
        e.dibujarRectangulo(this.langX, this.langY, this.langWidth, this.langHeight, 0, fill);
        e.dibujarRectangulo(this.langX, this.langY, this.langWidth, this.langHeight, 0, new Color(140, 140, 140));

        String langText = Text.LANGUAGE_LABEL.get() + " " + Lang.getCurrent().getDisplayName();
        e.cambiarFont("Arial", 16, Color.WHITE);
        e.escribirTexto(langText, this.langX - (langText.length() * 4.5), this.langY + 6);

        e.cambiarFont("Arial", 12, new Color(150, 150, 150));
        e.escribirTexto(Text.FULLSCREEN_LABEL.get(), 210, 350);

        this.backButton.draw(e);
    }

    private boolean isInsideLangButton(int mx, int my) {
        double minX = this.langX - this.langWidth / 2;
        double maxX = this.langX + this.langWidth / 2;
        double minY = this.langY - this.langHeight / 2;
        double maxY = this.langY + this.langHeight / 2;
        return mx >= minX && mx <= maxX && my >= minY && my <= maxY;
    }

    /**
     * Llamar cuando hay un click; si tocó el botón de idioma, lo togglea.
     */
    public void handleClick(int mx, int my) {
        if (isInsideLangButton(mx, my)) {
            Lang.toggle();
        }
    }

    public boolean clickedBack(int mx, int my) {
        return this.backButton.isClicked(mx, my);
    }
}
