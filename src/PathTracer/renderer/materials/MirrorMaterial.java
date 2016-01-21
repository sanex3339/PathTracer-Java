package PathTracer.renderer.materials;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.ReflectiveSurface;
import PathTracer.renderer.*;
import PathTracer.renderer.colorComputation.*;

public class MirrorMaterial extends AbstractMaterial implements ReflectiveSurface {
    /**
     * Reflection coefficient 0..1
     */
    private double reflectionCoefficient = 0;

    public MirrorMaterial(RGBColor surfaceColor, double reflectionCoefficient) {
        this.surfaceColor = surfaceColor;
        this.reflectionCoefficient = reflectionCoefficient;
    }

    /**
     * @param ray
     * @param scene
     * @return RGBColor
     */
    @Override
    public RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        BaseSurfaceColorComputation baseColorComputation = new BaseSurfaceColorComputation<BaseSurface>(ray, this, scene);
        ReflectiveSurfaceColorComputation reflectiveSurfaceColorComputation = new ReflectiveSurfaceColorComputation<ReflectiveSurface>(ray, intersection, scene, this);

        return baseColorComputation
            .calculateColor()
            .add(
                reflectiveSurfaceColorComputation.calculateColor()
            );
    }

    /**
     * @param direction
     * @param normal
     * @return RGBColor
     */
    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
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
