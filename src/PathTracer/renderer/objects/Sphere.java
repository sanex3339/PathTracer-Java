package PathTracer.renderer.objects;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

public class Sphere implements SceneObject {
    private Vector position;
    private double radius;
    private BaseSurface material;
    private Double area = null;

    public Sphere(Vector center, double radius, BaseSurface material) {
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
        if (this.area != null) {
            return this.area;
        }

        this.area = 4 * Math.PI * Math.pow(this.radius, 2);

        return this.area;
    }

    /**
     * Get ray-sphere intersection data
     *
     * @param ray
     * @return IntersectData
     */
    public IntersectData getIntersectData (Ray ray) {
        Vector k = Vector.substract(ray.getOrigin(), this.position);
        double b = Vector.dot(k, ray.getDirection());
        double c = Vector.dot(k, k) - Math.pow(this.radius, 2);
        double d = Math.pow(b, 2) - c;
        double distance;
        double t1;
        double t2;
        double minT;
        double maxT;
        Vector hitPoint;

        if (b > 0 || d < 0) {
            return null;
        }

        t1 = -b + Math.sqrt(d);
        t2 = -b - Math.sqrt(d);
        minT = Math.min(t1, t2);
        maxT = Math.max(t1, t2);

        if (minT > PTMath.EPSILON) {
            distance = minT;
        } else {
            distance = maxT;
        }

        hitPoint = ray.getHitPoint(distance);

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
     * @return Vector
     */
    public Vector getPosition () {
        return this.position;
    }

    /**
     * @return Vector
     */
    public Vector getRandomPoint () {
        double u = RandomGenerator.getRandomDouble();
        double v = RandomGenerator.getRandomDouble();
        double q = 2 * Math.PI * u;
        double f = Math.pow(Math.cos(2 * v - 1), -1);

        return Vector.substract(
            this.getPosition(),
            new Vector(
                this.radius * Math.cos(q) * Math.sin(f),
                this.radius * Math.sin(q) * Math.sin(f),
                this.radius * Math.cos(f)
            )
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
     * @return Vector
     */
    public Vector getNormal (Vector point) {
        return Vector.normalize(
            Vector.scale(
                Vector.substract(point, this.position),
                1 / this.radius
            )
        );
    }
}