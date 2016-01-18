package PathTracer.interfaces;

import PathTracer.renderer.IntersectData;
import PathTracer.renderer.Materials.AbstractMaterial;
import PathTracer.renderer.Ray;
import PathTracer.renderer.Vector;

public interface SceneObject {
    double getArea ();
    IntersectData getIntersectData (Ray ray);
    AbstractMaterial getMaterial ();
    Vector getPosition ();
    Vector getRandomPoint ();
    SceneObject setMaterial (AbstractMaterial material);
}