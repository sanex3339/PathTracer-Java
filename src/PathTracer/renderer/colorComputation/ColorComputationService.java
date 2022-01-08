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
    private PTColor pixelColor = PTColor.BLACK;

    public ColorComputationService(Ray ray, Scene scene) {
        this.ray = ray;
        this.scene = scene;
    }

    /**
     * Start calculation of pixel color
     */
    public void calculatePixelColor() {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);
        double terminationProbability = this.terminationProbability(ray.getIteration());

        if (!intersection.isIntersected() || RandomGenerator.getRandomDouble() < terminationProbability) {
            this.pixelColor = PTColor.BLACK;

            return;
        }

        this.pixelColor = intersection
            .getOwner()
            .getMaterial()
            .getComputedColor(this.ray, intersection, this.scene)
            .scale(1 / (1 - terminationProbability));
    }

    /**
     * @return PTColor
     */
    public PTColor getPixelColor () {
        return this.pixelColor;
    }

    /**
     * @param intersection
     * @param scene
     * @param lightSource
     * @return PTColor
     */
    public static PTColor getExplicitLightSamplingColor (IntersectPoint intersection, Scene scene, SceneObject lightSource) {
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
    public static Ray getNextIterationRandomRay (Ray ray, IntersectPoint intersection, Vector newDirection) {
        double epsilon = Vector.dot(newDirection, ray.getDirection()) > 0 ? PTMath.EPSILON : -PTMath.EPSILON;

        return new Ray(
            Vector.add(
                Vector.add(
                    ray.getOrigin(),
                    Vector.scale(
                        Vector.scale(
                            intersection.getNormal(),
                            PTMath.EPSILON
                        ),
                        Vector.dot(
                            newDirection,
                            intersection.getNormal()
                        )
                    )
                ),
                Vector.scale(
                    ray.getDirection(),
                    intersection.getDistanceFromOrigin() * (1 + epsilon)
                )
            ),
            newDirection,
            ray.getIteration() + 1
        );
    }

    /**
     * @param cosTheta1
     * @param etaExt
     * @param etaInt
     * @return
     */
    public static double fresnel (double cosTheta1, double etaExt, double etaInt) {
        double temp;

        if (cosTheta1 < 0) {
            temp = etaExt;
            etaExt = etaInt;
            etaInt = temp;
        }

        double sinTheta = (etaExt / etaInt) * Math.sqrt(Math.max(PTMath.EPSILON, 1 - cosTheta1 * cosTheta1));

        if (sinTheta > 1) {
            return 1;
        }

        double cosTheta2 = Math.sqrt(Math.max(PTMath.EPSILON, 1 - sinTheta * sinTheta));

        return ColorComputationService.fresnelDielectric(Math.abs(cosTheta1), cosTheta2, etaInt, etaExt);
    }

    /**
     * @param cosTheta1
     * @param cosTheta2
     * @param etaExt
     * @param etaInt
     * @return
     */
    public static double fresnelDielectric (double cosTheta1, double cosTheta2, double etaExt, double etaInt) {
        double Rs = (etaExt * cosTheta1 - etaInt * cosTheta2) / (etaExt * cosTheta1 + etaInt * cosTheta2);
        double Rp = (etaInt * cosTheta1 - etaExt * cosTheta2) / (etaInt * cosTheta1 + etaExt * cosTheta2);

        return (Rs * Rs + Rp * Rp) / 2;
    }

    /**
     * Fresnel Schlick approximation
     *
     * @param cosTheta
     * @param n1
     * @param n2
     * @return double fresnel coefficient
     */
    public static double fresnelSchlick (double cosTheta, double n1, double n2) {
        if (cosTheta <= PTMath.EPSILON) {
            cosTheta = - cosTheta;
        }

        double r0 = Math.pow((n1 - n2) / (n1 + n2), 2);

        return r0 + (1 - r0) * Math.pow((1 - cosTheta), 5);
    }

    /**
     * Return ray termination probability based on current ray iteration.
     * probability = (1 / (1 + sqrt(iteration) / 6)
     *
     * @param iteration
     * @return double
     */
    private double terminationProbability (double iteration) {
        return 1 - (1 / (1 + Math.sqrt(iteration) / 6));
    }
}