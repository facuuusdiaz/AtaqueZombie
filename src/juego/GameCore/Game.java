package juego.GameCore;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;
import juego.Plants.*;
import juego.Zombies.*;
import juego.Objets.*;
import juego.GameUI.*;

public class Game extends InterfaceJuego {

    private Entorno environment;
    
    private Board board;
    private PlantCardBar cardBar;
    private ZombieBar zombieBar;
    private PlantCard selectedCard;

    private PlantManager plantManager;
    private ZombieManager zombieManager;
    private GraveManager graveManager;
    private ItemManager itemManager;
    private Plant movingPlant;

    private Collisions collisions;

    private boolean isGameOver;
    private boolean isGameWon;
    private double giftX; 

    private int zombiesToWin;
    private int tickCounter;
    private int gameSeconds;
    
    private boolean bossSpawned;
    private FinalBoss finalBoss; 

    public Game() {
        this.environment = new Entorno(this, "Zombie Survival", 800, 600);

        this.board = new Board(this.environment);
        this.cardBar = new PlantCardBar();
        this.zombieBar = new ZombieBar();

        this.selectedCard = null;
        this.movingPlant = null;
        this.plantManager = new PlantManager();
        this.zombieManager = new ZombieManager(this.board); 
        this.graveManager = new GraveManager(this.board); 
        this.itemManager = new ItemManager();
        this.collisions = new Collisions();

        this.isGameOver = false;
        this.isGameWon = false;
        this.bossSpawned = false;
        this.finalBoss = null;
        
        this.giftX = this.board.getBoxWidth() / 2.0;
        this.zombiesToWin = 50; 

        this.tickCounter = 0;
        this.gameSeconds = 0;

        this.environment.iniciar();
    }

    public void tick() {
        // 1. GAME STATE CHECKS
        if (!this.bossSpawned && this.zombieManager.getTotalKilled() >= this.zombiesToWin) {
            this.finalBoss = this.zombieManager.spawnFinalBoss(this.board);
            this.bossSpawned = true;
            this.zombieManager.stopSpawns(); 
            this.zombieManager.setFinalBoss(this.finalBoss);
        }
        
        if (this.bossSpawned && this.finalBoss != null && !this.finalBoss.isAlive()) {
            this.isGameWon = true;
        }

        if (this.isGameWon) {
            environment.cambiarFont("Arial", 50, Color.GREEN);
            environment.escribirTexto("¡GANASTE!", 280, 300);
            return;
        }

        if (this.isGameOver) {
            environment.cambiarFont("Arial", 50, Color.RED);
            environment.escribirTexto("¡PERDISTE!", 280, 300);
            return;
        }

        // Timer
        this.tickCounter++;
        if (this.tickCounter >= 60) {
            this.gameSeconds++;
            this.tickCounter = 0;
        }

        // 2. GAME LOGIC
        this.cardBar.tick(this.environment); 
        this.handleMouse(); 
        this.handleKeyboard(); 

        this.zombieManager.tick(this.environment, this.board, this.plantManager);
        this.plantManager.tick(this.environment, this.zombieManager, this.finalBoss);

        boolean shouldEnd = this.collisions.check(
                this.plantManager, this.zombieManager, this.board, 
                this.finalBoss, this.graveManager, this.itemManager, 
                this.giftX, this.environment
            );

        if (shouldEnd) {
            this.isGameOver = true;
        }

        // 3. RENDERING
        this.board.drawBoard();
        this.drawHUD();

        this.cardBar.draw(this.environment);
        this.zombieBar.draw(this.environment);

        this.graveManager.draw(this.environment);
        this.itemManager.draw(this.environment);

        this.plantManager.draw(this.environment);
        this.zombieManager.draw(this.environment);

        if (this.selectedCard != null) {
            int mx = this.environment.mouseX();
            int my = this.environment.mouseY();
            environment.dibujarCirculo(mx, my, 40, Color.CYAN);
        }

        this.board.drawGifts();
    }

