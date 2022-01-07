package PathTracer.interfaces;

import PathTracer.renderer.IntersectData;
import PathTracer.renderer.Ray;
import mikera.vectorz.Vector3;

public interface SceneObject {
    double getArea ();
    IntersectData getIntersectData (Ray ray);
    BaseSurface getMaterial ();
    Vector3 getPosition ();
    Vector3 getRandomPoint ();
}