package PathTracer.renderer.Lights;

import PathTracer.renderer.IntersectData;
import PathTracer.renderer.Material;
import PathTracer.renderer.Objects.AbstractObject;
import PathTracer.renderer.Ray;
import PathTracer.renderer.Vector;

public abstract class AbstractLight {
    public abstract double getFadeRadius ();
    public abstract Vector getPosition ();
    public abstract double getPower ();
    public abstract double getRadius ();
    public abstract IntersectData getIntersectData (Ray ray);
    public abstract Vector getNormal (Vector point);
    public abstract Material getMaterial ();
    public abstract Vector getRandomPoint ();
    public abstract AbstractLight setMaterial (Material material);
}
