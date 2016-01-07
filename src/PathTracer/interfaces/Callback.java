package PathTracer.interfaces;

import java.awt.*;
import java.util.List;

@FunctionalInterface
public interface Callback {
    void callback (List<Color> colors);
}