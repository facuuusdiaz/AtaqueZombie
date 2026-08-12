package juego.Objets;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Grave {
    
    // 1. Attributes
    private double x, y;
    private int row, column;
    private int health;
    private Image image;
    private double scale = 0.2;
    private double width = 40.0;
    private double height = 60.0;

    // 2. Constructor
    public Grave(double x, double y, int row, int column) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.column = column;
        this.health = 15; // Grave resistance
        
        this.image = Herramientas.cargarImagen("Tumbaa.png"); 
    }

    // 3. Actions
    public void receiveDamage() {
        this.health--;
    }
    
    public void receiveDamage(int damage) {
        this.health -= damage;
    }

    // 4. Rendering
    public void draw(Entorno e) {
        if (this.isAlive()) {
            e.dibujarImagen(this.image, this.x, this.y, 0, this.scale);
        }
    }

    // 5. Utilities (Collisions)
    public boolean isColliding(double otherX, double otherY, double otherWidth, double otherHeight) {
        double myLeft = this.x - this.width / 2;
        double myRight = this.x + this.width / 2;
        double myTop = this.y - this.height / 2;
        double myBottom = this.y + this.height / 2;

        double otherLeft = otherX - otherWidth / 2;
        double otherRight = otherX + otherWidth / 2;
        double otherTop = otherY - otherHeight / 2;
        double otherBottom = otherY + otherHeight / 2;

        return myLeft < otherRight && myRight > otherLeft && myTop < otherBottom && myBottom > otherTop;
    }

    // 6. Getters & Setters
    public boolean isAlive() { return this.health > 0; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getWidth() { return this.width; }
    public double getHeight() { return this.height; }
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
}