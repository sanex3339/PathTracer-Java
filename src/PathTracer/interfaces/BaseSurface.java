package PathTracer.interfaces;

import PathTracer.renderer.*;

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
    RGBColor getBRDF (Vector direction, Vector normal);

    /**
     * @param direction
     * @param normal
     * @return double
     */
    double getPDF (Vector direction, Vector normal);

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