    private void handleMouse() {
        if (this.environment.sePresionoBoton(environment.BOTON_IZQUIERDO)) {
            int mx = this.environment.mouseX();
            int my = this.environment.mouseY();
            
            // 1. Check Items
            Item clickedItem = this.itemManager.getClickedItem(mx, my);
            if (clickedItem != null) {
                this.cardBar.applyItemEffect(clickedItem.getType());
                this.itemManager.removeItem(clickedItem);
                this.resetSelection();
                return;
            }

            // 2. Check Cards
            PlantCard clickedCard = this.cardBar.getClickedCard(mx, my);
            if (clickedCard != null) { 
                this.selectedCard = clickedCard;
                this.cardBar.selectCard(this.selectedCard);
                if (this.movingPlant != null) {
                    this.movingPlant.setSelectedForMove(false);
                    this.movingPlant = null;
                }
            } 
            // 3. Check Board
            else if (this.board.isInsideBoard(mx, my)) { 
                Plant clickedPlant = this.plantManager.getClickedPlant(mx, my);
                if (this.movingPlant != null) {
                    this.movingPlant.setSelectedForMove(false);
                }
                this.movingPlant = clickedPlant;
                if (this.movingPlant != null) {
                    this.movingPlant.setSelectedForMove(true);
                    this.selectedCard = null;
                    this.cardBar.selectCard(null);
                }
            } 
            // 4. Clicked outside
            else { 
                this.resetSelection();
            }
        }

        if (this.environment.seLevantoBoton(environment.BOTON_IZQUIERDO)) {
            if (this.selectedCard != null) {
                int mx = this.environment.mouseX();
                int my = this.environment.mouseY();
                if (this.board.isInsideBoard(mx, my)) {
                    boolean planted = this.plantManager.plant(this.selectedCard, mx, my, this.board, this.graveManager); 
                    if (planted) {
                        this.selectedCard.startCooldown(this.environment);
                    }
                }
                this.selectedCard = null;
                this.cardBar.selectCard(null);
            }
        }
    }
    
    private void resetSelection() {
        this.selectedCard = null;
        this.cardBar.selectCard(null);
        if (this.movingPlant != null) {
            this.movingPlant.setSelectedForMove(false);
            this.movingPlant = null;
        }
    }

    private void handleKeyboard() {
        if (this.movingPlant == null) { return; }

        int currentRow = (int) this.movingPlant.getRow();
        int currentCol = (int) this.movingPlant.getColumn();
        int newRow = currentRow;
        int newCol = currentCol;
        boolean moved = false;

        if (environment.sePresiono(environment.TECLA_ARRIBA)) { newRow--; moved = true; }
        else if (environment.sePresiono(environment.TECLA_ABAJO)) { newRow++; moved = true; }
        else if (environment.sePresiono(environment.TECLA_IZQUIERDA)) { newCol--; moved = true; }
        else if (environment.sePresiono(environment.TECLA_DERECHA)) { newCol++; moved = true; }

        if (moved) {
            boolean insideLimits = newRow >= 0 && newRow < 5 && newCol >= 0 && newCol < 10;
            if (insideLimits) {
                if (newCol == 0) { return; } 
                if (this.plantManager.hasPlantIn(newRow, newCol)) { return; } 
                if (this.graveManager.getGraveInBox(newRow, newCol) != null) { return; } 

                this.movingPlant.setBox(newRow, newCol, this.board);
            }
        }
    }

    private void drawHUD() {
        environment.cambiarFont("Arial", 18, Color.WHITE);
        int x = 350;
        environment.escribirTexto("Time: " + this.gameSeconds + "s", x, 40);
        
        if (!this.bossSpawned) {
            int killed = this.zombieManager.getTotalKilled();
            int remaining = this.zombiesToWin - killed;
            if (remaining < 0) { remaining = 0; }
            
            environment.escribirTexto("Killed: " + killed, x, 65);
            environment.escribirTexto("Remaining for Boss: " + remaining, x, 90);
        } else {
            int killed = this.zombieManager.getTotalKilled();
            environment.escribirTexto("Killed: " + killed, x, 65);
            
            environment.cambiarFont("Arial", 20, Color.RED); 
            environment.escribirTexto("FINAL BOSS INCOMING!!", x, 90);
        }
    }
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Game game = new Game();
    }
}