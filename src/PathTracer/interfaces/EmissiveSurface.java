package PathTracer.interfaces;

import PathTracer.renderer.IntersectPoint;
import PathTracer.renderer.LightSourceSamplingData;
import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Scene;

public interface EmissiveSurface extends BaseSurface {
    RGBColor getEmissionColor ();
    double getIntensity ();
    LightSourceSamplingData sampleLight (IntersectPoint intersection, SceneObject light, Scene scene);
}
