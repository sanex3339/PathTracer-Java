package PathTracer.renderer;

public class IntersectData {
    public Vector hitPoint;
    public Vector normal;
    public double distance;

    public IntersectData(Vector hitPoint, Vector normal, double distance) {
        this.hitPoint = hitPoint;
        this.normal = normal;
        this.distance = distance;
    }
}
