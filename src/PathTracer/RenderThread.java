package PathTracer;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.Callable;

public class RenderThread implements Callable<Color> {
    private int sample;

    public RenderThread (int sample) {
        this.sample = sample;
    }

    @Override
    public Color call() throws InterruptedException {
        Random random = new Random();

        Thread.sleep(500);

        return new Color(
            random.nextInt(255),
            random.nextInt(255),
            random.nextInt(255)
        );
    }
}
