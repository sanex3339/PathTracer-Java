package PathTracer.renderer.materials;

import PathTracer.interfaces.EmissiveSurface;
import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import PathTracer.renderer.colorComputation.*;

public class LightMaterial extends AbstractMaterial implements EmissiveSurface {
    /**
     * RGB emission color 0..255
     */
    private PTColor emissionColor = PTColor.WHITE;

    /**
     * Emission intensity
     */
    private double intensity = 0;

    public LightMaterial (PTColor emissionColor, double intensity) {
        this.surfaceColor = emissionColor;
        this.emissionColor = emissionColor;
        this.intensity = intensity;
    }

    public LightMaterial (PTColor emissionColor) {
        this.surfaceColor = emissionColor;
        this.emissionColor = emissionColor;
        this.intensity = 1;
    }

    /**
     * @param ray
     * @param scene
     * @return PTColor
     */
    @Override
    public PTColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        EmissiveSurfaceColorComputation emissiveSurfaceColorComputation = new EmissiveSurfaceColorComputation<>(this);

        return emissiveSurfaceColorComputation.calculateColor()
            .scale(this.intensity);
    }

    /**
     * @param direction
     * @param normal
     * @return PTColor
     */
    @Override
    public PTColor getBRDF (Vector direction, Vector normal) {
        return this.getEmissionColor();
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
     * @return PTColor
     */
    public PTColor getEmissionColor () {
        return this.emissionColor;
    }

    /**
     * @return double
     */
    public double getIntensity () {
        return this.intensity;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isLightSource () {
        return true;
    }

    /**
     * Sampling of area light source.
     *
     * @param intersection
     * @param light
     * @param scene
     * @return LightSourceSamplingData
     */
    public LightSourceSamplingData sampleLight (IntersectPoint intersection, SceneObject light, Scene scene) {
        Vector lightSourceRandomPoint = light.getRandomPoint();
        Vector rayLine = Vector.substract(
            lightSourceRandomPoint,
            intersection.getHitPoint()
        );

        IntersectPoint shadowRay = Tracer.trace(
            new Ray(
                intersection.getHitPoint(),
                Vector.normalize(rayLine)
            ),
            scene
        );

        BaseSurface intersectionMaterial = intersection.getOwner().getMaterial();
        BaseSurface shadowRayMaterial = shadowRay.getOwner().getMaterial();

        if (
            shadowRay.isIntersected() && shadowRayMaterial.isLightSource()
        ) {
            EmissiveSurface lightMaterial = (EmissiveSurface) light.getMaterial();
            PTColor emissionColor = lightMaterial.getEmissionColor();

            double lightPower = lightMaterial.getIntensity();
            double shadowRayLength = rayLine.getLength();

            Vector lightDirection = Vector.normalize(
                Vector.substract(lightSourceRandomPoint, intersection.getHitPoint())
            );

            double cosTheta1 = - Vector.dot(lightDirection, shadowRay.getNormal());
            double lightPDF = (1.0 / light.getArea()) * shadowRayLength * shadowRayLength / cosTheta1;

            PTColor lightColor = intersectionMaterial
                .getBRDF(lightDirection, intersection.getNormal())
                .multiple(emissionColor)
                .scale(lightPower);

            return new LightSourceSamplingData(
                lightColor,
                lightPDF
            );
        } else {
            return new LightSourceSamplingData(
                PTColor.BLACK,
                1
            );
        }
    }
}
