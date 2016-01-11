package PathTracer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

final public class RenderCanvas extends Canvas implements Runnable {
    /**
     * Canvas width
     */
    public int canvasWidth = 300;

    /**
     * Canvas height
     */
    public int canvasHeight = 300;


    /**
     * Collection of calculated colors for each pixel
     */
    private List<Color> colors;

    /**
     * Default (init) color
     */
    private Color color = Color.WHITE;

    public RenderCanvas (int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    @Override
    public void paint (Graphics g) {
        for (int y = 0; y < this.canvasHeight; y++) {
            for (int x = 0; x < this.canvasWidth; x++) {
                g.setColor(this.color);
                g.fillRect(x, y, 1, 1);
            }
        }
    }

    public void draw (List<Color> colors) {
        this.colors = colors;

        Thread thread = new Thread(this, "redrawCanvas");
        thread.start();
    }


    /**
     * Update canvas with calculated colors
     */
    @Override
    public void run() {
        Graphics g = this.getGraphics();

        int i = 0;

        for (int y = 0; y < this.canvasHeight; y++) {
            for (int x = 0; x < this.canvasWidth; x++) {
                g.setColor(this.colors.get(i));
                g.fillRect(x, y, 1, 1);

                i++;
            }
        }
    }
}
