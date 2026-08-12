package juego.Objets;

import entorno.Entorno;
import juego.GameCore.Board;

public class GraveManager {
    
    // 1. Attributes
    private static final int MAX_GRAVES = 50;
    private Grave[] gravesOnBoard;
    private Board board;

    // 2. Constructor
    public GraveManager(Board board) {
        this.gravesOnBoard = new Grave[MAX_GRAVES];
        this.board = board;
    }

    // 3. Actions
    public boolean addGrave(int row, int col) {
        // Validation: No graves outside board or in gifts column (0)
        if (row < 0 || row >= this.board.getRows() || col <= 0 || col >= 10) { 
            return false;
        }
        
        // Validation: No overlapping graves
        if (getGraveInBox(row, col) != null) {
            return false;
        }

        // Find empty spot
        for (int i = 0; i < this.gravesOnBoard.length; i++) {
            if (this.gravesOnBoard[i] == null) {
                double[] center = board.getCenterBox(row, col);
                this.gravesOnBoard[i] = new Grave(center[0], center[1], row, col);
                return true; 
            }
        }
        System.out.println("No space for more graves!");
        return false; 
    }

    public void removeGrave(int index) {
         if (index >= 0 && index < this.gravesOnBoard.length) {
            this.gravesOnBoard[index] = null;
        }
    }
    
    public void removeGrave(Grave graveToRemove) {
        for (int i = 0; i < gravesOnBoard.length; i++) {
            if (gravesOnBoard[i] == graveToRemove) {
                gravesOnBoard[i] = null;
                return;
            }
        }
    }

    // 4. Rendering
    public void draw(Entorno environment) {
        for (Grave g : this.gravesOnBoard) {
            if (g != null && g.isAlive()) {
                g.draw(environment);
            }
        }
    }
 
    // 5. Utilities
    public Grave getGraveInBox(int row, int col) {
        for (Grave g : this.gravesOnBoard) {
            if (g != null && g.isAlive() && g.getRow() == row && g.getColumn() == col) {
                return g;
            }
        }
        return null;
    }    
     
    // 6. Getters
    public Grave[] getGraves() {
        return this.gravesOnBoard;
    }
}