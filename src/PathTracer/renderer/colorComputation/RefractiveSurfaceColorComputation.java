package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.ColorComputation;
import PathTracer.interfaces.RefractiveSurface;
import PathTracer.renderer.*;

public class RefractiveSurfaceColorComputation <T extends RefractiveSurface> implements ColorComputation {
    private Ray ray;
    private IntersectPoint intersection;
    private Scene scene;
    private T material;

    public RefractiveSurfaceColorComputation (Ray ray, IntersectPoint intersection, Scene scene, T material) {
        this.ray = ray;
        this.intersection = intersection;
        this.scene = scene;
        this.material = material;
    }

    /**
     * @return RGBColor
     */
    public RGBColor calculateColor () {
        ColorComputationService colorComputationService = new ColorComputationService(
            new Ray(
                intersection.getHitPoint(),
                this.getRefractiveRayDirection(),
                ray.getIteration() + 1
            ),
            this.scene
        );
        colorComputationService.calculatePixelColor();

        return colorComputationService
            .getPixelColor();
    }

    private Vector getRefractiveRayDirection () {
        double eta = 1 / this.material.getIOR();
        Vector normal = intersection.getNormal();
        double cos_theta = - Vector.dot(
            normal,
            ray.getDirection()
        );

        if (cos_theta < 0) {
            cos_theta *= -1;
            normal = Vector.scale(normal, -1);
            eta = 1 / eta;
        }

        double k = 1 - Math.pow(eta, 2) * (1 - Math.pow(cos_theta, 2));

        return Vector.normalize(
            Vector.add(
                Vector.scale(
                    ray.getDirection(),
                    eta
                ),
                Vector.scale(
                    normal,
                    eta * cos_theta - Math.sqrt(k)
                )
            )
        );
    }
}
