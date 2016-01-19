package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.Materials.AbstractMaterial;
import PathTracer.renderer.Materials.EmissiveMaterial;

public class PixelColor {
    private Ray ray;
    private Scene scene;

    private RGBColor pixelColor = RGBColor.BLACK;
    private RGBColor BRDF = RGBColor.BLACK;
    private RGBColor explicitLightSamplingColor = RGBColor.BLACK;

    public PixelColor (Ray ray, Scene scene) {
        this.ray = ray;
        this.scene = scene;
    }

    public void calculatePixelColor() {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);

        if (!intersection.isIntersected() || ray.getIteration() > 5) {
            this.pixelColor = RGBColor.BLACK;

            return;
        }

        this.pixelColor = this.getDiffuseColor(ray);

        if (intersection.getOwner().getMaterial().getReflectionCoefficient() > 0) {
            this.pixelColor = this.pixelColor.add(this.getReflectionColor(ray, intersection));
        }
    }

    public RGBColor getPixelColor () {
        return this.pixelColor;
    }

    public RGBColor getBRDF() {
        return this.BRDF;
    }

    public RGBColor getExplicitLightSamplingColor() {
        return this.explicitLightSamplingColor;
    }

    private RGBColor getDiffuseColor (Ray ray) {
        PixelColor nextIterationPixelColor;

        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);
        Vector newDirection = PTMath.cosineSampleHemisphere(intersection.getNormal());
        AbstractMaterial material = intersection.getOwner().getMaterial();

        if (intersection.getOwner().getMaterial().isLightSource()) {
            return intersection.getOwner().getMaterial().getEmissionColor();
        }

        for (SceneObject object : this.scene.getLights()) {
            this.explicitLightSamplingColor = this.explicitLightSamplingColor
                .add(this.getExplicitLightSamplingColor(intersection, object));
        }

        nextIterationPixelColor = new PixelColor(
            this.getNextIterationRandomRay(ray, intersection, newDirection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        this.BRDF = material.getBRDF(newDirection, intersection.getNormal());

        return this.explicitLightSamplingColor
            .add(
                nextIterationPixelColor
                    .getPixelColor()
                    .filter(
                        this.BRDF
                            .divide(
                                material
                                    .getPDF(newDirection, intersection.getNormal())
                            )
                    )
            );
    }

    private RGBColor getReflectionColor (Ray ray, IntersectPoint intersect) {
        PixelColor pixelColor;

        RGBColor reflectionColor;
        double reflectionValue = intersect.getOwner().getMaterial().getReflectionCoefficient();
        Vector reflectedRay;

        if (reflectionValue == 0) {
            return RGBColor.BLACK;
        }

        reflectedRay = Vector.reflect(
            ray.getDirection(),
            intersect.getNormal()
        );

        pixelColor = new PixelColor(
            new Ray(
                intersect.getHitPoint(),
                reflectedRay,
                ray.getIteration() + 1
            ),
            this.scene
        );
        pixelColor.calculatePixelColor();
        reflectionColor = pixelColor
            .getPixelColor()
            .scale(reflectionValue);

        return reflectionColor;
    }

    private RGBColor getExplicitLightSamplingColor(IntersectPoint intersection, SceneObject light) {
        EmissiveMaterial lightMaterial = (EmissiveMaterial) light.getMaterial();
        LightSourceSamplingData lightSourceSamplingData = lightMaterial.sampleAreaLight(intersection, light, this.scene);

        return lightSourceSamplingData
            .getLightBRDF()
            .divide(
                lightSourceSamplingData
                    .getLightPDF()
            );
    }

    private Ray getNextIterationRandomRay (Ray ray, IntersectPoint intersection, Vector newDirection) {
        Vector newPoint;
        double epsilon = Vector.dot(newDirection, ray.getDirection()) > 0 ? PTMath.EPSILON : -PTMath.EPSILON;

        newPoint = Vector.add(
            ray.getOrigin(),
            Vector.scale(
                ray.getDirection(),
                intersection.getDistanceFromOrigin() * (1 + epsilon)
            )
        );

        return new Ray(
            newPoint,
            newDirection,
            ray.getIteration() + 1
        );
    }
}
