package PathTracer.renderer;

import mikera.vectorz.Vector3;

public class IntersectData {
    private Vector3 hitPoint;
    private Vector3 normal;
    private double distance;

    public IntersectData (Vector3 hitPoint, Vector3 normal, double distance) {
        this.hitPoint = hitPoint;
        this.normal = normal;
        this.distance = distance;
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
     * @return double
     */
    public double getDistance () {
        return this.distance;
    }
}
