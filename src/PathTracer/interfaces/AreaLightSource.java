package PathTracer.interfaces;

import PathTracer.renderer.IntersectPoint;
import PathTracer.renderer.LightSourceSamplingData;
import PathTracer.renderer.Scene;

public interface AreaLightSource {
    LightSourceSamplingData sampleAreaLight (IntersectPoint intersection, SceneObject light, Scene scene);
}
