package juego.Plants;

import entorno.Entorno;
import entorno.Herramientas;
import juego.Objets.Shot;
import juego.Zombies.FinalBoss;
import juego.Zombies.ZombieManager;

public class RoseBlade extends Plant {

    private int delayShoot = 2000;
    private long nextShoot = 0;

public RoseBlade(double centerBox, double centerBox2, int row, int column) {
        super(centerBox, centerBox2, row, column,"Rose Blade", 100);
        this.image = Herramientas.cargarImagen("RoseBlade.png"); // Nombre corregido
    }

    @Override
    public Shot tick(Entorno e, ZombieManager zombieManager, FinalBoss jefeFinal) {
        if (this.nextShoot == 0) {
            this.nextShoot = e.tiempo() + this.delayShoot;
        }

        if (this.isAlive && e.tiempo() >= this.nextShoot) {
            boolean bossIsAlive = (jefeFinal != null && jefeFinal.isAlive());
            boolean standardThreat = zombieManager.hasZombieInRow(this.y, 800.0);

            if (bossIsAlive || standardThreat) {
                this.nextShoot = e.tiempo() + this.delayShoot;
                return new Shot(this.x, this.y);
            }
        }
        return null;
    }

    @Override
    public void draw(Entorno e) {
        super.draw(e, 0.2); // Llama al draw de la clase padre
    }
}