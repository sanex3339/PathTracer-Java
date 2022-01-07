package PathTracer.renderer;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomGenerator {
    public static Double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
