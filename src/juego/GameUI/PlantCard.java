package juego.GameUI;

import java.awt.Color;
import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;

public class PlantCard {

    private double x,y;
    private double width, height;
    private boolean isSelected;
    private String cardName;
    private int cooldown;
    private int nextAvailableTime;
    private boolean isReady;
    private Image cardImage;
    
    public PlantCard (double x, double y, String cardName) {
        this.x = x;
        this.y = y;
        this.width = 80.0;
        this.height = 100.0;
        this.cardName = cardName;
        this.isSelected = false;
        this.setReady(true);
        this.nextAvailableTime = 0;

        if (this.cardName.equals("Rose Blade")) {
            this.cardImage = Herramientas.cargarImagen("RoseBlade.png");
            this.cooldown = 4000;
        } else if (this.cardName.equals("Wall-nut")) {
            this.cardImage = Herramientas.cargarImagen("Wall-nutt.png"); 
            this.cooldown = 10000;
        } else if (this.cardName.equals("Rose-Bomba")) {
            this.cardImage = Herramientas.cargarImagen("Rose-Bombaa.png");
            this.cooldown = 15000;
        }
    }
    
    public boolean isReady() {
        return isReady;
        
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
        
    }

    public void tick (Entorno environment) {
        if (!this.isReady()) {
            if (environment.tiempo() >= this.nextAvailableTime) {
                this.setReady(true);
            }
        }
    }
    
    public void startCooldown(Entorno environment) {
        this.setReady(false);
        this.nextAvailableTime = environment.tiempo() + cooldown;
    }
    
    public void draw(Entorno environment) {
        Color borderColor;
        if (this.isSelected) {
            borderColor = Color.GREEN;
        } else {
            borderColor = new Color(80, 80, 80);
        }
        environment.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, Color.DARK_GRAY);
        environment.dibujarRectangulo(this.x, this.y, this.width, this.height, 0, borderColor);
        
        if (this.cardImage != null) {
            double scale = 0.1;
            if (this.cardName.equals("Rose Blade")) { scale = 0.2; }
            environment.dibujarImagen(this.cardImage, this.x, this.y - 10, 0, scale);
        } else {
            environment.dibujarRectangulo(this.x, this.y, this.width - 5, this.height - 5, 0, Color.ORANGE);
        }
        
        environment.cambiarFont("Arial", 12, Color.WHITE);
        environment.escribirTexto(this.cardName, this.x - 30, this.y + this.height/2 - 10);
        
        if (!this.isReady()) {
            long currentTime = environment.tiempo();
            long timeRemaining = this.nextAvailableTime - currentTime;
            if (timeRemaining < 0) { timeRemaining = 0; }
            double progress = (double)timeRemaining / (double)this.cooldown;
            progress = Math.min(progress, 1.0);
            double barHeight = this.height * progress;
            double barY = (this.y - this.height / 2) + (barHeight / 2);
            Color reloadColor = new Color(40, 40, 40, 200);
            environment.dibujarRectangulo(this.x, barY, this.width, barHeight, 0, reloadColor);
        }
    }
    
    public boolean isClicked(int mx, int my) {
        double minX = this.x - this.width / 2;
        double maxX = this.x + this.width / 2;
        double minY = this.y - this.height / 2;
        double maxY = this.y + this.height / 2;
        
        return (mx >= minX && mx <= maxX && my >= minY && my <= maxY);
    }
    
    public void modifyCooldown(String type) {
        if (!this.isReady()) {
            int modifier = 3000;
            if (type.equals("bueno")) {
                this.nextAvailableTime -= modifier;
            } else if (type.equals("malo")) {
                this.nextAvailableTime += modifier;
            }
        }
    }
    
    public void setSelected(boolean selected) { this.isSelected = selected; }
    public String getCardName() { return this.cardName; }
}
