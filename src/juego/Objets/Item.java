package juego.Objets;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Item {
    
    // 1. Attributes
    private double x, y;
    private String type; // "bueno" or "malo"
    private double radius = 15;
    private Image image; 

    // 2. Constructor
    public Item(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;

        // Load a single image for the potion (surprise effect)
        this.image = Herramientas.cargarImagen("pocion.png");
    }

    // 3. Rendering
    public void draw(Entorno e) {
        if (this.image != null) {
            e.dibujarImagen(this.image, this.x, this.y, 0, 0.15);
        } else {
            // Fallback: draw colored circles if image fails to load
            Color baseColor;
            Color borderColor;

            if (this.type.equals("bueno")) {
                baseColor = new Color(0, 255, 0, 150); // Transparent green
                borderColor = Color.GREEN;
            } else {
                baseColor = new Color(255, 0, 0, 150); // Transparent red
                borderColor = Color.RED;
            }
            e.dibujarCirculo(this.x, this.y, this.radius, baseColor);
            e.dibujarCirculo(this.x, this.y, this.radius, borderColor);
        }
    }

    // 4. Utilities
    /**
     * Checks if coordinates (mx, my) are inside the item.
     */
    public boolean isClicked(int mx, int my) {
        double distance = Math.sqrt(Math.pow(mx - this.x, 2) + Math.pow(my - this.y, 2));
        // Increase click radius slightly to make it easier to grab
        return distance < this.radius * 1.5;
    }

    // 5. Getters
    public String getType() {
        return this.type;
    }
}