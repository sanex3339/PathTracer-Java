package PathTracer.renderer.Materials;

import PathTracer.interfaces.AreaLightSource;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

public class EmissiveMaterial extends AbstractMaterial implements AreaLightSource {
    public EmissiveMaterial (RGBColor emissionColor, double intensity) {
        super(emissionColor, emissionColor, intensity);
    }

    public EmissiveMaterial (RGBColor emissionColor) {
        super(emissionColor, emissionColor, 1);
    }

    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
        return super.getEmissionColor();
    }

    @Override
    public double getPDF (Vector direction, Vector normal) {
        return 1;
    }

    @Override
    public boolean isLightSource () {
        return true;
    }

    public LightSourceSamplingData sampleAreaLight(IntersectPoint intersection, SceneObject light, Scene scene) {
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

        if (
            shadowRay.isIntersected() &&
                shadowRay.getOwner().getMaterial().isLightSource()
            ) {
            RGBColor emissionColor = light.getMaterial().getEmissionColor();

            double lightPower = light.getMaterial().getIntensity();
            double shadowRayLength = rayLine.getLength();

            Vector lightDirection = Vector.normalize(
                Vector.substract(lightSourceRandomPoint, intersection.getHitPoint())
            );

            double cosTheta1 = - Vector.dot(lightDirection, shadowRay.getNormal());
            double lightPDF = (1.0 / light.getArea()) * shadowRayLength * shadowRayLength / cosTheta1;

            RGBColor lightBRDF = intersection
                .getOwner()
                .getMaterial()
                .getBRDF(lightDirection, intersection.getNormal())
                .filter(emissionColor)
                .scale(lightPower);

            return new LightSourceSamplingData(
                lightBRDF,
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
