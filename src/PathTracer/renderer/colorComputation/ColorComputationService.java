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

        if (!intersection.isIntersected() || ray.getIteration() > 7) {
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
}