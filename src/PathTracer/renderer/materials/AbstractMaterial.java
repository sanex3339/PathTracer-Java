package PathTracer.renderer.materials;

import PathTracer.interfaces.BaseSurface;
import PathTracer.renderer.*;

public abstract class AbstractMaterial implements BaseSurface {
    /**
     * RGB color 0..255
     */
    protected PTColor surfaceColor = PTColor.GRAY;

    /**
     * Default constructor
     */
    protected AbstractMaterial () { }

    public AbstractMaterial (PTColor surfaceColor) {
        this.surfaceColor = surfaceColor;
    }

    /**
     * @param direction
     * @param normal
     * @return PTColor - material BRDF
     */
    public abstract PTColor getBRDF (Vector direction, Vector normal);

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
     * @return PTColor
     */
    public PTColor getSurfaceColor () {
        return this.surfaceColor;
    }

    /**
     * @param ray
     * @param scene
     * @return PTColor
     */
    public abstract PTColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene);
}
