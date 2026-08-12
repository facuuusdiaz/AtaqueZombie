package juego.GameUI;

/**
 * Todos los textos de la interfaz (menús, HUD, pantallas) en español e inglés.
 * OJO: esto es solo para textos de interfaz. Los nombres de plantas/zombies
 * ("Rose Blade", "Wall-nut", etc.) NO están acá a propósito: el código los usa
 * como identificadores (switch en PlantManager, nombres de archivo de imagen),
 * así que traducirlos rompería esa lógica.
 */
public enum Text {

    // Menú principal
    TITLE("ZOMBIE SURVIVAL", "ZOMBIE SURVIVAL"),
    SUBTITLE("Defendé el jardín antes de que lleguen a los regalos", "Defend the garden before they reach the gifts"),
    PLAY("JUGAR", "PLAY"),
    INSTRUCTIONS("INSTRUCCIONES", "INSTRUCTIONS"),
    OPTIONS("OPCIONES", "OPTIONS"),
    EXIT("SALIR", "EXIT"),
    BACK("VOLVER", "BACK"),

    // Opciones
    OPTIONS_TITLE("OPCIONES", "OPTIONS"),
    LANGUAGE_LABEL("Idioma:", "Language:"),
    FULLSCREEN_LABEL("Pantalla completa: próximamente", "Fullscreen: coming soon"),

    // Instrucciones
    HOW_TO_PLAY("CÓMO JUGAR", "HOW TO PLAY"),
    INSTRUCTIONS_BODY(
        "- Click en una carta para seleccionar una planta.\n" +
        "- Click en el tablero para plantarla (no en la columna de regalos).\n" +
        "- Cada carta tiene un cooldown antes de poder volver a usarse.\n" +
        "- Wall-nut: mucha vida, no ataca. Sirve para bloquear el paso.\n" +
        "- Rose Blade: dispara automáticamente a los zombies en su fila.\n" +
        "- Rose-Bomba: al morir explota e inflige daño en área.\n" +
        "- Click en una planta ya puesta y usá las flechas para moverla.\n" +
        "- Las tumbas ocupan casilleros: hay que destruirlas a disparos.\n" +
        "- Los zombies pueden dejar pociones al morir (mejoran o\n" +
        "  empeoran tus cooldowns, ¡fijate cuál agarrás!).\n" +
        "- Sobreviví 50 zombies para que aparezca el Jefe Final.\n" +
        "- Si un zombie llega a los regalos, se termina el juego.",

        "- Click a card to select a plant.\n" +
        "- Click on the board to plant it (not in the gifts column).\n" +
        "- Each card has a cooldown before it can be used again.\n" +
        "- Wall-nut: lots of health, doesn't attack. Great for blocking.\n" +
        "- Rose Blade: automatically shoots zombies in its row.\n" +
        "- Rose-Bomba: explodes on death, dealing area damage.\n" +
        "- Click a placed plant and use the arrow keys to move it.\n" +
        "- Graves occupy tiles: you need to shoot them down.\n" +
        "- Zombies may drop potions when they die (they improve or\n" +
        "  worsen your cooldowns, watch which one you grab!).\n" +
        "- Survive 50 zombies to make the Final Boss appear.\n" +
        "- If a zombie reaches the gifts, the game is over."
    ),

    // Pausa
    PAUSE_TITLE("PAUSA", "PAUSED"),
    RESUME("REANUDAR", "RESUME"),
    RESTART("REINICIAR", "RESTART"),
    EXIT_TO_MENU("SALIR AL MENÚ", "EXIT TO MENU"),

    // Fin de partida
    YOU_LOST("¡PERDISTE!", "YOU LOST!"),
    YOU_WON("¡GANASTE!", "YOU WON!"),
    CLICK_TO_MENU("Click para volver al menú", "Click to return to menu"),

    // HUD
    MISSING_FOR_BOSS("Faltan %d para el Jefe", "%d left for the Boss"),
    FINAL_BOSS("¡JEFE FINAL!", "FINAL BOSS!"),
    KILLED_PREFIX("x ", "x ");

    private final String es;
    private final String en;

    Text(String es, String en) {
        this.es = es;
        this.en = en;
    }

    public String get() {
        return Lang.getCurrent() == Lang.ES ? this.es : this.en;
    }

    /**
     * Para textos con formato (ej. MISSING_FOR_BOSS con un número).
     */
    public String get(Object... args) {
        return String.format(this.get(), args);
    }
}
