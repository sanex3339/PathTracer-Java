package PathTracer.renderer.Objects;

import PathTracer.renderer.IntersectData;
import PathTracer.renderer.Material;
import PathTracer.renderer.Ray;
import PathTracer.renderer.Vector;

public abstract class AbstractObject {
    public abstract IntersectData getIntersectData (Ray ray);
    public abstract Material getMaterial ();
    public abstract Vector getPosition ();
    public abstract Vector getRandomPoint ();
    public abstract AbstractObject setMaterial (Material material);
}
