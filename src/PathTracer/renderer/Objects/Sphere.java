package PathTracer.renderer.Objects;

import PathTracer.renderer.*;

public class Sphere extends AbstractObject {
    private Vector position;
    private double radius;
    private Material material;

    public Sphere(Vector center, double radius) {
        this.position = center;
        this.radius = radius;
    }

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
        double intersectionPoint = 0;
        Vector hitPoint;

        if (b > 0 || d < 0) {
            return null;
        }

        if (d >= 0) {
            t1 = -b + Math.sqrt(d);
            t2 = -b - Math.sqrt(d);
            minT = Math.min(t1, t2);
            maxT = Math.max(t1, t2);

            if (minT > RTMath.EPSILON) {
                intersectionPoint = minT;
            } else {
                intersectionPoint = maxT;
            }

            if (intersectionPoint < RTMath.EPSILON) {
                return null;
            }
        }

        hitPoint = Vector.add(
            Vector.scale(ray.getDirection(), intersectionPoint),
            ray.getOrigin()
        );
        distance = Vector.substract(
            hitPoint,
            ray.getOrigin()
        ).getLength();

        return new IntersectData(
            hitPoint,
            this.getNormal(hitPoint),
            distance
        );
    }

    public Material getMaterial () {
        return this.material;
    }

    public Vector getPosition () {
        return this.position;
    }

    public Vector getRandomPoint () {
        double u = Math.random();
        double v = Math.random();
        double q = 2 * Math.PI * u;
        double f = Math.pow(Math.cos(2 * v - 1), -1);

        return new Vector(
            this.radius * Math.cos(q) * Math.sin(f),
            this.radius * Math.sin(q) * Math.sin(f),
            this.radius * Math.cos(f)
        );
    }

    public double getRadius () {
        return this.radius;
    }

    public Vector getNormal (Vector point) {
        return Vector.normalize(
            Vector.scale(
                Vector.substract(point, this.position),
                1 / this.radius
            )
        );
    }

    public Sphere setMaterial (Material material) {
        this.material = material;

        return this;
    }
}