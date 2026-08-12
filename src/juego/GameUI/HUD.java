package juego.GameUI;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

/**
 * HUD visual con panel de fondo, íconos y barra de progreso hacia el jefe final.
 * Si las imágenes de los íconos no existen en tu carpeta de recursos,
 * cargarImagen() devuelve null y el HUD dibuja círculos de color como respaldo
 * (no rompe el juego).
 */
public class HUD {

    private Image clockIcon;
    private Image skullIcon;
    private Image bossIcon;

    public HUD() {
        // Opcional: si tenés estos archivos en tu carpeta de imágenes se van a usar,
        // si no, el HUD dibuja un placeholder automáticamente (safeLoad evita que
        // el juego crashee si Herramientas.cargarImagen no encuentra el archivo).
        this.clockIcon = safeLoad("Reloj.png");
        this.skullIcon = safeLoad("Calavera.png");
        this.bossIcon = safeLoad("BossIcon.png");
    }

    /**
     * Carga una imagen de forma segura. Herramientas.cargarImagen tira
     * NullPointerException si el archivo no existe (en vez de devolver null),
     * así que acá lo atajamos y devolvemos null para usar el placeholder.
     */
    private Image safeLoad(String filename) {
        try {
            return Herramientas.cargarImagen(filename);
        } catch (Exception ex) {
            return null;
        }
    }

    public void draw(Entorno e, int gameSeconds, int killed, int remaining, boolean bossSpawned) {
        double panelX = 400;
        double panelY = 62;
        double panelWidth = 400;
        double panelHeight = 90;

        // Panel de fondo
        e.dibujarRectangulo(panelX, panelY, panelWidth, panelHeight, 0, new Color(18, 18, 18, 210));
        e.dibujarRectangulo(panelX, panelY, panelWidth, panelHeight, 0, new Color(100, 100, 100));

        // --- Tiempo ---
        drawIconOrPlaceholder(e, this.clockIcon, panelX - 160, panelY - 12, Color.CYAN);
        e.cambiarFont("Arial", 16, Color.WHITE);
        e.escribirTexto(this.formatTime(gameSeconds), panelX - 178, panelY + 22);

        // --- Muertes ---
        drawIconOrPlaceholder(e, this.skullIcon, panelX - 30, panelY - 12, Color.LIGHT_GRAY);
        e.cambiarFont("Arial", 16, Color.WHITE);
        e.escribirTexto("x " + killed, panelX - 15, panelY + 22);

        // --- Estado del Boss / Restantes ---
        if (!bossSpawned) {
            e.cambiarFont("Arial", 13, Color.ORANGE);
            e.escribirTexto("Faltan " + remaining + " para el Jefe", panelX + 55, panelY - 8);

            // Barra de progreso hacia el boss
            double barMaxWidth = 150;
            int total = killed + remaining;
            double progress = total > 0 ? (double) killed / (double) total : 0.0;
            double barWidth = barMaxWidth * progress;

            double barCenterXBase = panelX + 55;
            double barY = panelY + 22;

            e.dibujarRectangulo(barCenterXBase + barMaxWidth / 2.0, barY, barMaxWidth, 12, 0, Color.DARK_GRAY);
            if (barWidth > 0) {
                e.dibujarRectangulo(barCenterXBase + barWidth / 2.0, barY, barWidth, 12, 0, new Color(200, 40, 40));
            }
        } else {
            drawIconOrPlaceholder(e, this.bossIcon, panelX + 100, panelY - 5, Color.RED);
            e.cambiarFont("Arial", 17, Color.RED);
            e.escribirTexto("¡JEFE FINAL!", panelX + 40, panelY + 25);
        }
    }

    private void drawIconOrPlaceholder(Entorno e, Image icon, double x, double y, Color fallbackColor) {
        if (icon != null) {
            e.dibujarImagen(icon, x, y, 0, 0.05);
        } else {
            e.dibujarCirculo(x, y, 10, fallbackColor);
        }
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
