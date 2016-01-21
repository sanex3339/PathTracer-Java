package PathTracer.renderer.objects;

import PathTracer.interfaces.BaseSurface;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Polygon implements SceneObject {
    private List<Triangle> triangles = new ArrayList<>();
    private List<Vector> vertices = new ArrayList<>();
    private BaseSurface material;

    public Polygon (List<Vector> vertices, BaseSurface material) {
        if (vertices.size() < 3 || vertices.size() > 4) {
            throw new IllegalArgumentException("Each polygon must contain only 3 or 4 vertices.");
        }

        this.vertices = vertices;
        this.material = material;
        this.triangulatePolygon();
    }

    /**
     * Get area of polygon
     *
     * @return double
     */
    public double getArea () {
        double area = 0;

        for (Triangle triangle : this.triangles) {
            area += triangle.getArea();
        }

        return area;
    }

    /**
     * Get intersection data for polygon
     *
     * @param ray
     * @return IntersectData
     */
    public IntersectData getIntersectData(Ray ray) {
        IntersectData intersectData;

        for (Triangle triangle : this.triangles) {
            intersectData = triangle.getIntersectData(ray);

            if (intersectData != null) {
                return intersectData;
            }
        }

        return null;
    }

    /**
     * @return T extend BaseSurface
     */
    public BaseSurface getMaterial () {
        return this.material;
    }

    /**
     * @return  Vector
     */
    public Vector getPosition() {
        return this.getRandomPoint();
    }

    /**
     * @return Vector
     */
    public Vector getRandomPoint() {
        List<Vector> randomPoints = this.triangles
            .stream()
            .map(Triangle::getRandomPoint)
            .collect(Collectors.toList());

        return randomPoints.get((int) Math.round(Math.random()));
    }

    /**
     * @return List<Vector>
     */
    public List<Vector> getVertices () {
        return this.vertices;
    }

    /**
     * @param index
     * @return Vector
     */
    public Vector getVertexByIndex(int index) {
        return this.vertices.get(index);
    }

    /**
     * Triangulate 4 vertex polygon by two 3 vertex triangles
     */
    private void triangulatePolygon () {
        if (this.vertices.size() == 3) {
            this.triangles.add(
                new Triangle(
                    vertices,
                    this.material
                )
            );

            return;
        }

        double dist1 = Vector.substract(
            this.getVertexByIndex(2),
            this.getVertexByIndex(0)
        ).getLength();

        double dist2 = Vector.substract(
            this.getVertexByIndex(1),
            this.getVertexByIndex(3)
        ).getLength();

        int ind1;
        int ind2;

        if (dist1 > dist2) {
            ind1 = 3;
            ind2 = 1;
        } else {
            ind1 = 2;
            ind2 = 0;
        }

        this.triangles.add(
            new Triangle(
                Arrays.asList(this.getVertexByIndex(0), this.getVertexByIndex(1), this.getVertexByIndex(ind1)),
                this.material
            )
        );
        this.triangles.add(
            new Triangle(
                Arrays.asList(this.getVertexByIndex(ind2), this.getVertexByIndex(2), this.getVertexByIndex(3)),
                this.material
            )
        );
    }
}
