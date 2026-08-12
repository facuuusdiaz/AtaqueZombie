package juego.Zombies;
import entorno.Entorno;
import juego.GameCore.Board;
import juego.Plants.PlantManager;

public class ZombieManager {

// 1. Attributes
    private static final int MAX_ZOMBIES = 50;
    private static final int MAX_PROJECTILES = 200;
    private static final int TIME_BETWEEN_ZOMBIES = 3000; // 3 seconds

    private Zombie[] zombiesInGame;
    private FinalBoss finalBoss;
    private ZombieProjectile[] projectiles;
    private Board board; // Si tradujiste Tablero a Board, cambialo acá

    private int nextZombieTime;
    private int zombiesKilled;
    private boolean spawnsStopped;

    // 2. Constructor
    public ZombieManager(Board board) {
        this.board = board;
        this.finalBoss = null;
        this.zombiesInGame = new Zombie[MAX_ZOMBIES];
        this.projectiles = new ZombieProjectile[MAX_PROJECTILES];
        this.zombiesKilled = 0;
        this.spawnsStopped = false;

        // El primer zombie aparece a los 5 segundos
        this.nextZombieTime = 5000; 
    }

    // 3. Core Logic (Tick)
    public void tick(Entorno e, Board board, PlantManager plantManager) {
        // Genera un nuevo zombie si ha pasado el tiempo
        if (e.tiempo() >= this.nextZombieTime && !this.spawnsStopped) {
            this.spawnZombie(this.board); 
            this.nextZombieTime = e.tiempo() + TIME_BETWEEN_ZOMBIES;
        }
        
        // Actualiza el estado de cada zombie y genera sus proyectiles
        for (int i = 0; i < this.zombiesInGame.length; i++) {
            if (this.zombiesInGame[i] != null) {
                // Gracias al polimorfismo, el tick es igual para todos
                ZombieProjectile p = this.zombiesInGame[i].tick(e, plantManager);
                if (p != null) {
                    this.addProjectile(p);
                }
            }
        }

        // Mueve los proyectiles de los zombies
        for (int i = 0; i < this.projectiles.length; i++) {
            if (this.projectiles[i] != null) {
                this.projectiles[i].move();
                if (this.projectiles[i].getX() < 0) { // Si sale de pantalla
                    this.projectiles[i] = null;
                }
            }
        }
    }

    // 4. Actions
    private void spawnZombie(Board board) {
        int randomRow = (int) (Math.random() * 5);
        double y = board.getCenterBox(randomRow, 0)[1]; // Cambiar a getCenterBox si lo tradujiste
        double x = 850.0; // Fuera de pantalla
        
        Zombie newZombie;
        double chance = Math.random();
        
        // Magia del polimorfismo: instanciamos directamente al hijo que corresponde
        if (chance < 0.7) { 
            newZombie = new NormalZombie(x, y);
        } else { 
            newZombie = new FastZombie(x, y);
        }
        this.addZombie(newZombie);
    }

    private boolean addZombie(Zombie zombie) {
        for (int i = 0; i < this.zombiesInGame.length; i++){
            if (this.zombiesInGame[i] == null) {
                this.zombiesInGame[i] = zombie;
                return true;
            }
        }
        return false;
    }

    public FinalBoss spawnFinalBoss(Board board) {
        double yCenterPlayArea = board.getMenuHeight() + (board.getBoxHeight() * board.getRows() / 2.0);
        double playAreaHeight = board.getScreenHeight() - board.getMenuHeight();
        double x = 850.0; 

        // Creamos al jefe usando su propia clase
        FinalBoss boss = new FinalBoss(x, yCenterPlayArea, playAreaHeight);
        
        this.addZombie(boss);
        return boss;
    }

    public void removeZombie(int index) {
        if (index >= 0 && index < this.zombiesInGame.length) {
            if (this.zombiesInGame[index] != null) {
                this.zombiesInGame[index] = null;
                this.zombiesKilled++;
            }
        }
    }

    public void stopSpawns() {
        this.spawnsStopped = true;
    }

    private void addProjectile(ZombieProjectile p) {
        for (int i = 0; i < this.projectiles.length; i++) {
            if (this.projectiles[i] == null) {
                this.projectiles[i] = p;
                return;
            }
        }
    }

    public void removeProjectile(int index) {
        if (index >= 0 && index < this.projectiles.length) {
            this.projectiles[index] = null;
        }
    }

    // 5. Rendering
    public void draw(Entorno e) {
        for (int i = 0; i < this.zombiesInGame.length; i++) {
            if (this.zombiesInGame[i] != null && this.zombiesInGame[i].isAlive()) {
                this.zombiesInGame[i].draw(e);
            }
        }
        for (int i = 0; i < this.projectiles.length; i++) {
            if (this.projectiles[i] != null) {
                this.projectiles[i].draw(e);
            }
        }
    }

    // 6. Utility
    public boolean hasZombieInRow(double rowY, double limitX) {
        for (int i = 0; i < this.zombiesInGame.length; i++) {
            Zombie z = this.zombiesInGame[i];
            if (z != null && z.isAlive()) { 
                if (this.finalBoss != null && z == this.finalBoss && z.getX() <= limitX) {
                    return true; 
                }
                else if (z != this.finalBoss && z.getY() == rowY && z.getX() <= limitX) {
                    return true;
                }
            }
        }
        return false;
    }

    // 7. Getters & Setters
    public Zombie[] getZombies() { return this.zombiesInGame; }
    public ZombieProjectile[] getProjectiles() { return this.projectiles; }
    public int getTotalKilled() { return this.zombiesKilled; }
    public void setFinalBoss(FinalBoss boss) { this.finalBoss = boss; }
}