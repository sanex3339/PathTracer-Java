package PathTracer.renderer.objects;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import mikera.vectorz.Vector3;

public class Sphere implements SceneObject {
    private Vector3 position;
    private double radius;
    private BaseSurface material;

    public Sphere(Vector3 center, double radius, BaseSurface material) {
        this.position = center;
        this.radius = radius;
        this.material = material;
    }

    /**
     * Get area of sphere
     *
     * @return double
     */
    public double getArea () {
        return 4 * Math.PI * (this.radius * this.radius);
    }

    /**
     * Get ray-sphere intersection data
     *
     * @param ray
     * @return IntersectData
     */
    public IntersectData getIntersectData (Ray ray) {
        Vector3 k = PTVector.substract(ray.getOrigin(), this.position);
        double b = PTVector.dot(k, ray.getDirection());
        double c = PTVector.dot(k, k) - (this.radius * this.radius);
        double d = (b * b) - c;
        double distance;
        double t1;
        double t2;
        double minT;
        double maxT;
        double intersectionPoint = 0;
        Vector3 hitPoint;

        if (b > 0 || d < 0) {
            return null;
        }

        if (d >= 0) {
            t1 = -b + Math.sqrt(d);
            t2 = -b - Math.sqrt(d);
            minT = Math.min(t1, t2);
            maxT = Math.max(t1, t2);

            if (minT > PTMath.EPSILON) {
                intersectionPoint = minT;
            } else {
                intersectionPoint = maxT;
            }

            if (intersectionPoint < PTMath.EPSILON) {
                return null;
            }
        }

        hitPoint = PTVector.add(
            PTVector.scale(ray.getDirection(), intersectionPoint),
            ray.getOrigin()
        );
        distance = PTVector.substract(
            hitPoint,
            ray.getOrigin()
        ).magnitude();

        return new IntersectData(
            hitPoint,
            this.getNormal(hitPoint),
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
        return this.position;
    }

    /**
     * @return Vector3
     */
    public Vector3 getRandomPoint () {
        double u = RandomGenerator.getRandomDouble();
        double v = RandomGenerator.getRandomDouble();
        double q = 2 * Math.PI * u;
        double f = Math.pow(Math.cos(2 * v - 1), -1);

        return PTVector.substract(
            this.getPosition(),
            new PTVector(
                this.radius * Math.cos(q) * Math.sin(f),
                this.radius * Math.sin(q) * Math.sin(f),
                this.radius * Math.cos(f)
            ).getVector()
        );
    }

    /**
     * @return double
     */
    public double getRadius () {
        return this.radius;
    }

    /**
     * Get normal vector of sphere
     *
     * @param point
     * @return Vector3
     */
    public Vector3 getNormal (Vector3 point) {
        return PTVector.normalize(
            PTVector.scale(
                PTVector.substract(point, this.position),
                1 / this.radius
            )
        );
    }
}
