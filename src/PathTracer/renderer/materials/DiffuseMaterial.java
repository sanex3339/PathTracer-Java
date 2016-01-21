package PathTracer.renderer.materials;

import PathTracer.interfaces.BaseSurface;
import PathTracer.renderer.*;
import PathTracer.renderer.colorComputation.*;

public class DiffuseMaterial extends AbstractMaterial {
    public DiffuseMaterial (RGBColor surfaceColor) {
        super(surfaceColor);
    }

    /**
     * @param ray
     * @param scene
     * @return RGBColor
     */
    @Override
    public RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        BaseSurfaceColorComputation baseColorComputation = new BaseSurfaceColorComputation<BaseSurface>(ray, this, scene);

        return baseColorComputation.calculateColor();
    }

    /**
     * @param direction
     * @param normal
     * @return RGBColor
     */
    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
        double cosTheta = Vector.dot(
            direction,
            normal
        );

        return this
            .getSurfaceColor()
            .scale(cosTheta)
            .divide(Math.PI);
    }

    /**
     * @param direction
     * @param normal
     * @return double
     */
    @Override
    public double getPDF (Vector direction, Vector normal) {
        double cosTheta = Vector.dot(
            direction,
            normal
        );

        return cosTheta / Math.PI;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isLightSource () {
        return false;
    }
}
