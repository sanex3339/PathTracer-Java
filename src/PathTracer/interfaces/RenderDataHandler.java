package PathTracer.interfaces;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@FunctionalInterface
public interface RenderDataHandler {
    void callback (Future<List<Color>> thread) throws ExecutionException, InterruptedException;
}