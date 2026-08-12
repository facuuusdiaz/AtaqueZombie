package juego.Plants;

import entorno.Entorno;
import entorno.Herramientas;
import juego.Objets.Shot;
import juego.Zombies.FinalBoss;
import juego.Zombies.ZombieManager;

public class RoseBomba extends Plant {

public RoseBomba(double centerBox, double centerBox2, int row, int column) {
        super(centerBox, centerBox2, row, column,"Rose-Bomba", 1);
        this.image = Herramientas.cargarImagen("Rose-Bombaa.png"); // Nombre corregido
    }

    @Override
    public Shot tick(Entorno e, ZombieManager zombieManager, FinalBoss jefeFinal) {
        // Rose Bomba does not shoot, so return null
        return null;
    }

    @Override
    public void draw(Entorno e) {
        super.draw(e, 0.1); // Llama al draw de la clase padre
    }
}