package juego.Plants;

import juego.GameCore.Board;
import entorno.Entorno;
import juego.Objets.Shot;
import juego.Zombies.FinalBoss;
import juego.Zombies.ZombieManager;

import java.awt.Color;
import java.awt.Image;

public abstract class Plant {
    
    protected double x, y;
    protected int row, column;

    protected boolean isAlive;
    protected int health, maxHealth;
    protected Image image;
    protected boolean isSelectedForMove;

    protected String name;

    public Plant(double x, double y, int row, int column, String name, int maxHealth) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.column = column;
        this.name = name;
        this.isAlive = true;
        this.isSelectedForMove = false;
    }

    // Abstract method to be implemented by subclasses
    public abstract Shot tick(Entorno e, ZombieManager zombieManager, FinalBoss jefeFinal);

    public void receiveDamage() {
        this.health --;
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    public void receiveShootDamage() {
        this.health -= 5;
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    //Method to check collision between two objects
    public boolean itIsColision(double x, double y, double otherX, double otherY) {
        double BroadcastWidth = 50; // Ancho de colisión
        double BroadcastHeight = 50; // Alto de colisión
        double left = this.x - BroadcastWidth / 2;
        double right = this.x + BroadcastWidth / 2;
        double otherLeft = x - BroadcastWidth / 2;
        double otherRight = x + BroadcastWidth / 2;
        double top = this.y - BroadcastHeight / 2;
        double bottom = this.y + BroadcastHeight / 2;
        double otherTop = y - BroadcastHeight / 2;
        double otherBottom = y + BroadcastHeight / 2;

        return left < otherRight && right > otherLeft && top < otherBottom && bottom > otherTop;
    }

    public abstract void draw(Entorno e);

    // Draw the plant on the screen
public void draw(Entorno e, double scale) {
        if (!this.isAlive) {return;}

        if(this.image != null) {
            // ACÁ ESTABA EL ERROR: Faltaba el 0 del ángulo antes del scale
            e.dibujarImagen(this.image, this.x, this.y, 0, scale); 
        } else {
            e.dibujarRectangulo(this.x, this.y, 50, 50, 0, Color.GREEN);
        }
    }

    // Method to set the plant's position based on its row and column in the grid
    public void setBox (int row, int column, Board board) {
        this.row = row;
        this.column = column;

        double[] newCenter = board.getCenterBox(row, column);
        this.x = newCenter[0];
        this.y = newCenter[1];
    }

    //Getters and Setters
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public boolean isAlive() { return this.isAlive; }
    public String getName() { return this.name; }
    public void setSelectedForMove(boolean selected) { this.isSelectedForMove = selected; }

    public boolean isClicked(double mouseX, double mouseY) {
        double distance = Math.sqrt(Math.pow(mouseX - this.x, 2) + Math.pow(mouseY - this.y, 2));
        return distance < 20; // Radio de clickeo
    }

}
