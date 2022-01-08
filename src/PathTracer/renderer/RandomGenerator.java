package PathTracer.renderer;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomGenerator {
    public static Double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static Double getRandomDouble(int min, int max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
