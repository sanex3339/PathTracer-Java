package PathTracer.interfaces;

import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Vector;

public interface BaseSurface {
    RGBColor getBRDF (Vector direction, Vector normal);
    double getPDF (Vector direction, Vector normal);
    boolean isLightSource ();
    RGBColor getColor ();
}
