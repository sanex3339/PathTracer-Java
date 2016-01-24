package PathTracer.renderer.materials;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.ReflectiveSurface;
import PathTracer.interfaces.RefractiveSurface;
import PathTracer.renderer.*;
import PathTracer.renderer.RGBColor;
import PathTracer.renderer.colorComputation.BaseSurfaceColorComputation;
import PathTracer.renderer.colorComputation.ReflectiveSurfaceColorComputation;
import PathTracer.renderer.colorComputation.RefractiveSurfaceColorComputation;

public class GlassMaterial extends AbstractMaterial implements ReflectiveSurface, RefractiveSurface {
    /**
     * diffuse coefficient
     */
    private double diffuseCoefficient = 0;

    /**
     * Reflection coefficient 0..1
     */
    private double reflectionCoefficient = 0.75;

    /**
     * Refraction coefficient 0..1
     */
    private double refractionCoefficient = 0.92;

    /**
     * Index of refraction, for glass 1.51
     */
    private double IOR = 1.51;

    public GlassMaterial () {
        this.surfaceColor = RGBColor.BLACK;
    }

    public GlassMaterial (RGBColor surfaceColor, double reflectionCoefficient, double IOR) {
        this.surfaceColor = surfaceColor;
        this.reflectionCoefficient = reflectionCoefficient;
        this.refractionCoefficient = 1 - this.reflectionCoefficient;
        this.IOR = IOR;
    }

    /**
     * @param ray
     * @param scene
     * @return RGBColor
     */
    @Override
    public RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        BaseSurfaceColorComputation baseColorComputation = new BaseSurfaceColorComputation<BaseSurface>(ray, this, scene);
        ReflectiveSurfaceColorComputation reflectiveSurfaceColorComputation = new ReflectiveSurfaceColorComputation<>(ray, intersection, scene, this);
        RefractiveSurfaceColorComputation refractiveSurfaceColorComputation = new RefractiveSurfaceColorComputation<>(ray, intersection, scene, this);

        return reflectiveSurfaceColorComputation.calculateColor()
            .add(
                refractiveSurfaceColorComputation.calculateColor()
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
