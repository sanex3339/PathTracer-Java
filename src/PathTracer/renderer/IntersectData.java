package PathTracer.renderer;

public class IntersectData {
    private Vector hitPoint;
    private Vector normal;
    private double distance;

    public IntersectData (Vector hitPoint, Vector normal, double distance) {
        this.hitPoint = hitPoint;
        this.normal = normal;
        this.distance = distance;
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
     * @return double
     */
    public double getDistance () {
        return this.distance;
    }
}
