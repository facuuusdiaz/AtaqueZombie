package juego.GameCore;

import entorno.Entorno;
import juego.Plants.Plant;
import juego.Plants.PlantManager;
import juego.Zombies.Zombie;
import juego.Zombies.ZombieManager;
import juego.Zombies.ZombieProjectile;
import juego.Zombies.FinalBoss;
import juego.Objets.Grave;
import juego.Objets.GraveManager;
import juego.Objets.ItemManager;
import juego.Objets.Shot;

public class Collisions {

    public boolean check(PlantManager plantManager, ZombieManager zombieManager, Board board, FinalBoss finalBoss, GraveManager graveManager, ItemManager itemManager, double giftX, Entorno environment) {

        Zombie[] zombies = zombieManager.getZombies();
        Plant[] plants = plantManager.getPlants();
        Shot[] shots = plantManager.getShots();
        ZombieProjectile[] zombieProjectiles = zombieManager.getProjectiles();
        Grave[] graves = graveManager.getGraves();

        // 1. ZOMBIE LOOP
        for (int i = 0; i < zombies.length; i++) {
            Zombie z = zombies[i];
            if (z == null || !z.isAlive()) { continue; }

            // 1.1 ZOMBIE VS GIFT (Game Over)
            if (z.getX() <= giftX) {
                return true; 
            }

            // 1.2 ZOMBIE VS PLANT (Bite)
            boolean isTouchingPlant = false;
            for (int j = 0; j < plants.length; j++) {
                Plant p = plants[j];
                if (p == null || !p.isAlive()) { continue; }

                if (z.isColliding(p.getX(), p.getY(), 40, 40)) {
                    isTouchingPlant = true;
                    z.setAttacking(true);
                    p.receiveDamage(); 
                    
                    if (!p.isAlive()) { 
                        // If it was a bomb, explode!
                        if (p.getName().equals("Rose-Bomba")) {
                            this.explode(p.getRow(), p.getColumn(), zombieManager, graves, board, finalBoss);
                        }
                        plantManager.removePlant(j); 
                    }
                    break;
                }
            }
            if (!isTouchingPlant) {
                z.setAttacking(false);
            }

            // 1.3 SHOT (PLANT) VS ZOMBIE
            for (int k = 0; k < shots.length; k++) {
                Shot s = shots[k];
                if (s == null) { continue; }

                if (z.isColliding(s.getX(), s.getY(), s.getWidth(), s.getHeight())) {
                    z.receiveDamage();
                    plantManager.removeShot(k);
                    
                    if (!z.isAlive()) { 
                        if (finalBoss != null && z == finalBoss) {
                            // Boss Loot
                            itemManager.addItem(z.getX(), z.getY());
                            itemManager.addItem(z.getX() - 40, z.getY());
                            itemManager.addItem(z.getX() + 40, z.getY());
                            itemManager.addItem(z.getX(), z.getY() - 40);
                            itemManager.addItem(z.getX(), z.getY() + 40);
                        } else {
                            // Normal Zombie Loot / Grave spawn
                            double GRAVE_CHANCE = 0.45; 
                            if (Math.random() < GRAVE_CHANCE) {
                                int[] box = board.getBox((int)z.getX(), (int)z.getY());
                                if (box != null) {
                                    int row = box[0];
                                    int col = box[1];
                                    if (!plantManager.hasPlantIn(row, col)) {
                                        graveManager.addGrave(row, col);
                                    }
                                }
                            }
                            double ITEM_CHANCE = 0.50; 
                            if (Math.random() < ITEM_CHANCE) {
                                itemManager.addItem(z.getX(), z.getY());
                            }
                        }
                        zombieManager.removeZombie(i); 
                    }
                    break;
                }
            }
        } 

        // 2. ZOMBIE PROJECTILE VS PLANT
        for (int k = 0; k < zombieProjectiles.length; k++) {
            ZombieProjectile zp = zombieProjectiles[k];
            if (zp == null) { continue; }

            for (int j = 0; j < plants.length; j++) {
                Plant p = plants[j];
                if (p == null || !p.isAlive()) { continue; }

                if (p.itIsColision(zp.getX(), zp.getY(), zp.getDiameter(), zp.getDiameter())) {
                    p.receiveShootDamage();
                    zombieManager.removeProjectile(k);
                    
                    if (!p.isAlive()) {
                        if (p.getName().equals("Rose-Bomba")) {
                            this.explode(p.getRow(), p.getColumn(), zombieManager, graves, board, finalBoss);
                        }
                        plantManager.removePlant(j);
                    }
                    break;
                }
            }
        }

        // 3. SHOT (PLANT) VS GRAVE
        for (int k = 0; k < shots.length; k++) {
            Shot s = shots[k];
            if (s == null) { continue; }

            for (int tIdx = 0; tIdx < graves.length; tIdx++) {
                Grave g = graves[tIdx];
                if (g == null || !g.isAlive()) { continue; }

                if (g.isColliding(s.getX(), s.getY(), s.getWidth(), s.getHeight())) {
                    g.receiveDamage();
                    plantManager.removeShot(k);
                    if (!g.isAlive()) {
                        graveManager.removeGrave(tIdx);
                    }
                }
            }
        }

        return false; 
    }
    
    private void explode(int centerRow, int centerCol, ZombieManager zombieManager, Grave[] graves, Board board, FinalBoss finalBoss) {
        // Explodes in a 3x3 grid
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r < 0 || r >= 5 || c < 0 || c >= 10) { continue; }

                double[] centerBox = board.getCenterBox(r, c);
                if (centerBox == null) { continue; }
                
                double boxX = centerBox[0];
                double boxY = centerBox[1];
                double boxWidth = board.getBoxWidth();
                double boxHeight = board.getBoxHeight();
                int explosionDamage = 1000;

                // Hit Zombies
                Zombie[] zombies = zombieManager.getZombies(); 
                for (int i = 0; i < zombies.length; i++) { 
                    Zombie z = zombies[i];
                    if (z != null && z == finalBoss) { continue; } // Boss is immune
                    
                    if (z != null && z.isAlive()) {
                        if (z.isColliding(boxX, boxY, boxWidth, boxHeight)) {
                            z.receiveDamage(explosionDamage); 
                            zombieManager.removeZombie(i);
                        }
                    }
                }
                
                // Hit Graves
                for (Grave g : graves) {
                    if (g != null && g.isAlive()) {
                        if (g.getRow() == r && g.getColumn() == c) {
                            g.receiveDamage(explosionDamage); 
                        }
                    }
                }
            }
        }
    }
}
