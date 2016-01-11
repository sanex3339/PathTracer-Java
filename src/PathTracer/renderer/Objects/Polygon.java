package PathTracer.renderer.Objects;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Polygon implements SceneObject {
    private List<Triangle> triangles = new ArrayList<>();
    private List<Vector> vertices = new ArrayList<>();
    private Material material = Material.BASE_MATERIAL;

    public Polygon (List<Vector> vertices) {
        if (vertices.size() < 3 || vertices.size() > 4) {
            throw new IllegalArgumentException("Each polygon must contain only 3 or 4 vertices.");
        }

        this.vertices = vertices;
        this.triangulatePolygon();
    }

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

    public Material getMaterial () {
        return this.material;
    }

    public Vector getPosition() {
        return this.getRandomPoint();
    }

    public Vector getRandomPoint() {
        List<Vector> randomPoints = this.triangles
            .stream()
            .map(Triangle::getRandomPoint)
            .collect(Collectors.toList());

        return randomPoints.get((int) Math.round(Math.random()));
    }

    public List<Vector> getVertices () {
        return this.vertices;
    }

    public Vector getVertexByIndex(int index) {
        return this.vertices.get(index);
    }

    public Polygon setMaterial (Material material) {
        this.material = material;

        return this;
    }

    private void triangulatePolygon () {
        if (this.vertices.size() == 3) {
            this.triangles.add(
                new Triangle(vertices)
                    .setMaterial(this.material)
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
                Arrays.asList(this.getVertexByIndex(0), this.getVertexByIndex(1), this.getVertexByIndex(ind1))
            )
            .setMaterial(this.material)
        );
        this.triangles.add(
            new Triangle(
                Arrays.asList(this.getVertexByIndex(ind2), this.getVertexByIndex(2), this.getVertexByIndex(3))
            )
            .setMaterial(this.material)
        );
    }
}
