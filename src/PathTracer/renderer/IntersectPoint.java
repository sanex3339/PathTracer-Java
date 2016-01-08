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

    public Vector getHitPoint () {
        return this.hitPoint;
    }

    public Vector getNormal () {
        return this.normal;
    }

    public SceneObject getOwner () {
        return this.owner;
    }

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
