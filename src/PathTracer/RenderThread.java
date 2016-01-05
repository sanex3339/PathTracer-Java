package PathTracer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

final public class RenderThread <T> implements Callable<Map<String, T>> {
    private int sample;

    /**
     * @param sample sample value
     */
    public RenderThread (int sample) {
        this.sample = sample;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, T> call () throws InterruptedException {
        Random random = new Random();

        Thread.sleep(500);

        Map<String, T> data = new HashMap<>();
        data.put(
            "color",
            (T) new Color(
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
            )
        );

        return data;
    }
}
