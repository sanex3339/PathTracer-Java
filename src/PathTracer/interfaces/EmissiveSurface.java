package PathTracer.interfaces;

import PathTracer.renderer.IntersectPoint;
import PathTracer.renderer.LightSourceSamplingData;
import PathTracer.renderer.PTColor;
import PathTracer.renderer.Scene;

public interface EmissiveSurface extends BaseSurface {
    PTColor getEmissionColor ();
    double getIntensity ();
    LightSourceSamplingData sampleLight (IntersectPoint intersection, SceneObject light, Scene scene);
}
