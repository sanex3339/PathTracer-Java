package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.Objects.Plane;
import PathTracer.renderer.Objects.Sphere;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tracer {
    private Scene scene;
    private int screenWidth;
    private int screenHeight;

    public Tracer (int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        List<SceneObject> objects = new ArrayList<>();

        // light sphere
        objects.add(
            new Sphere(new Vector(0, 580, 0), 150)
                .setMaterial(
                    new Material(
                        new RGBColor(115, 115, 115),
                        new Emission(new RGBColor(255, 250, 249), 1, 1600)
                    )
                )
        );

        // mirror sphere
        objects.add(
            new Sphere(new Vector(-300, -100, 400), 200)
                .setMaterial(new Material(RGBColor.BLACK, 1))
        );

        // green sphere
        objects.add(
            new Sphere(new Vector(300, -300, 400), 200)
                .setMaterial(new Material(new RGBColor(72, 201, 26)))
        );

        // top plane
        objects.add(
            new Plane(new Vector(0, -1, 0), new Vector (0, 700, 0))
                .setMaterial(
                    new Material(new RGBColor(0.75 * 255, 0.75 * 255, 0.75 * 255))
                        .setLambertCoeff(1)
                )
        );

        // bottom plane
        objects.add(
            new Plane(new Vector(0, 1, 0), new Vector (0, -700, 0))
                .setMaterial(
                    new Material(new RGBColor(0.75 * 255, 0.75 * 255, 0.75 * 255))
                        .setLambertCoeff(1)
                )
        );

        // right plane
        objects.add(
            new Plane(new Vector(-1, 0, 0), new Vector (700, 0, 0))
                .setMaterial(
                    new Material(new RGBColor(0.25 * 255, 0.25 * 255, 0.75 * 255))
                        .setLambertCoeff(1)
                )
        );

        // left plane
        objects.add(
            new Plane(new Vector(1, 0, 0), new Vector (-700, 0, 0))
                .setMaterial(
                    new Material(new RGBColor(0.75 * 255, 0.25 * 255, 0.25 * 255))
                        .setLambertCoeff(1)
                )
        );

        // front plane
        objects.add(
            new Plane(new Vector(0, 0, -1), new Vector (0, 0, 700))
                .setMaterial(
                    new Material(new RGBColor(0.75 * 255, 0.75 * 255, 0.75 * 255))
                        .setLambertCoeff(1)
                )
        );

        // back plane
        objects.add(
            new Plane(new Vector(0, 0, 1), new Vector (0, 0, -700))
                .setMaterial(
                    new Material(new RGBColor(0.75 * 255, 0.75 * 255, 0.75 * 255))
                        .setLambertCoeff(1)
                )
        );

        this.setScene(
            new Scene(
                objects,
                new Camera(
                    new Vector(0, 0, -699),
                    new Vector(0, 0, 1),
                    this.screenWidth,
                    this.screenHeight
                )
            )
        );
    }

    private Vector getPerspectiveVector (double x, double y) {
        Camera camera = this.scene.getCamera();

        return Vector.normalize(
            Vector.add(
                camera.getForwardVector(),
                Vector.add(
                    Vector.scale(
                        camera.getRightVector(),
                        camera.recenterX(x)
                    ),
                    Vector.scale(
                        camera.getUpVector(),
                        camera.recenterY(y)
                    )
                )
            )
        );
    }

    public static IntersectPoint trace (Ray ray, Scene scene) {
        IntersectPoint intersection = new IntersectPoint();
        IntersectData intersectData;

        double minDistance = Double.POSITIVE_INFINITY;

        List<SceneObject> sceneObjects = scene.getObjects();

        for (SceneObject object : sceneObjects) {
            intersectData = object.getIntersectData(ray);

            if (
                intersectData != null &&
                intersectData.getDistance() < minDistance
            ) {
                minDistance = intersectData.getDistance();

                intersection.intersected();
                intersection.setHitPoint(intersectData.getHitPoint());
                intersection.setNormal(intersectData.getNormal());
                intersection.setDistanceFromOrigin(intersectData.getDistance());
                intersection.setOwner(object);
            }
        }

        return intersection;
    }

    public List<Color> render () {
        double randomMultiplier = 0.5;
        double randX;
        double randY;

        PixelColor pixelColor;

        List<Color> buffer = new ArrayList<>();
        RGBColor color;
        Ray ray;

        for (int y = 0; y < this.screenHeight; y++) {
            for (int x = 0; x < this.screenWidth; x++) {
                color = RGBColor.BLACK;

                if (Math.random() > 0.5) {
                    randX = x + Math.random() * randomMultiplier;
                } else {
                    randX = x - Math.random() * randomMultiplier;
                }

                if (Math.random() > 0.5) {
                    randY = y + Math.random() * randomMultiplier;
                } else {
                    randY = y - Math.random() * randomMultiplier;
                }

                ray = new Ray(
                    this.scene.getCamera().getPosition(),
                    this.getPerspectiveVector(randX, randY)
                );

                pixelColor = new PixelColor(ray, this.scene);
                color = color.add(pixelColor.getPixelColor());

                buffer.add(
                    new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue()
                    )
                );
            }
        }

        return buffer;
    }

    public void setScene (Scene scene) {
        this.scene = scene;
    }
}
