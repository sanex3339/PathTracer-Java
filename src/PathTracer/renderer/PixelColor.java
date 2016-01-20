package PathTracer.renderer;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.EmissiveSurface;
import PathTracer.interfaces.ReflectiveSurface;
import PathTracer.interfaces.SceneObject;

public class PixelColor {
    private Ray ray;
    private Scene scene;

    private BaseSurface material;

    /**
     * Final pixel color
     */
    private RGBColor pixelColor = RGBColor.BLACK;

    /**
     * BRDF of surface
     */
    private RGBColor BRDF = RGBColor.BLACK;

    /**
     * Explicit direct light sampling color
     */
    private RGBColor explicitLightSamplingColor = RGBColor.BLACK;

    public PixelColor (Ray ray, Scene scene) {
        this.ray = ray;
        this.scene = scene;
    }

    /**
     * Start calculation of pixel color
     */
    public void calculatePixelColor() {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);

        if (!intersection.isIntersected() || ray.getIteration() > 5) {
            this.pixelColor = RGBColor.BLACK;

            return;
        }

        this.material = intersection.getOwner().getMaterial();
        this.pixelColor = this.getDirectColor(ray);

        if (this.material instanceof ReflectiveSurface) {
            this.pixelColor = this.pixelColor.add(this.getReflectionColor(ray, intersection));
        }
    }

    /**
     * @return RGBColor
     */
    public RGBColor getPixelColor () {
        return this.pixelColor;
    }

    /**
     * @return RGBColor
     */
    public RGBColor getBRDF() {
        return this.BRDF;
    }

    /**
     * @return RGBColor
     */
    public RGBColor getExplicitLightSamplingColor() {
        return this.explicitLightSamplingColor;
    }

    /**
     * Calculate surface direct color (without reflection color)
     *
     * @param ray
     * @return RGBColor
     */
    private RGBColor getDirectColor(Ray ray) {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);
        Vector newDirection = PTMath.cosineSampleHemisphere(intersection.getNormal());

        if (this.material instanceof EmissiveSurface) {
            return ((EmissiveSurface) this.material).getEmissionColor();
        }

        for (SceneObject object : this.scene.getLights()) {
            this.explicitLightSamplingColor = this.explicitLightSamplingColor
                .add(this.getExplicitLightSamplingColor(intersection, object));
        }

        PixelColor nextIterationPixelColor = new PixelColor(
            this.getNextIterationRandomRay(ray, intersection, newDirection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        this.BRDF = this.material.getBRDF(newDirection, intersection.getNormal());

        return this.explicitLightSamplingColor
            .add(
                nextIterationPixelColor
                    .getPixelColor()
                    .filter(
                        this.BRDF
                            .divide(
                                this.material
                                    .getPDF(newDirection, intersection.getNormal())
                            )
                    )
            );
    }

    private RGBColor getReflectionColor (Ray ray, IntersectPoint intersect) {
        double reflectionValue = ((ReflectiveSurface) this.material).getReflectionCoefficient();

        if (reflectionValue == 0) {
            return RGBColor.BLACK;
        }

        Vector reflectedRay = Vector.reflect(
            ray.getDirection(),
            intersect.getNormal()
        );

        PixelColor pixelColor = new PixelColor(
            new Ray(
                intersect.getHitPoint(),
                reflectedRay,
                ray.getIteration() + 1
            ),
            this.scene
        );
        pixelColor.calculatePixelColor();

        return pixelColor
            .getPixelColor()
            .scale(reflectionValue);
    }

    private RGBColor getExplicitLightSamplingColor (IntersectPoint intersection, SceneObject lightSource) {
        EmissiveSurface lightMaterial = (EmissiveSurface) lightSource.getMaterial();
        LightSourceSamplingData lightSourceSamplingData = lightMaterial.sampleLight(intersection, lightSource, this.scene);

        return lightSourceSamplingData
            .getLightBRDF()
            .divide(
                lightSourceSamplingData
                    .getLightPDF()
            );
    }

    private Ray getNextIterationRandomRay (Ray ray, IntersectPoint intersection, Vector newDirection) {
        double epsilon = Vector.dot(newDirection, ray.getDirection()) > 0 ? PTMath.EPSILON : -PTMath.EPSILON;

        return new Ray(
            Vector.add(
                ray.getOrigin(),
                Vector.scale(
                    ray.getDirection(),
                    intersection.getDistanceFromOrigin() * (1 + epsilon)
                )
            ),
            newDirection,
            ray.getIteration() + 1
        );
    }
}
