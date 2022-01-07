package PathTracer.renderer.materials;

import PathTracer.interfaces.BaseSurface;
import PathTracer.renderer.*;
import mikera.vectorz.Vector3;

public abstract class AbstractMaterial implements BaseSurface {
    /**
     * RGB color 0..255
     */
    protected RGBColor surfaceColor = RGBColor.GRAY;

    /**
     * Default constructor
     */
    protected AbstractMaterial () { }

    public AbstractMaterial (RGBColor surfaceColor) {
        this.surfaceColor = surfaceColor;
    }

    /**
     * @param direction
     * @param normal
     * @return RGBColor - material BRDF
     */
    public abstract RGBColor getBRDF (Vector3 direction, Vector3 normal);

    /**
     * @param direction
     * @param normal
     * @return double - material PDF
     */
    public abstract double getPDF (Vector3 direction, Vector3 normal);

    /**
     * @return boolean
     */
    public abstract boolean isLightSource ();

    /**
     * @return RGBColor
     */
    public RGBColor getSurfaceColor () {
        return this.surfaceColor;
    }

    /**
     * @param ray
     * @param scene
     * @return RGBColor
     */
    public abstract RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene);
}
