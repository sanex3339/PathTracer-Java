package PathTracer.interfaces;

import PathTracer.renderer.*;
import mikera.vectorz.Vector3;

public interface BaseSurface {
    /**
     * @param ray
     * @param intersection
     * @param scene
     * @return RGBColor
     */
    RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene);

    /**
     * @param direction
     * @param normal
     * @return RGBColor
     */
    RGBColor getBRDF (Vector3 direction, Vector3 normal);

    /**
     * @param direction
     * @param normal
     * @return double
     */
    double getPDF (Vector3 direction, Vector3 normal);

    /**
     * @return boolean
     */
    boolean isLightSource ();

    /**
     * Return raw surface color without any mixing with lighting or global illumination
     *
     * @return RGBColor
     */
    RGBColor getSurfaceColor ();
}
