package juego.GameUI;

import java.awt.Color;
import entorno.Entorno;

public class InstructionsScreen {

    private Button backButton;

    public InstructionsScreen() {
        this.backButton = new Button(400, 545, 180, 50, Text.BACK);
    }

    public void draw(Entorno e) {
        e.dibujarRectangulo(400, 300, 800, 600, 0, new Color(10, 10, 10));
        e.dibujarRectangulo(400, 300, 700, 470, 0, new Color(28, 28, 28, 235));
        e.dibujarRectangulo(400, 300, 700, 470, 0, new Color(110, 110, 110));

        e.cambiarFont("Arial", 26, Color.GREEN);
        e.escribirTexto(Text.HOW_TO_PLAY.get(), 310, 100);

        e.cambiarFont("Arial", 15, Color.WHITE);
        int x = 90;
        int y = 145;
        int lineHeight = 27;

        String[] lines = Text.INSTRUCTIONS_BODY.get().split("\n");
        for (String line : lines) {
            e.escribirTexto(line, x, y);
            y += lineHeight;
        }

        this.backButton.draw(e);
    }

    public boolean clickedBack(int mx, int my) {
        return this.backButton.isClicked(mx, my);
    }
}
