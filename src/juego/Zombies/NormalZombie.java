package juego.Zombies;

public class NormalZombie extends Zombie {

    public NormalZombie(double x, double y) {
        // Cuidado con el ancho y alto (antes decía 1, 1). Lo cambiamos a 50, 50
        super(x, y, 0.5, 100, "ZombieNormal.png", 50, 50, 0.05, 100);
        
        // Stats específicas de este zombie:
        this.attackDamage = 15; // Quita 15 de vida por ataque
        this.attackSpeed = 60;  // Tarda 1 segundo (60 frames) en morder
    }
}