package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.ColorComputation;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

import java.util.List;

public class BaseSurfaceColorComputation <T extends BaseSurface> implements ColorComputation {
    private Ray ray;
    private T material;
    private Scene scene;
    private List<SceneObject> sceneLights;

    public BaseSurfaceColorComputation(Ray ray, T material, Scene scene) {
        this.ray = ray;
        this.material = material;
        this.scene = scene;
        this.sceneLights = this.scene.getLights();
    }

    /**
     * @return RGBColor
     */
    public RGBColor calculateColor () {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);
        Vector newDirection = PTMath.cosineSampleHemisphere(intersection.getNormal());

        RGBColor explicitLightSamplingColor = RGBColor.BLACK;

        for (SceneObject object : this.sceneLights) {
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
                    .filter(
                        this.material.getBRDF(newDirection, intersection.getNormal())
                            .divide(
                                this.material
                                    .getPDF(newDirection, intersection.getNormal())
                            )
                    )
            );
    }
}
