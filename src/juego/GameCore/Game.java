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

    // --- Estado del juego y pantallas de UI ---
    private GameState state;
    private MainMenu mainMenu;
    private InstructionsScreen instructionsScreen;
    private OptionsMenu optionsMenu;
    private PauseMenu pauseMenu;
    private HUD hud;

    public Game() {
        this.environment = new Entorno(this, "Zombie Survival", 800, 600);

        this.board = new Board(this.environment);
        this.cardBar = new PlantCardBar();
        this.zombieBar = new ZombieBar();

        this.mainMenu = new MainMenu();
        this.instructionsScreen = new InstructionsScreen();
        this.optionsMenu = new OptionsMenu();
        this.pauseMenu = new PauseMenu();
        this.hud = new HUD();
        this.state = GameState.MENU;

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
        switch (this.state) {
            case MENU:
                this.tickMenu();
                break;
            case INSTRUCTIONS:
                this.tickInstructions();
                break;
            case OPTIONS:
                this.tickOptions();
                break;
            case PLAYING:
                this.tickPlaying();
                break;
            case PAUSED:
                this.tickPaused();
                break;
            case GAME_OVER:
                this.tickGameOver();
                break;
            case WON:
                this.tickWon();
                break;
        }
    }

    // ---------------- MENÚ PRINCIPAL ----------------
    private void tickMenu() {
        this.mainMenu.draw(this.environment);

        if (this.environment.sePresionoBoton(environment.BOTON_IZQUIERDO)) {
            int mx = this.environment.mouseX();
            int my = this.environment.mouseY();
            String option = this.mainMenu.getClickedOption(mx, my);

            if ("PLAY".equals(option)) {
                this.startNewGame();
                this.state = GameState.PLAYING;
            } else if ("INSTRUCTIONS".equals(option)) {
                this.state = GameState.INSTRUCTIONS;
            } else if ("OPTIONS".equals(option)) {
                this.state = GameState.OPTIONS;
            } else if ("EXIT".equals(option)) {
                System.exit(0);
            }
        }
    }

    // ---------------- INSTRUCCIONES ----------------
    private void tickInstructions() {
        this.instructionsScreen.draw(this.environment);

        if (this.environment.sePresionoBoton(environment.BOTON_IZQUIERDO)) {
            int mx = this.environment.mouseX();
            int my = this.environment.mouseY();
            if (this.instructionsScreen.clickedBack(mx, my)) {
                this.state = GameState.MENU;
            }
        }
    }

    // ---------------- OPCIONES ----------------
    private void tickOptions() {
        this.optionsMenu.draw(this.environment);

        if (this.environment.sePresionoBoton(environment.BOTON_IZQUIERDO)) {
            int mx = this.environment.mouseX();
            int my = this.environment.mouseY();
            this.optionsMenu.handleClick(mx, my);
            if (this.optionsMenu.clickedBack(mx, my)) {
                this.state = GameState.MENU;
            }
        }
    }

    // ---------------- PAUSA ----------------
    private void tickPaused() {
        // Dibuja el juego "congelado" debajo del overlay de pausa
        this.drawGameWorld();
        this.pauseMenu.draw(this.environment);

        if (this.environment.sePresiono(environment.TECLA_ESCAPE)) {
            this.state = GameState.PLAYING;
            return;
        }

        if (this.environment.sePresionoBoton(environment.BOTON_IZQUIERDO)) {
            int mx = this.environment.mouseX();
            int my = this.environment.mouseY();
            String option = this.pauseMenu.getClickedOption(mx, my);

            if ("RESUME".equals(option)) {
                this.state = GameState.PLAYING;
            } else if ("RESTART".equals(option)) {
                this.startNewGame();
                this.state = GameState.PLAYING;
            } else if ("EXIT".equals(option)) {
                this.state = GameState.MENU;
            }
        }
    }

    // ---------------- GAME OVER / GANASTE ----------------
    private void tickGameOver() {
        this.drawGameWorld();

        environment.cambiarFont("Arial", 50, Color.RED);
        environment.escribirTexto(Text.YOU_LOST.get(), 280, 300);

        environment.cambiarFont("Arial", 16, Color.WHITE);
        environment.escribirTexto(Text.CLICK_TO_MENU.get(), 295, 340);

        if (this.environment.sePresionoBoton(environment.BOTON_IZQUIERDO)) {
            this.state = GameState.MENU;
        }
    }

    private void tickWon() {
        this.drawGameWorld();

        environment.cambiarFont("Arial", 50, Color.GREEN);
        environment.escribirTexto(Text.YOU_WON.get(), 280, 300);

        environment.cambiarFont("Arial", 16, Color.WHITE);
        environment.escribirTexto(Text.CLICK_TO_MENU.get(), 295, 340);

        if (this.environment.sePresionoBoton(environment.BOTON_IZQUIERDO)) {
            this.state = GameState.MENU;
        }
    }

    // ---------------- JUEGO EN CURSO ----------------
    private void tickPlaying() {

        if (this.environment.sePresiono(environment.TECLA_ESCAPE)) {
            this.state = GameState.PAUSED;
            return;
        }

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
            this.state = GameState.WON;
            return;
        }

        if (this.isGameOver) {
            this.state = GameState.GAME_OVER;
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
        this.drawGameWorld();
    }

    /**
     * Dibuja todo el mundo del juego: tablero, HUD, cartas, plantas, zombies, etc.
     * Se usa tanto en PLAYING como (congelado, sin lógica) en PAUSED / GAME_OVER / WON.
     */
    private void drawGameWorld() {
        this.board.drawBoard();

        int remaining = this.zombiesToWin - this.zombieManager.getTotalKilled();
        if (remaining < 0) { remaining = 0; }

        this.hud.draw(this.environment, this.gameSeconds, this.zombieManager.getTotalKilled(),
                remaining, this.bossSpawned);

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

    /**
     * Reinicia todo el estado de la partida (usado en "Jugar" y en "Reiniciar" desde pausa).
     */
    private void startNewGame() {
        this.cardBar = new PlantCardBar();
        this.zombieBar = new ZombieBar();

        this.selectedCard = null;
        this.movingPlant = null;
        this.plantManager = new PlantManager();
        this.zombieManager = new ZombieManager(this.board);
        this.graveManager = new GraveManager(this.board);
        this.itemManager = new ItemManager();

        this.isGameOver = false;
        this.isGameWon = false;
        this.bossSpawned = false;
        this.finalBoss = null;

        this.tickCounter = 0;
        this.gameSeconds = 0;
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

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Game game = new Game();
    }
}
