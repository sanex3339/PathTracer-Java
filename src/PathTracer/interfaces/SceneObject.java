package PathTracer.interfaces;

import PathTracer.renderer.IntersectData;
import PathTracer.renderer.Ray;
import PathTracer.renderer.Vector;

public interface SceneObject <T extends BaseSurface> {
    double getArea ();
    IntersectData getIntersectData (Ray ray);
    T getMaterial ();
    Vector getPosition ();
    Vector getRandomPoint ();
}