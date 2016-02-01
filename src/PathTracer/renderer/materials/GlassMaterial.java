package PathTracer.renderer.materials;

import PathTracer.interfaces.ReflectiveSurface;
import PathTracer.interfaces.RefractiveSurface;
import PathTracer.renderer.*;
import PathTracer.renderer.PTColor;
import PathTracer.renderer.colorComputation.ColorComputationService;
import PathTracer.renderer.colorComputation.ReflectiveSurfaceColorComputation;
import PathTracer.renderer.colorComputation.RefractiveSurfaceColorComputation;

public class GlassMaterial extends AbstractMaterial implements ReflectiveSurface, RefractiveSurface {
    /**
     * Reflection coefficient 0..1
     */
    private double reflectionCoefficient = 0.7;

    /**
     * Refraction coefficient 0..1
     */
    private double refractionCoefficient = 1;

    /**
     * Index of refraction, for glass 1.51
     */
    private double IOR = 1.6;

    public GlassMaterial () {
        this.surfaceColor = PTColor.BLACK;
    }

    public GlassMaterial (PTColor surfaceColor, double reflectionCoefficient, double IOR) {
        this.surfaceColor = surfaceColor;
        this.reflectionCoefficient = reflectionCoefficient;
        this.refractionCoefficient = 1 - this.reflectionCoefficient;
        this.IOR = IOR;
    }

    /**
     * @param ray
     * @param scene
     * @return PTColor
     */
    @Override
    public PTColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        ReflectiveSurfaceColorComputation reflectiveSurfaceColorComputation = new ReflectiveSurfaceColorComputation<>(ray, intersection, scene, this);
        RefractiveSurfaceColorComputation refractiveSurfaceColorComputation = new RefractiveSurfaceColorComputation<>(ray, intersection, scene, this);

        double fresnelCoefficient = ColorComputationService.fresnel(
            Vector.dot(intersection.getNormal(), ray.getDirection()),
            this.IOR,
            1
        );

        return reflectiveSurfaceColorComputation.calculateColor()
            .scale(this.reflectionCoefficient * fresnelCoefficient)
            .add(
                refractiveSurfaceColorComputation.calculateColor()
                    .scale(this.refractionCoefficient * (1 - fresnelCoefficient))
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
     * @return double
     */
    public double getRefractionCoefficient () {
        return this.refractionCoefficient;
    }

    /**
     * @return double
     */
    public double getIOR () {
        return this.IOR;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isLightSource () {
        return false;
    }
}
