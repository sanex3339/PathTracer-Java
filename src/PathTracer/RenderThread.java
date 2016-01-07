package PathTracer;

import PathTracer.renderer.Tracer;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Callable;

final public class RenderThread implements Callable<List<Color>> {
    private int screenWidth;
    private int screenHeight;

    public RenderThread (int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public List<Color> call () {
        return new Tracer(this.screenWidth, screenHeight).render();
    }
}
