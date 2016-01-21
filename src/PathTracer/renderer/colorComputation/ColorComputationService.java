package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.EmissiveSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

public class ColorComputationService {
    private Ray ray;
    private Scene scene;

    /**
     * Final pixel color
     */
    private RGBColor pixelColor = RGBColor.BLACK;

    public ColorComputationService(Ray ray, Scene scene) {
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

        this.pixelColor = intersection.
            getOwner().
            getMaterial().
            getComputedColor(this.ray, intersection, this.scene);
    }

    /**
     * @return RGBColor
     */
    public RGBColor getPixelColor () {
        return this.pixelColor;
    }

    public static RGBColor getExplicitLightSamplingColor (IntersectPoint intersection, Scene scene, SceneObject lightSource) {
        EmissiveSurface lightMaterial = (EmissiveSurface) lightSource.getMaterial();
        LightSourceSamplingData lightSourceSamplingData = lightMaterial.sampleLight(intersection, lightSource, scene);

        return lightSourceSamplingData
            .getLightBRDF()
            .divide(
                lightSourceSamplingData
                    .getLightPDF()
            );
    }

    public static Ray getNextIterationRandomRay (Ray ray, IntersectPoint intersection, Vector newDirection) {
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
