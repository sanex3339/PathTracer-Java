package PathTracer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class RenderCanvas extends Canvas {
    public int canvasWidth = 300;
    public int canvasHeight = 300;

    private Color color = Color.RED;

    public RenderCanvas (int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        setBackground (Color.WHITE);
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

    public void update (Color color) {
        this.color = color;

        repaint();
    }
}
