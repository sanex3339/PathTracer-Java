package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.EmissiveSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import mikera.vectorz.Vector3;

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

    /**
     * @param intersection
     * @param scene
     * @param lightSource
     * @return RGBColor
     */
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

    /**
     * @param ray
     * @param intersection
     * @param newDirection
     * @return Ray
     */
    public static Ray getNextIterationRandomRay (Ray ray, IntersectPoint intersection, Vector3 newDirection) {
        double epsilon = PTVector.dot(newDirection, ray.getDirection()) > 0 ? PTMath.EPSILON : -PTMath.EPSILON;

        return new Ray(
            PTVector.add(
                ray.getOrigin(),
                PTVector.scale(
                    ray.getDirection(),
                    intersection.getDistanceFromOrigin() * (1 + epsilon)
                )
            ),
            newDirection,
            ray.getIteration() + 1
        );
    }
}
