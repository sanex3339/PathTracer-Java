package PathTracer.renderer.objects;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import mikera.vectorz.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Triangle implements SceneObject {
    private List<Vector3> vertices = new ArrayList<>();
    private BaseSurface material;

    public Triangle(List<Vector3> vertices, BaseSurface material) {
        if (vertices.size() != 3) {
            throw new IllegalArgumentException("Each triangle must contain only 3 vertices.");
        }

        this.vertices = vertices;
        this.material = material;
    }

    /**
     * Get area of triangle
     *
     * @return double
     */
    public double getArea () {
        Vector3 vertex0 = this.vertices.get(0);
        Vector3 vertex1 = this.vertices.get(1);
        Vector3 vertex2 = this.vertices.get(2);

        double a = PTVector.substract(vertex1, vertex0).magnitude();
        double b = PTVector.substract(vertex2, vertex0).magnitude();
        double c = PTVector.substract(vertex2, vertex1).magnitude();

        double s = (a + b + c) / 2;

        return Math.sqrt(
            s * (s - a) * (s - b) * (s - c)
        );
    }

    /**
     * Get ray-triangle intersection data
     *
     * @param ray
     * @return IntersectData
     */
    public IntersectData getIntersectData (Ray ray) {
        double distance;
        double distanceFromAxisCenter;
        double numerator;
        double denominator;

        Vector3 hitPoint;
        Vector3 normal = this.getNormal();

        distanceFromAxisCenter = PTVector.dot(this.vertices.get(0), normal);

        denominator = PTVector.dot(normal, ray.getDirection());
        numerator = -PTVector.dot(normal, ray.getOrigin()) + distanceFromAxisCenter;

        if (numerator >= PTMath.EPSILON) {
            return null;
        }

        distance = numerator / denominator;

        if (distance < PTMath.EPSILON) {
            return null;
        }

        hitPoint = ray.getHitPoint(distance);

        if (!isPointInsideTriangle(hitPoint)) {
            return null;
        }

        return new IntersectData(
            hitPoint,
            normal,
            distance
        );
    }

    /**
     * Get normal vector of triangle
     *
     * @return Vector3
     */
    public Vector3 getNormal () {
        Vector3 vertex0 = this.vertices.get(0);
        Vector3 vertex1 = this.vertices.get(1);
        Vector3 vertex2 = this.vertices.get(2);

        Vector3 edge1 = PTVector.substract(vertex2, vertex0);
        Vector3 edge2 = PTVector.substract(vertex1, vertex0);

        return PTVector.normalize(PTVector.cross(edge1, edge2));
    }

    /**
     * @return Vector3
     */
    public Vector3 getPosition () {
        return this.getRandomPoint();
    }

    /**
     * @return Vector3
     */
    public Vector3 getRandomPoint () {
        Vector3 vertex0 = this.vertices.get(0);
        Vector3 vertex1 = this.vertices.get(1);
        Vector3 vertex2 = this.vertices.get(2);

        double rand1 = RandomGenerator.getRandomDouble();
        double rand2 = RandomGenerator.getRandomDouble();

        Vector3 x = PTVector.add(
            vertex0,
            PTVector.scale(
                PTVector.substract(
                    vertex1,
                    vertex0
                ),
                rand1
            ),
            PTVector.scale(
                PTVector.substract(
                    vertex2,
                    vertex0
                ),
                rand2
            )
        );

        if (!isPointInsideTriangle(x)) {
            Vector3 v3 = PTVector.add(
                vertex0,
                PTVector.substract(
                    vertex1,
                    vertex0
                ),
                PTVector.substract(
                    vertex2,
                    vertex0
                )
            );

            x = PTVector.add(
                vertex0,
                PTVector.substract(
                    v3,
                    x
                )
            );
        }

        return x;
    }

    /**
     * @return T extend BaseSurface
     */
    public BaseSurface getMaterial () {
        return this.material;
    }

    /**
     * @return List<Vector>
     */
    public List<Vector3> getVertices () {
        return this.vertices;
    }

    /**
     * @param index
     * @return Vector3
     */
    public Vector3 getVertexByIndex(int index) {
        return this.vertices.get(index);
    }

    /**
     * Check - if point placed inside triangle
     *
     * @param point
     * @return boolean
     */
    private boolean isPointInsideTriangle (Vector3 point) {
        int verticesLength = this.vertices.size();

        for (int i = 0; i < verticesLength; i++) {
            Vector3 vertex1 = this.vertices.get(i);
            Vector3 vertex2;

            if (i == verticesLength - 1) {
                vertex2 = this.vertices.get(0);
            } else {
                vertex2 = this.vertices.get(i + 1);
            }

            if (
                !Triangle.checkSameClockDir(
                    PTVector.substract(vertex2, vertex1),
                    PTVector.substract(point, vertex1),
                    this.getNormal()
                )
            ) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param vector1
     * @param vector2
     * @param normal
     * @return boolean
     */
    private static boolean checkSameClockDir (Vector3 vector1, Vector3 vector2, Vector3 normal) {
        Vector3 normalV1V2 = PTVector.cross(vector2, vector1);

        return PTVector.dot(normalV1V2, normal) >= 0;
    }
}
