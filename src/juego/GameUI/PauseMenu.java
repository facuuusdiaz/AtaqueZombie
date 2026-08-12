package juego.GameUI;

import java.awt.Color;
import entorno.Entorno;

public class PauseMenu {

    private Button resumeButton;
    private Button restartButton;
    private Button exitButton;

    public PauseMenu() {
        this.resumeButton = new Button(400, 260, 260, 55, Text.RESUME);
        this.restartButton = new Button(400, 330, 260, 55, Text.RESTART);
        this.exitButton = new Button(400, 400, 260, 55, Text.EXIT_TO_MENU);
    }

    public void draw(Entorno e) {
        // Overlay oscuro sobre el juego "congelado" que se dibuja detrás
        e.dibujarRectangulo(400, 300, 800, 600, 0, new Color(0, 0, 0, 170));

        e.dibujarRectangulo(400, 300, 360, 300, 0, new Color(25, 25, 25, 245));
        e.dibujarRectangulo(400, 300, 360, 300, 0, new Color(120, 120, 120));

        e.cambiarFont("Arial", 30, Color.WHITE);
        e.escribirTexto(Text.PAUSE_TITLE.get(), 335, 175);

        this.resumeButton.draw(e);
        this.restartButton.draw(e);
        this.exitButton.draw(e);
    }

    /**
     * Devuelve "RESUME", "RESTART", "EXIT" según el botón clickeado, o null si no clickeó ninguno.
     */
    public String getClickedOption(int mx, int my) {
        if (this.resumeButton.isClicked(mx, my)) { return "RESUME"; }
        if (this.restartButton.isClicked(mx, my)) { return "RESTART"; }
        if (this.exitButton.isClicked(mx, my)) { return "EXIT"; }
        return null;
    }
}
