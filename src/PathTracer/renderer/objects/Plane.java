package PathTracer.renderer.objects;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

public class Plane implements SceneObject {
    private Vector normal;
    private Vector point = new Vector(0, 0, 0);
    private BaseSurface material;

    public Plane(Vector normal, Vector point, BaseSurface material) {
        this.normal = normal;
        this.point = point;
        this.material = material;
    }

    Plane (Vector normal, BaseSurface material) {
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
        Vector hitPoint;
        double t =
            Vector.dot(
                Vector.substract(
                    this.point,
                    ray.getOrigin()
                ),
                this.normal
            ) /
            Vector.dot(
                ray.getDirection(),
                this.getNormal()
            );

        if (t <= PTMath.EPSILON) {
            return null;
        }

        hitPoint = Vector.add(
            ray.getOrigin(),
            Vector.scale(
                ray.getDirection(),
                t
            )
        );

        distance = Vector.substract(
            hitPoint,
            ray.getOrigin()
        ).getLength();

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
     * @return Vector
     */
    public Vector getPosition () {
        return this.point;
    }

    /**
     * @return Vector
     */
    public Vector getRandomPoint () {
        return this.getPosition();
    }

    /**
     * Get normal vector
     *
     * @return Vector
     */
    public Vector getNormal () {
        return this.normal;
    }
}
