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
        Vector intersectionNormal = this.intersection.getNormal();
        Vector newDirection = PTMath.cosineSampleHemisphere(intersectionNormal);

        PTColor directColor = this.material.getSurfaceColor();
        PTColor explicitLightSamplingColor = PTColor.BLACK;

        for (SceneObject lightSource : this.sceneLights) {
            explicitLightSamplingColor = explicitLightSamplingColor
                .add(ColorComputationService.getExplicitLightSamplingColor(this.intersection, this.scene, lightSource));
        }

        ColorComputationService nextIterationPixelColor = new ColorComputationService(
            ColorComputationService.getNextIterationRandomRay(this.ray, this.intersection, newDirection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        return directColor
            .multiple(nextIterationPixelColor.getPixelColor())
            .add(explicitLightSamplingColor);
    }
}
