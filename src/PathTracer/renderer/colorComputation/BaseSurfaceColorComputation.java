package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.ColorComputation;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

public class BaseSurfaceColorComputation <T extends BaseSurface> implements ColorComputation {
    private Ray ray;
    private T material;
    private Scene scene;

    public BaseSurfaceColorComputation(Ray ray, T material, Scene scene) {
        this.ray = ray;
        this.material = material;
        this.scene = scene;
    }

    /**
     * @return PTColor
     */
    public PTColor calculateColor () {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);
        Vector newDirection = PTMath.cosineSampleHemisphere(intersection.getNormal());

        PTColor explicitLightSamplingColor = PTColor.BLACK;

        for (SceneObject object : this.scene.getLights()) {
            explicitLightSamplingColor = explicitLightSamplingColor
                .add(ColorComputationService.getExplicitLightSamplingColor(intersection, this.scene, object));
        }

        ColorComputationService nextIterationPixelColor = new ColorComputationService(
            ColorComputationService.getNextIterationRandomRay(ray, intersection, newDirection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        return explicitLightSamplingColor
            .add(
                nextIterationPixelColor
                    .getPixelColor()
                    .multiple(
                        this.material.getBRDF(newDirection, intersection.getNormal())
                            .divide(
                                this.material
                                    .getPDF(newDirection, intersection.getNormal())
                            )
                    )
            );
    }
}
