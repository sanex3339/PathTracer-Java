package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.ColorComputation;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

import java.util.List;

public class BaseSurfaceColorComputation <T extends BaseSurface> implements ColorComputation {
    private Ray ray;
    private IntersectPoint intersection;
    private T material;
    private Scene scene;
    private List<SceneObject> sceneLights;

    public BaseSurfaceColorComputation(Ray ray, IntersectPoint intersection, T material, Scene scene) {
        this.ray = ray;
        this.intersection = intersection;
        this.material = material;
        this.scene = scene;
        this.sceneLights = this.scene.getLights();
    }

    /**
     * @return PTColor
     */
    public PTColor calculateColor () {
        Vector newDirection = PTMath.cosineSampleHemisphere(this.intersection.getNormal());

        PTColor explicitLightSamplingColor = PTColor.BLACK;

        for (SceneObject object : this.sceneLights) {
            explicitLightSamplingColor = explicitLightSamplingColor
                .add(ColorComputationService.getExplicitLightSamplingColor(this.intersection, this.scene, object));
        }

        ColorComputationService nextIterationPixelColor = new ColorComputationService(
            ColorComputationService.getNextIterationRandomRay(this.ray, this.intersection, newDirection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        return explicitLightSamplingColor
            .add(
                nextIterationPixelColor
                    .getPixelColor()
                    .multiple(
                        this.material.getBRDF(newDirection, this.intersection.getNormal())
                            .divide(
                                this.material
                                    .getPDF(newDirection, this.intersection.getNormal())
                            )
                    )
            );
    }
}
