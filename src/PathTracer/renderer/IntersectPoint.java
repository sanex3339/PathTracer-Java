package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;
import mikera.vectorz.Vector3;

public class IntersectPoint {
    private boolean isIntersected = false;
    private Vector3 hitPoint;
    private Vector3 normal;
    private double distanceFromOrigin;
    private SceneObject owner;

    public boolean isIntersected() {
        return this.isIntersected;
    }

    /**
     * @return Vector3
     */
    public Vector3 getHitPoint () {
        return this.hitPoint;
    }

    /**
     * @return Vector3
     */
    public Vector3 getNormal () {
        return this.normal;
    }

    /**
     * @return SceneObject
     */
    public SceneObject getOwner () {
        return this.owner;
    }

    /**
     * @return double
     */
    public double getDistanceFromOrigin () {
        return this.distanceFromOrigin;
    }

    public void intersected () {
        this.isIntersected = true;
    }

    public void setHitPoint (Vector3 hitPoint) {
        this.hitPoint = hitPoint;
    }

    public void setNormal (Vector3 normal) {
        this.normal = normal;
    }

    public void setOwner (SceneObject owner) {
        this.owner = owner;
    }

    public void setDistanceFromOrigin (double distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }
}
