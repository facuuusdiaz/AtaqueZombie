package juego.GameUI;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class ZombieCard {
    private double x, y;
    private double width = 80.0;
    private double height = 100.0;
    private String zombieName;
    private Image cardImage;
    private double scale; 

    public ZombieCard(double x, double y, String name, String imageFile, double scale) {
        this.x = x;
        this.y = y;
        this.zombieName = name;
        this.scale = scale;
        
        this.cardImage = Herramientas.cargarImagen(imageFile);
    }

    public void draw(Entorno environment) {
        environment.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, Color.DARK_GRAY);
        environment.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, Color.BLUE); 

        if (this.cardImage != null) {
            environment.dibujarImagen(this.cardImage, this.x, this.y - 10, 0, this.scale);
        } else {
            environment.dibujarRectangulo(this.x, this.y, this.width - 5, this.height - 5, 0, Color.RED);
        }

        environment.cambiarFont("Arial", 12, Color.WHITE);
        environment.escribirTexto(this.zombieName, this.x - 30, this.y + this.height/2 - 10);
    }
}
