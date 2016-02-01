package PathTracer.interfaces;

import PathTracer.renderer.*;

public interface BaseSurface {
    /**
     * @param ray
     * @param intersection
     * @param scene
     * @return PTColor
     */
    PTColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene);

    /**
     * @param direction
     * @param normal
     * @return PTColor
     */
    PTColor getBRDF (Vector direction, Vector normal);

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
     * @return PTColor
     */
    PTColor getSurfaceColor ();
}
