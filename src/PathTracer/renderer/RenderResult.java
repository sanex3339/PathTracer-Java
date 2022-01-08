package PathTracer.renderer;

import java.awt.*;
import java.util.List;

public record RenderResult (
    /**
     * calculated pixel colors from Tracer
     */
    List<Color> colors,

    /**
     * render time for sample
     */
    int renderTime
) {
}
