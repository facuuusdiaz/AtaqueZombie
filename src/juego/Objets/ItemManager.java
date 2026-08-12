package juego.Objets;

import entorno.Entorno;

public class ItemManager {
    
    // 1. Attributes
    private static final int MAX_ITEMS = 30; // Maximum items on screen
    private Item[] itemsOnBoard;

    // 2. Constructor
    public ItemManager() {
        this.itemsOnBoard = new Item[MAX_ITEMS];
    }

    // 3. Actions
    /**
     * Adds a new item to the board at (x, y). Randomly decides if it's good or bad.
     */
    public void addItem(double x, double y) {
        for (int i = 0; i < this.itemsOnBoard.length; i++) {
            if (this.itemsOnBoard[i] == null) {
                // 50% chance of being good, 50% bad
                String type = (Math.random() > 0.5) ? "bueno" : "malo";
                
                this.itemsOnBoard[i] = new Item(x, y, type);
                return; 
            }
        }
    }

    public void removeItem(Item itemToRemove) {
        for (int i = 0; i < this.itemsOnBoard.length; i++) {
            if (this.itemsOnBoard[i] == itemToRemove) {
                this.itemsOnBoard[i] = null;
                return; 
            }
        }
    }

    // 4. Rendering
    public void draw(Entorno environment) {
        for (Item item : this.itemsOnBoard) {
            if (item != null) {
                item.draw(environment);
            }
        }
    }

    // 5. Utilities
    /**
     * Checks if an item was clicked and returns it. Returns null if none was clicked.
     */
    public Item getClickedItem(int mx, int my) {
        for (Item item : this.itemsOnBoard) {
            if (item != null && item.isClicked(mx, my)) {
                return item;
            }
        }
        return null; 
    }
}
