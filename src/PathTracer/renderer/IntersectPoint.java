package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;

public class IntersectPoint {
    private boolean isIntersected = false;
    private Vector hitPoint;
    private Vector normal;
    private double distanceFromOrigin;
    private SceneObject owner;

    public boolean isIntersected() {
        return this.isIntersected;
    }

    /**
     * @return Vector
     */
    public Vector getHitPoint () {
        return this.hitPoint;
    }

    /**
     * @return Vector
     */
    public Vector getNormal () {
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

    public void setHitPoint (Vector hitPoint) {
        this.hitPoint = hitPoint;
    }

    public void setNormal (Vector normal) {
        this.normal = normal;
    }

    public void setOwner (SceneObject owner) {
        this.owner = owner;
    }

    public void setDistanceFromOrigin (double distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }
}
