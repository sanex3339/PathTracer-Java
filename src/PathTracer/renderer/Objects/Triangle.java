package PathTracer.renderer.Objects;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

import java.util.ArrayList;
import java.util.List;

public class Triangle implements SceneObject {
    private List<Vector> vertices = new ArrayList<>();
    private Material material = Material.BASE_MATERIAL;

    public Triangle(List<Vector> vertices) {
        if (vertices.size() != 3) {
            throw new IllegalArgumentException("Each triangle must contain only 3 vertices.");
        }

        this.vertices = vertices;
    }

    public IntersectData getIntersectData (Ray ray) {
        double distance;
        double distanceFromAxisCenter;
        double numerator;
        double denominator;

        Vector hitPoint;
        Vector normal = this.getNormal();

        distanceFromAxisCenter = Vector.dot(this.vertices.get(0), normal);

        denominator = Vector.dot(normal, ray.getDirection());
        numerator = -Vector.dot(normal, ray.getOrigin()) + distanceFromAxisCenter;

        if (numerator >= PTMath.EPSILON) {
            return null;
        }

        distance = numerator / denominator;

        if (distance < PTMath.EPSILON) {
            return null;
        }

        hitPoint = ray.getHitPoin(distance);

        if (!isPointInsideTriangle(hitPoint)) {
            return null;
        }

        return new IntersectData(
            hitPoint,
            normal,
            distance
        );
    }

    public Vector getNormal () {
        Vector edge1 = Vector.substract(this.vertices.get(2), this.vertices.get(0));
        Vector edge2 = Vector.substract(this.vertices.get(1), this.vertices.get(0));

        return Vector.normalize(Vector.cross(edge1, edge2));
    }

    public Vector getPosition () {
        return this.getRandomPoint();
    }

    public Vector getRandomPoint () {
        double rand1 = Math.random();
        double rand2 = Math.random();

        Vector x = Vector.add(
            this.getVertexByIndex(0),
            Vector.scale(
                Vector.substract(
                    this.getVertexByIndex(1),
                    this.getVertexByIndex(0)
                ),
                rand1
            ),
            Vector.scale(
                Vector.substract(
                    this.getVertexByIndex(2),
                    this.getVertexByIndex(0)
                ),
                rand2
            )
        );

        if (!isPointInsideTriangle(x)) {
            Vector v3 = Vector.add(
                this.getVertexByIndex(0),
                Vector.substract(
                    this.getVertexByIndex(1),
                    this.getVertexByIndex(0)
                ),
                Vector.substract(
                    this.getVertexByIndex(2),
                    this.getVertexByIndex(0)
                )
            );

            x = Vector.add(
                this.getVertexByIndex(0),
                Vector.substract(
                    v3,
                    x
                )
            );
        }

        return x;
    }

    public Material getMaterial () {
        return this.material;
    }

    public List<Vector> getVertices () {
        return this.vertices;
    }

    public Vector getVertexByIndex(int index) {
        return this.vertices.get(index);
    }

    public Triangle setMaterial (Material material) {
        this.material = material;

        return this;
    }

    private boolean isPointInsideTriangle (Vector point) {
        for (int i = 0, verticesLength = this.vertices.size(); i < verticesLength; i++) {
            Vector vertex1 = this.vertices.get(i);
            Vector vertex2;

            if (i == verticesLength - 1) {
                vertex2 = this.vertices.get(0);
            } else {
                vertex2 = this.vertices.get(i + 1);
            }

            if (
                !Triangle.checkSameClockDir(
                    Vector.substract(vertex2, vertex1),
                    Vector.substract(point, vertex1),
                    this.getNormal()
                )
            ) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkSameClockDir (Vector vector1, Vector vector2, Vector normal) {
        Vector normalV1V2 = Vector.cross(vector2, vector1);

        return Vector.dot(normalV1V2, normal) >= 0;
    }
}
