package PathTracer.renderer.Objects;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

public class Plane implements SceneObject {
    private Vector normal;
    private Vector point = new Vector(0, 0, 0);
    private Material material = Material.BASE_MATERIAL;

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

    public Material getMaterial () {
        return this.material;
    }

    public Vector getPosition () {
        return this.point;
    }

    public Vector getRandomPoint () {
        return this.getPosition();
    }

    public Vector getNormal () {
        return this.normal;
    }

    public Plane setMaterial (Material material) {
        this.material = material;

        return this;
    }
}
