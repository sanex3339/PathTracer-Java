package PathTracer.renderer.Objects;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import PathTracer.renderer.Materials.AbstractMaterial;
import PathTracer.renderer.Materials.LambertianMaterial;

public class Sphere implements SceneObject {
    private Vector position;
    private double radius;
    private AbstractMaterial material = LambertianMaterial.BASE_MATERIAL;

    public Sphere(Vector center, double radius) {
        this.position = center;
        this.radius = radius;
    }

    public double getArea () {
        return  4 * Math.PI * Math.pow(this.radius, 2);
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

            if (minT > PTMath.EPSILON) {
                intersectionPoint = minT;
            } else {
                intersectionPoint = maxT;
            }

            if (intersectionPoint < PTMath.EPSILON) {
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

    public AbstractMaterial getMaterial () {
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

        return Vector.substract(
            this.getPosition(),
            new Vector(
                this.radius * Math.cos(q) * Math.sin(f),
                this.radius * Math.sin(q) * Math.sin(f),
                this.radius * Math.cos(f)
            )
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

    public Sphere setMaterial (AbstractMaterial material) {
        this.material = material;

        return this;
    }
}
