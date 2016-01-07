package PathTracer.renderer.Objects;

import PathTracer.renderer.*;

public class Plane extends AbstractObject {
    private Vector normal;
    private Vector point = new Vector(0, 0, 0);
    private Material material = new Material(new RGBColor(115, 115, 115));

    public Plane(Vector normal, Vector point) {
        this.normal = normal;
        this.point = point;
    }

    Plane (Vector normal) {
        this.normal = normal;
    }

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

        if (t <= RTMath.EPSILON) {
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

    public Material getMaterial () {
        return this.material;
    }

    public Vector getPosition () {
        return this.point;
    }

    public Vector getRandomPoint () {
        return new Vector(0, 0, 0);
    }

    public Vector getNormal () {
        return this.normal;
    }

    public Plane setMaterial (Material material) {
        this.material = material;

        return this;
    }
}
