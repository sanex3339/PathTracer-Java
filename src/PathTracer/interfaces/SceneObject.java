package PathTracer.interfaces;

import PathTracer.renderer.IntersectData;
import PathTracer.renderer.Material;
import PathTracer.renderer.Ray;
import PathTracer.renderer.Vector;

public interface SceneObject {
    double getArea ();
    IntersectData getIntersectData (Ray ray);
    Material getMaterial ();
    Vector getPosition ();
    Vector getRandomPoint ();
    SceneObject setMaterial (Material material);
}