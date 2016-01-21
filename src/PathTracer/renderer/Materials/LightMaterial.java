package PathTracer.renderer.Materials;

import PathTracer.interfaces.EmissiveSurface;
import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import PathTracer.renderer.ColorComputation.*;

public class LightMaterial extends AbstractMaterial implements EmissiveSurface {
    /**
     * RGB emission color 0..255
     */
    private RGBColor emissionColor = RGBColor.BLACK;

    /**
     * Emission intensity
     */
    private double intensity = 0;

    public LightMaterial (RGBColor emissionColor, double intensity) {
        this.color = emissionColor;
        this.emissionColor = emissionColor;
        this.intensity = intensity;
    }

    public LightMaterial (RGBColor emissionColor) {
        this.color = emissionColor;
        this.emissionColor = emissionColor;
        this.intensity = 1;
    }

    /**
     * @param ray
     * @param scene
     * @return RGBColor
     */
    @Override
    public RGBColor getComputedColor (Ray ray, IntersectPoint intersection, Scene scene) {
        EmissiveSurfaceColorComputation emissiveSurfaceColorComputation = new EmissiveSurfaceColorComputation<EmissiveSurface>(this);

        return emissiveSurfaceColorComputation.calculateColor();
    }

    /**
     * @param direction
     * @param normal
     * @return RGBColor
     */
    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
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
     * @return RGBColor
     */
    public RGBColor getEmissionColor () {
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
    public LightSourceSamplingData sampleLight(IntersectPoint intersection, SceneObject light, Scene scene) {
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
            RGBColor emissionColor = lightMaterial.getEmissionColor();

            double lightPower = lightMaterial.getIntensity();
            double shadowRayLength = rayLine.getLength();

            Vector lightDirection = Vector.normalize(
                Vector.substract(lightSourceRandomPoint, intersection.getHitPoint())
            );

            double cosTheta1 = - Vector.dot(lightDirection, shadowRay.getNormal());
            double lightPDF = (1.0 / light.getArea()) * shadowRayLength * shadowRayLength / cosTheta1;

            RGBColor lightColor = intersectionMaterial
                .getBRDF(lightDirection, intersection.getNormal())
                .filter(emissionColor)
                .scale(lightPower);

            return new LightSourceSamplingData(
                lightColor,
                lightPDF
            );
        } else {
            return new LightSourceSamplingData(
                RGBColor.BLACK,
                1
            );
        }
    }
}
