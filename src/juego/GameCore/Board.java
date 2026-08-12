package juego.GameCore; // Ajustá el paquete según tu estructura

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Board {

    // 1. Attributes
    private Entorno environment;
    private Image giftImage;
    private double screenWidth = 800.0;
    private double screenHeight = 600.0;
    private double menuHeight = 125.0;
    private double playAreaHeight = screenHeight - menuHeight;
    private int rows = 5;
    private int columns = 10;
    
    // 2. Constructor
    public Board(Entorno environment) {
        this.environment = environment;
        this.giftImage = Herramientas.cargarImagen("Regalooo.png");
    }
    
    // 3. Rendering
    public void drawBoard() {
        double boxWidth = this.screenWidth / this.columns;
        double boxHeight = this.playAreaHeight / this.rows;
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                double centerX = (j * boxWidth) + (boxWidth / 2.0);
                double centerY = this.menuHeight + (i * boxHeight) + (boxHeight / 2.0);
                
                Color boxColor;
                if ((i + j) % 2 == 0) {
                    boxColor = new Color(144, 238, 144); // Light green
                } else {
                    boxColor = new Color(0, 100, 0); // Dark green
                }
                
                this.environment.dibujarRectangulo(centerX, centerY, boxWidth, boxHeight, 0.0, boxColor);
            }
        }
    }
    
    public void drawGifts() {
        double boxWidth = this.screenWidth / this.columns;
        double boxHeight = this.playAreaHeight / this.rows;
        
        for (int i = 0; i < this.rows; i++) {
            double centerX = boxWidth / 2;
            double centerY = this.menuHeight + (i * boxHeight) + (boxHeight / 2);
            
            this.environment.dibujarImagen(this.giftImage, centerX, centerY, 0, 0.1);
        }
    }
    
    // 4. Utilities (Grid Math)
    
    /**
     * Returns true if mouse coordinates are inside the play area (not in the menu).
     */
    public boolean isInsideBoard(int mx, int my) {
        return my >= this.menuHeight && my <= this.screenHeight &&
               mx >= 0 && mx <= this.screenWidth;
    }
    
    /**
     * Converts pixel coordinates to grid coordinates [row, column].
     */
    public int[] getBox(int mx, int my) {
        if (!isInsideBoard(mx, my)) {
            return null;
        }
        int col = (int) (mx / getBoxWidth());
        int row = (int) ((my - this.menuHeight) / getBoxHeight());
        
        return new int[]{row, col};
    }
    
    /**
     * Returns exact center coordinates [x, y] of a given grid box.
     */
    public double[] getCenterBox(int row, int col) {
        if (row < 0 || row >= this.rows || col < 0 || col >= this.columns) {
            return null;
        }
        double x = (col * getBoxWidth()) + (getBoxWidth() / 2.0);
        double y = this.menuHeight + (row * getBoxHeight()) + (getBoxHeight() / 2.0);
        return new double[]{x, y};
    }

    // 5. Getters & Setters
    public double getMenuHeight() { return this.menuHeight; }
    public double getBoxWidth() { return this.screenWidth / this.columns; }
    public double getBoxHeight() { return this.playAreaHeight / this.rows; }
    public double getScreenHeight() { return this.screenHeight; }
    public void setScreenHeight(double screenHeight) { this.screenHeight = screenHeight; }
    public int getRows() { return this.rows; }
    public void setRows(int rows) { this.rows = rows; }
}