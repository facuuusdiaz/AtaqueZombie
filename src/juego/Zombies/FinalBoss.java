package juego.Zombies;

import entorno.Entorno;
import juego.Plants.PlantManager;

public class FinalBoss extends Zombie {

    public FinalBoss(double x, double yCenterPlayArea, double playAreaHeight) {
        super(x, yCenterPlayArea, 0.3, 500, "ZombieFinall.png", 100, playAreaHeight, 1.0, 200);
    }


    @Override
    public ZombieProjectile tick(Entorno e, PlantManager plantManager) {
        if (!this.isAlive()) { return null; }
        
        this.move();
        return null; // The final boss does not shoot projectiles in this implementation
        
    }
    
}
