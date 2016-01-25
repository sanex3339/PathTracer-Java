package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.ColorComputation;
import PathTracer.interfaces.ReflectiveSurface;
import PathTracer.renderer.*;

public class ReflectiveSurfaceColorComputation <T extends ReflectiveSurface> implements ColorComputation {
    private Ray ray;
    private IntersectPoint intersection;
    private Scene scene;
    private T material;

    public ReflectiveSurfaceColorComputation (Ray ray, IntersectPoint intersection, Scene scene, T material) {
        this.ray = ray;
        this.intersection = intersection;
        this.scene = scene;
        this.material = material;
    }

    /**
     * @return RGBColor
     */
    public RGBColor calculateColor () {
        Vector reflectedRay = Vector.reflect(
            ray.getDirection(),
            intersection.getNormal()
        );

        ColorComputationService colorComputationService = new ColorComputationService(
            new Ray(
                intersection.getHitPoint(),
                reflectedRay,
                ray.getIteration() + 1
            ),
            this.scene
        );
        colorComputationService.calculatePixelColor();

        return colorComputationService
            .getPixelColor();
    }
}
