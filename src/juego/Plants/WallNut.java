package juego.Plants;

import entorno.Entorno;
import entorno.Herramientas;
import juego.Objets.Shot;
import juego.Zombies.FinalBoss;
import juego.Zombies.ZombieManager;

public class WallNut extends Plant {

    public WallNut(double x, double y, int row, int column) {
        super(x, y, row, column,"Wall Nut", 1000); // 1000 de vida
        this.image = Herramientas.cargarImagen("Wall-nutt.png"); // Nombre corregido
    }

    @Override
    public Shot tick(Entorno e, ZombieManager zombieManager, FinalBoss jefeFinal) {
        // Wall Nut does not shoot, so return null
        return null;
    }

    @Override
    public void draw(Entorno e) {
        super.draw(e, 0.09); // Llama al draw de la clase padre
    }
}
