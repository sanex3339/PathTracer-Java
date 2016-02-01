package PathTracer.renderer.materials;

import PathTracer.renderer.*;
import PathTracer.renderer.colorComputation.*;

public class DiffuseMaterial extends AbstractMaterial {
    public DiffuseMaterial (PTColor surfaceColor) {
        super(surfaceColor);
    }

    /**
     * @param ray
     * @param scene
     * @return PTColor
     */
    @Override
    public PTColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        BaseSurfaceColorComputation baseColorComputation = new BaseSurfaceColorComputation<>(ray, this, scene);

        return baseColorComputation.calculateColor();
    }

    /**
     * @param direction
     * @param normal
     * @return PTColor
     */
    @Override
    public PTColor getBRDF (Vector direction, Vector normal) {
        double cosTheta = Vector.dot(
            direction,
            normal
        );

        return this.getSurfaceColor()
            .scale(cosTheta / Math.PI);
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
