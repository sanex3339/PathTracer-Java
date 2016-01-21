package PathTracer.renderer.Materials;

import PathTracer.interfaces.BaseSurface;
import PathTracer.renderer.*;

public abstract class AbstractMaterial implements BaseSurface {
    /**
     * RGB color 0..255
     */
    protected RGBColor color = RGBColor.GRAY;

    /**
     * Default constructor
     */
    protected AbstractMaterial () { }

    public AbstractMaterial(RGBColor color) {
        this.color = color;
    }

    /**
     * @param direction
     * @param normal
     * @return RGBColor - material BRDF
     */
    public abstract RGBColor getBRDF (Vector direction, Vector normal);

    /**
     * @param direction
     * @param normal
     * @return double - material PDF
     */
    public abstract double getPDF (Vector direction, Vector normal);

    /**
     * @return boolean
     */
    public abstract boolean isLightSource ();

    /**
     * @return RGBColor
     */
    public RGBColor getColor () {
        return this.color;
    }

    /**
     * @param ray
     * @param scene
     * @return RGBColor
     */
    public abstract RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene);
}
