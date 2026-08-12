package juego.GameUI;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class MainMenu {

    private Button playButton;
    private Button instructionsButton;
    private Button optionsButton;
    private Button exitButton;
    private Image backgroundImage;

    public MainMenu() {
        this.playButton = new Button(400, 300, 240, 55, Text.PLAY);
        this.instructionsButton = new Button(400, 368, 240, 55, Text.INSTRUCTIONS);
        this.optionsButton = new Button(400, 436, 240, 55, Text.OPTIONS);
        this.exitButton = new Button(400, 504, 240, 55, Text.EXIT);

        // Opcional: si tenés una imagen de fondo para el menú, descomentá esta línea
        // (safeLoad evita que el juego crashee si el archivo no existe).
        // this.backgroundImage = safeLoad("MenuFondo.png");
    }

    private Image safeLoad(String filename) {
        try {
            return Herramientas.cargarImagen(filename);
        } catch (Exception ex) {
            return null;
        }
    }

    public void draw(Entorno e) {
        if (this.backgroundImage != null) {
            e.dibujarImagen(this.backgroundImage, 400, 300, 0, 1.0);
        } else {
            e.dibujarRectangulo(400, 300, 800, 600, 0, new Color(8, 35, 8));
        }

        // Overlay oscuro para que el texto se lea bien encima del fondo
        e.dibujarRectangulo(400, 300, 800, 600, 0, new Color(0, 0, 0, 110));

        e.cambiarFont("Arial", 44, Color.GREEN);
        e.escribirTexto(Text.TITLE.get(), 175, 155);

        e.cambiarFont("Arial", 14, Color.LIGHT_GRAY);
        e.escribirTexto(Text.SUBTITLE.get(), 150, 195);

        this.playButton.draw(e);
        this.instructionsButton.draw(e);
        this.optionsButton.draw(e);
        this.exitButton.draw(e);
    }

    /**
     * Devuelve "PLAY", "INSTRUCTIONS", "OPTIONS", "EXIT" según el botón clickeado, o null si no clickeó ninguno.
     */
    public String getClickedOption(int mx, int my) {
        if (this.playButton.isClicked(mx, my)) { return "PLAY"; }
        if (this.instructionsButton.isClicked(mx, my)) { return "INSTRUCTIONS"; }
        if (this.optionsButton.isClicked(mx, my)) { return "OPTIONS"; }
        if (this.exitButton.isClicked(mx, my)) { return "EXIT"; }
        return null;
    }
}
