package PathTracer.renderer;

import PathTracer.renderer.Objects.AbstractObject;

public class IntersectPoint {
    private boolean intersected = false;
    private Vector hitPoint;
    private Vector normal;
    private double distanceFromOrigin;
    private AbstractObject owner;

    public boolean getIntersect () {
        return this.intersected;
    }

    public Vector getHitPoint () {
        return this.hitPoint;
    }

    public Vector getNormal () {
        return this.normal;
    }

    public AbstractObject getOwner () {
        return this.owner;
    }

    public double getDistanceFromOrigin () {
        return this.distanceFromOrigin;
    }

    public void setIntersect () {
        this.intersected = true;
    }

    public void setHitPoint(Vector hitPoint) {
        this.hitPoint = hitPoint;
    }

    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    public void setOwner(AbstractObject owner) {
        this.owner = owner;
    }

    public void setDistanceFromOrigin (double distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }
}
