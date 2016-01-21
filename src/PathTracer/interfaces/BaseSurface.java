package PathTracer.interfaces;

import PathTracer.renderer.*;

public interface BaseSurface {
    RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene);
    RGBColor getBRDF (Vector direction, Vector normal);
    double getPDF (Vector direction, Vector normal);
    boolean isLightSource ();
    RGBColor getColor ();
}
