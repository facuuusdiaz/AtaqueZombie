package juego.Objets;

import java.awt.Color;
import entorno.Entorno;

public class Shot {

    // 1. Attributes
    private double x;
    private double y;
    private double speed;
    private double width;
    private double height;
    
    // 2. Constructor
    public Shot(double x, double y) {
        this.x = x + 20; // Spawns slightly ahead of the plant
        this.y = y;
        this.speed = 3.0; // Projectile speed
        
        this.width = 12.0;
        this.height = 6.0;
    }
    
    // 3. Actions
    public void move() {
        // Moves to the right
        this.x += this.speed;
    }
    
    // 4. Rendering
    public void draw(Entorno e) {
        e.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, Color.ORANGE);
    }
    
    // 5. Getters (For collisions)
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getWidth() { return this.width; }
    public double getHeight() { return this.height; }
}