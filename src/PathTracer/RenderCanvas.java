package PathTracer;

import java.awt.*;
import java.util.List;

final public class RenderCanvas extends Canvas {
    private FrameRenderer frameRenderer;

    public RenderCanvas (int canvasWidth, int canvasHeight) {
        this.setSize(new Dimension(canvasWidth, canvasHeight));

        this.frameRenderer = new FrameRenderer(this);
    }

    @Override
    public void paint (Graphics g) {
        super.paint(g);

        this.createBufferStrategy(2);
    }

    /**
     * Update canvas with calculated colors
     */
    public void update (List<Color> colors) {
        this.frameRenderer.drawColors(colors);
    }

    /**
     * Saving canvas data to file
     */
    public void saveToFile () {
        this.frameRenderer.saveToFile();
    }
}
