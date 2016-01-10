package PathTracer;

import PathTracer.renderer.Scene;
import PathTracer.renderer.Tracer;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Callable;

final public class RenderThread implements Callable<List<Color>> {
    private int screenWidth;
    private int screenHeight;

    private Scene scene;

    public RenderThread (int screenWidth, int screenHeight, Scene scene) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scene = scene;
    }

    @Override
    public List<Color> call () {
        return new Tracer(this.screenWidth, screenHeight, scene).render();
    }
}
