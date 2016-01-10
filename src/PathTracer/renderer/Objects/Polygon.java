package PathTracer.renderer.Objects;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon implements SceneObject {
    private List<Vector> vertices = new ArrayList<>();
    private Material material;

    public Polygon (List<Vector> vertices) {
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

        for (int i = 0, verticesLength = this.vertices.size(); i < verticesLength; i++) {
            Vector vertex1 = this.vertices.get(i);
            Vector vertex2;

            if (i == verticesLength - 1) {
                vertex2 = this.vertices.get(0);
            } else {
                vertex2 = this.vertices.get(i + 1);
            }

            if (
                !Polygon.checkSameClockDir(
                    Vector.substract(vertex2, vertex1),
                    Vector.substract(hitPoint, vertex1),
                    this.getNormal()
                )
            ) {
                return null;
            }
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
        Polygon triangle = this.triangulatePolygon();

        double rand1 = Math.random();
        double rand2 = Math.random();

        Vector x = Vector.add(
            triangle.getVertexByIndex(0),
            Vector.add(
                Vector.scale(
                    Vector.substract(
                        triangle.getVertexByIndex(1),
                        triangle.getVertexByIndex(0)
                    ),
                    rand1
                ),
                Vector.scale(
                    Vector.substract(
                        triangle.getVertexByIndex(2),
                        triangle.getVertexByIndex(0)
                    ),
                    rand2
                )
            )
        );

        /*for (int i = 0, verticesLength = triangle.getVertices().size(); i < verticesLength; i++) {
            Vector vertex1 = triangle.getVertexByIndex(i);
            Vector vertex2;

            if (i == verticesLength - 1) {
                vertex2 = triangle.getVertexByIndex(0);
            } else {
                vertex2 = triangle.getVertexByIndex(i + 1);
            }

            if (
                !Polygon.checkSameClockDir(
                    Vector.substract(vertex2, vertex1),
                    Vector.substract(x, vertex1),
                    this.getNormal()
                )
            ) {
                Vector v3 = Vector.add(
                    triangle.getVertexByIndex(0),
                    Vector.add(
                        Vector.substract(
                            triangle.getVertexByIndex(1),
                            triangle.getVertexByIndex(0)
                        ),
                        Vector.substract(
                            triangle.getVertexByIndex(2),
                            triangle.getVertexByIndex(0)
                        )
                    )
                );

                x = Vector.add(
                    triangle.getVertexByIndex(0),
                    Vector.substract(
                        x,
                        v3
                    )
                );
            }
        }*/

        //System.out.println(x.getCoordinates());

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

    public Polygon setMaterial (Material material) {
        this.material = material;

        return this;
    }

    private Polygon triangulatePolygon () {
        Polygon triangle;

        if (this.vertices.size() == 4) {
            if (Math.random() > 0) {
                triangle = new Polygon(
                    Arrays.asList(
                        this.getVertexByIndex(0),
                        this.getVertexByIndex(1),
                        this.getVertexByIndex(3)
                    )
                );
            } else {
                triangle = new Polygon(
                    Arrays.asList(
                        this.getVertexByIndex(1),
                        this.getVertexByIndex(2),
                        this.getVertexByIndex(3)
                    )
                );
            }
        } else {
            triangle = this;
        }

        return triangle;
    }

    private static boolean checkSameClockDir (Vector vector1, Vector vector2, Vector normal) {
        Vector normalV1V2 = Vector.cross(vector2, vector1);

        return Vector.dot(normalV1V2, normal) >= 0;
    }
}
