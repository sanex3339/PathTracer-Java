package PathTracer.renderer.materials;

import PathTracer.interfaces.ReflectiveSurface;
import PathTracer.renderer.*;
import PathTracer.renderer.colorComputation.*;

public class MirrorMaterial extends AbstractMaterial implements ReflectiveSurface {
    /**
     * Reflection coefficient 0..1
     */
    private double reflectionCoefficient = 1;

    public MirrorMaterial(PTColor surfaceColor, double reflectionCoefficient) {
        this.surfaceColor = surfaceColor;
        this.reflectionCoefficient = reflectionCoefficient;
    }

    /**
     * @param ray
     * @param scene
     * @return PTColor
     */
    @Override
    public PTColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        BaseSurfaceColorComputation baseColorComputation = new BaseSurfaceColorComputation<>(ray, this, scene);
        ReflectiveSurfaceColorComputation reflectiveSurfaceColorComputation = new ReflectiveSurfaceColorComputation<>(ray, intersection, scene, this);

        return baseColorComputation
            .calculateColor()
            .add(
                reflectiveSurfaceColorComputation.calculateColor()
                    .scale(reflectionCoefficient)
            );
    }

    /**
     * @param direction
     * @param normal
     * @return PTColor
     */
    @Override
    public PTColor getBRDF (Vector direction, Vector normal) {
        return this.getSurfaceColor();
    }

    /**
     * @param direction
     * @param normal
     * @return double
     */
    @Override
    public double getPDF (Vector direction, Vector normal) {
        return 1;
    }

    /**
     * @return double
     */
    public double getReflectionCoefficient () {
        return this.reflectionCoefficient;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isLightSource () {
        return false;
    }
}
