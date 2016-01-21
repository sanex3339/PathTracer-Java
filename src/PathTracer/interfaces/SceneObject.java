package PathTracer.interfaces;

import PathTracer.renderer.IntersectData;
import PathTracer.renderer.Ray;
import PathTracer.renderer.Vector;

public interface SceneObject {
    double getArea ();
    IntersectData getIntersectData (Ray ray);
    BaseSurface getMaterial ();
    Vector getPosition ();
    Vector getRandomPoint ();
}