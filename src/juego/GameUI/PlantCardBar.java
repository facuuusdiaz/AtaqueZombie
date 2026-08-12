package juego.GameUI;

import entorno.Entorno;

public class PlantCardBar {

    private PlantCard[] deck;
    
    public PlantCardBar() {
        this.deck = new PlantCard[3];
        this.deck[0] = new PlantCard (60.0, 60.0, "Rose Blade");
        this.deck[1] = new PlantCard (160.0, 60.0, "Wall-nut");
        this.deck[2] = new PlantCard (260.0, 60.0, "Rose-Bomba");
    }
    
    public void tick (Entorno environment) {
        for (PlantCard card : this.deck) {
            if (card != null) {
                card.tick(environment);
            }
        }
    }
    
    public void draw (Entorno environment) {
        for (PlantCard card : this.deck) {
            if (card != null) {
                card.draw(environment);
            }
        }
    }
    
    public PlantCard getClickedCard(int mx, int my) {
        for (PlantCard card : this.deck) {
            // Checks if clicked AND if ready (cooldown finished)
            if (card != null && card.isClicked(mx, my) && card.isReady()) {
                return card;
            }
        }
        return null; 
    }
    
    public void selectCard(PlantCard selectedCard) {
        for (PlantCard card : this.deck) {
            if (card != null) {
                card.setSelected(card == selectedCard);
            }
        }
    }
    
    public void applyItemEffect(String type) {
        for (PlantCard card : this.deck) {
            if (card != null) {
                card.modifyCooldown(type);
            }
        }
    }
}