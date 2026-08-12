package juego.GameUI;

/**
 * Idioma actual del juego. Las pantallas de UI consultan Lang.getCurrent()
 * en cada draw(), así que togglear el idioma actualiza los textos al toque,
 * sin necesidad de reiniciar nada.
 */
public enum Lang {
    ES,
    EN;

    private static Lang current = ES;

    public static Lang getCurrent() {
        return current;
    }

    public static void setCurrent(Lang lang) {
        current = lang;
    }

    public static void toggle() {
        current = (current == ES) ? EN : ES;
    }

    public String getDisplayName() {
        return this == ES ? "Español" : "English";
    }
}
