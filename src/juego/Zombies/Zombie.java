package juego.Zombies;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import juego.Plants.PlantManager; // Acordate de ajustar tus imports

public abstract class Zombie {
    protected double x, y;
    protected double speed, width, height, scale;
    protected int health;
    protected boolean isAttacking;
    protected Image image;
    protected int shootDelayTicks;
    protected int tickCounter;

    public Zombie(double x, double y, double speed, int health, String imageName, double width, double height, double scale, int shootDelayTicks) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.isAttacking = false;
        this.shootDelayTicks = shootDelayTicks;
        
        this.image = Herramientas.cargarImagen(imageName);
        this.tickCounter = -(int)(Math.random() * this.shootDelayTicks); 
    }

    //Method for the zombie to perform its actions each tick
    public ZombieProjectile tick(Entorno e, PlantManager plantManager) {
        if (!this.isAlive()) { return null; }
        
        this.move();
        this.tickCounter++;

        boolean hasPlant = plantManager.hasPlantInRow(this.y);

        if (this.isAttacking || this.tickCounter < this.shootDelayTicks || !hasPlant) {
            return null; 
        }

        this.tickCounter = 0;
        return new ZombieProjectile(this.x, this.y);
    }

    // Method to move the zombie
    public void move() {
        if (!this.isAttacking) {
            this.x -= this.speed;
        }
    }

    //method for drawing the zombie on the screen
public void draw(Entorno e) {
        if (this.image != null) {
            e.dibujarImagen(this.image, this.x, this.y, 0, this.scale);
        } else {
            // RED DE SEGURIDAD: Si no hay imagen, dibuja un cuadrado rojo
            e.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, java.awt.Color.RED);
        }
    }

    //Method to check collision between the zombie and a plant
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

    // Getters & Setters
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getWidth() { return this.width; }
    public double getHeight() { return this.height; }
    public boolean isAlive() { return this.health > 0; }
    public void receiveDamage() { this.health--; }
    public void receiveDamage(int damage) { this.health -= damage; }
    public void setAttacking(boolean attacking) { this.isAttacking = attacking; }
}

