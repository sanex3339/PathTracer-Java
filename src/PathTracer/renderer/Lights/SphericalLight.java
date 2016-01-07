package PathTracer.renderer.Lights;

import PathTracer.renderer.*;

import java.awt.*;

public class SphericalLight extends AbstractLight {
    private Vector center;
    private double radius = 50;
    private double power = 10;
    private double fadeRadius = 2500;
    private Material material = new Material(new RGBColor(244, 244, 244)).setLambertCoeff(1);

    SphericalLight (Vector position, double power, double radius) {
        this.center = position;
        this.power = power;
        this.radius = radius;
    }

    SphericalLight (Vector position, double power) {
        this.center = position;
        this.power = power;
    }

    public double getFadeRadius () {
        return this.fadeRadius;
    }

    public Vector getPosition () {
        return this.center;
    }

    public double getPower () {
        return this.power;
    }

    public double getRadius () {
        return this.radius;
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

    public IntersectData getIntersectData (Ray ray) {
        Vector k = Vector.substract(ray.getOrigin(), this.center);
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

    public Vector getNormal (Vector point) {
        return Vector.normalize(
            Vector.scale(
                Vector.substract(point, this.center),
                1 / this.radius
            )
        );
    }

    public Material getMaterial () {
        return this.material;
    }

    public SphericalLight setMaterial (Material material) {
        this.material = material;

        return this;
    }
}
