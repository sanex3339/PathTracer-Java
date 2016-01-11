package PathTracer.interfaces;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Callable;

@FunctionalInterface
public interface RenderDataProvider {
    Callable<List<Color>> callback();
}