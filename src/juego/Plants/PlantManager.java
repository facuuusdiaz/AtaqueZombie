package juego.Plants;

import entorno.Entorno;
import juego.Objets.Shot;
import juego.Zombies.FinalBoss;
import juego.Zombies.ZombieManager;


import juego.GameCore.Board; 
import juego.Objets.GraveManager; 
import juego.GameUI.PlantCard; 

public class PlantManager {

    // 1. Attributes
    private static final int MAX_PLANTS = 50;
    private Plant[] plantsOnBoard;

    private static final int MAX_SHOTS = 200;
    private Shot[] shots;

    // 2. Constructor
    public PlantManager() {
        this.plantsOnBoard = new Plant[MAX_PLANTS];
        this.shots = new Shot[MAX_SHOTS];
    }

    // 3. Core Logic (Tick)
    public void tick(Entorno e, ZombieManager zombieManager, FinalBoss finalBoss) {
        
        // Think about plants and generate shots
        for (int i = 0; i < this.plantsOnBoard.length; i++) {
            Plant plant = this.plantsOnBoard[i];
            
            if (plant != null && plant.isAlive()) {
                // El polimorfismo en acción: cada planta sabe cómo disparar (o no)
                Shot d = plant.tick(e, zombieManager, finalBoss); 
                
                if (d != null) {
                    this.addShot(d);
                }
            }
        }

        // Move shots
        for (int i = 0; i < this.shots.length; i++) {
            if (this.shots[i] != null) {
                this.shots[i].move(); // Cambiar a .move() si tradujiste la clase Disparo
                
                if (this.shots[i].getX() > e.ancho()) {
                    this.shots[i] = null; // Remove shot if it goes off-screen
                }
            }
        }
    }

    // 4. Actions (Adding / Planting / Removing)
    private void addShot(Shot shot) {
        for (int i = 0; i < this.shots.length; i++) {
            if (this.shots[i] == null) {
                this.shots[i] = shot;
                return;
            }
        }
    }

    private boolean addPlant(Plant plant) {
        for (int i = 0; i < this.plantsOnBoard.length; i++) {
            if (this.plantsOnBoard[i] == null) {
                this.plantsOnBoard[i] = plant;
                return true;
            }
        }
        System.out.println("¡No hay más espacio para plantar!");
        return false;
    }

    /**
     * Plants a card on the board, validating the position and instantiating subclasses.
     */
    public boolean plant(PlantCard card, int mx, int my, Board board, GraveManager graveManager) {
        if (board.isInsideBoard(mx, my)) {

            int[] box = board.getBox(mx, my);
            if (box == null) return false; // Click outside the grid

            int row = box[0];
            int col = box[1];

            // Validations
            if (col == 0) { return false; } // Gifts column
            if (this.hasPlantIn(row, col)) { return false; } // Already has a plant
            if (graveManager.getGraveInBox(row, col) != null) { return false; } // Has a grave

            double[] centerBox = board.getCenterBox(row, col);
            String name = card.getCardName(); // Cambiar a .getName() si lo tradujiste

            Plant newPlant = null;

            // Instanciación limpia gracias a la Herencia
            switch (name) {
                case "Rose Blade":
                    newPlant = new RoseBlade(centerBox[0], centerBox[1], row, col);
                    break;
                case "Wall-nut":
                    newPlant = new WallNut(centerBox[0], centerBox[1], row, col);
                    break;
                case "Rose-Bomba":
                    newPlant = new RoseBomba(centerBox[0], centerBox[1], row, col);
                    break;
                default:
                    return false; // Carta no reconocida
            }

            return this.addPlant(newPlant);
        }
        return false;
    }

    public void removePlant(int index) {
         if (index >= 0 && index < this.plantsOnBoard.length) {
            this.plantsOnBoard[index] = null;
        }
    }

    public void removeShot(int index) {
        if (index >= 0 && index < this.shots.length) {
            this.shots[index] = null;
        }
    }

    // 5. Rendering
    public void draw(Entorno environment) {
        // Draw plants
        for (int i = 0; i < this.plantsOnBoard.length; i++) {
            if (this.plantsOnBoard[i] != null) {
                // NOTA: Para que esto funcione, acordate de crear un método 
                // public abstract void draw(Entorno e); en Plant.java
                // para que cada hija use su propia escala.
                this.plantsOnBoard[i].draw(environment); 
            }
        }
        // Draw shots
        for (int i = 0; i < this.shots.length; i++) {
            if (this.shots[i] != null) {
                this.shots[i].draw(environment); // Cambiar a .draw() si lo tradujiste
            }
        }
    }

    // 6. Utility & Getters
    public Plant getClickedPlant(double mx, double my) {
        for (Plant p : this.plantsOnBoard) {
            if (p != null && p.isAlive() && p.isClicked(mx, my)) {
                return p;
            }
        }
        return null;
    }

    public boolean hasPlantIn(int row, int col) {
        for (Plant p : this.plantsOnBoard) {
            if (p != null && p.isAlive() && p.getRow() == row && p.getColumn() == col) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPlantInRow(double yRow) {
        for (Plant p : this.plantsOnBoard) {
            if (p != null && p.isAlive() && p.getY() == yRow) {
                return true;
            }
        }
        return false;
    }

    public Plant[] getPlants() { return this.plantsOnBoard; }
    public Shot[] getShots() { return this.shots; }

}