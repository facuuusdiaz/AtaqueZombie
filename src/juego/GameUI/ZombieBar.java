package juego.GameUI;

import entorno.Entorno;

public class ZombieBar {
    
    private ZombieCard[] zombieDeck;

    public ZombieBar() {
        this.zombieDeck = new ZombieCard[2]; 

        double y = 60; 
        double xNormalZombie = 800 - 60;  
        double xFastZombie = 800 - 160; 
        
        this.zombieDeck[0] = new ZombieCard(xNormalZombie, y, "Normal", "ZombieNormal.png", 0.08); 
        this.zombieDeck[1] = new ZombieCard(xFastZombie, y, "Fast", "Grinchh.png", 0.075); 
    }

    public void draw(Entorno environment) {
        for (ZombieCard card : this.zombieDeck) {
            if (card != null) {
                card.draw(environment);
            }
        }
    }
}