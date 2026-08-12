package juego.Zombies;

import java.awt.Color;
import entorno.Entorno;

public class ZombieProjectile {

    // 1. Attributes
    private double x;
    private double y;
    private double speed;
    private double diameter;
    
    // 2. Constructor
    public ZombieProjectile(double x, double y) {
        this.x = x - 20; // Spawns slightly ahead (left) of the zombie
        this.y = y;
        this.speed = -2.0; // Negative speed to move left
        this.diameter = 12.0;
    }
    
    // 3. Actions
    public void move() {
        // Moves to the left
        this.x += this.speed;
    }
    
    // 4. Rendering
    public void draw(Entorno e) {
        // White snowball
        e.dibujarCirculo(this.x, this.y, this.diameter, Color.WHITE); 
    }
    
    // 5. Getters & Setters (For collisions)
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getDiameter() { return this.diameter; }
}