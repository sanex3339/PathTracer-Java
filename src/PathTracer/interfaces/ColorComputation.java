package PathTracer.interfaces;

import PathTracer.renderer.RGBColor;

public interface ColorComputation <T extends BaseSurface> {
    RGBColor calculateColor ();
}
