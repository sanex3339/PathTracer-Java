package PathTracer.renderer.objects;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import mikera.vectorz.Vector3;

public class Plane implements SceneObject {
    private Vector3 normal;
    private Vector3 point = new PTVector(0, 0, 0).getVector();
    private BaseSurface material;

    public Plane(Vector3 normal, Vector3 point, BaseSurface material) {
        this.normal = normal;
        this.point = point;
        this.material = material;
    }

    Plane (Vector3 normal, BaseSurface material) {
        this.normal = normal;
        this.material = material;
    }

    /**
     * Get area of plane
     *
     * @return double
     */
    public double getArea () {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Get ray-plane intersection data
     *
     * @param ray
     * @return IntersectData
     */
    public IntersectData getIntersectData (Ray ray) {
        double distance;
        Vector3 hitPoint;
        double t =
            PTVector.dot(
                PTVector.substract(
                    this.point,
                    ray.getOrigin()
                ),
                this.normal
            ) /
            PTVector.dot(
                ray.getDirection(),
                this.getNormal()
            );

        if (t <= PTMath.EPSILON) {
            return null;
        }

        hitPoint = PTVector.add(
            ray.getOrigin(),
            PTVector.scale(
                ray.getDirection(),
                t
            )
        );

        distance = PTVector.substract(
            hitPoint,
            ray.getOrigin()
        ).magnitude();

        return new IntersectData(
            hitPoint,
            this.getNormal(),
            distance
        );
    }

    /**
     * @return T extend BaseSurface
     */
    public BaseSurface getMaterial () {
        return this.material;
    }

    /**
     * @return Vector3
     */
    public Vector3 getPosition () {
        return this.point;
    }

    /**
     * @return Vector3
     */
    public Vector3 getRandomPoint () {
        return this.getPosition();
    }

    /**
     * Get normal vector
     *
     * @return Vector3
     */
    public Vector3 getNormal () {
        return this.normal;
    }
}
