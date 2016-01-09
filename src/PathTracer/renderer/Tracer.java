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
            new Sphere(new Vector(0, 630, 0), 150)
                .setMaterial(
                    new Material(
                        new RGBColor(115, 115, 115),
                        new Emission(new RGBColor(255, 250, 249), 1, 1900)
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
                    new Material(RGBColor.BLACK)
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

    private Vector cosineSampleHemisphere (Vector normal) {
        double u = Math.random();
        double v = Math.random();
        double r = Math.sqrt(u);
        double angle = 2 * Math.PI * v;
        Vector sdir;
        Vector tdir;

        if (Math.abs(normal.getCoordinates().get("x")) < 0.5) {
            sdir = Vector.cross(normal, new Vector(1,0,0));
        } else {
            sdir = Vector.cross(normal, new Vector(0,1,0));
        }

        tdir = Vector.cross(normal, sdir);

        return Vector.add(
            Vector.scale(normal,  Math.sqrt(1 - u)),
            Vector.add(
                Vector.scale(sdir, r * Math.cos(angle)),
                Vector.scale(tdir, r * Math.sin(angle))
            )
        );
    }

    private RGBColor getColor (Ray ray) {
        IntersectPoint intersection = this.trace(ray);

        // if light source - return emission color
        if (intersection.getOwner().getMaterial().isLightSource()) {
            return intersection.getOwner().getMaterial().getEmission().getEmissionColor();
        }

        // return black color if not intersected
        if (!intersection.isIntersected()) {
            return RGBColor.BLACK;
        }

        return this.getDiffuseColor(ray)
            .add(
                this.getReflectionColor(ray, intersection)
            );
    }

    private RGBColor getDiffuseColor(Ray ray) {
        Vector newDirection;
        RGBColor tempColor;
        RGBColor lightSamplingColor = RGBColor.BLACK;
        IntersectPoint intersection = this.trace(ray);
        Vector newPoint;
        int rayIteration = ray.getIteration();

        if (rayIteration >= 5) {
            return RGBColor.BLACK;
        }

        // get lightsampling color
        for (SceneObject object : this.scene.getObjects()) {
            if (object.getMaterial().getEmission().equals(RGBColor.BLACK)) {
                continue;
            }

            lightSamplingColor = lightSamplingColor
                .add(this.getLightPower(intersection, object));
        }

        // get new random direction and point for new iteration
        newDirection = this.cosineSampleHemisphere(intersection.getNormal());

        if (Vector.dot(newDirection, ray.getDirection()) > 0) {
            newPoint = Vector.add(
                ray.getOrigin(),
                Vector.scale(
                    ray.getDirection(),
                    intersection.getDistanceFromOrigin() * (1 + RTMath.EPSILON)
                )
            );
        } else {
            newPoint = Vector.add(
                ray.getOrigin(),
                Vector.scale(
                    ray.getDirection(),
                    intersection.getDistanceFromOrigin() * (1 - RTMath.EPSILON)
                )
            );
        }

        tempColor = this.getDiffuseColor(
            new Ray(
                newPoint,
                newDirection,
                ++rayIteration
            )
        );

        return lightSamplingColor
            .scale(rayIteration)
            .add(tempColor)
            .divide(rayIteration);
    }

    private RGBColor getReflectionColor (Ray ray, IntersectPoint intersect) {
        RGBColor reflectionColor;
        double reflectionValue = intersect.getOwner().getMaterial().getReflectionCoeff();
        Vector reflectedRay;

        if (reflectionValue == 0) {
            return RGBColor.BLACK;
        }

        reflectedRay = Vector.reflect(
            ray.getDirection(),
            intersect.getNormal()
        );

        reflectionColor = this.getColor(
            new Ray(intersect.getHitPoint(), reflectedRay, ray.getIteration() + 1)
        ).scale(reflectionValue);

        return reflectionColor;
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

    private RGBColor getLightPower (IntersectPoint intersect, SceneObject object) {
        Vector lightSourceRandomPoint = object.getRandomPoint();
        RGBColor emissionColor = object.getMaterial().getEmission().getEmissionColor();
        RGBColor hitPointColor = intersect.getOwner().getMaterial().getColor();
        double lightPower = object.getMaterial().getEmissionValue();
        double fadeRadius = object.getMaterial().getEmission().getFadeRadius();
        Vector rayLine = Vector.substract(
            Vector.substract(
                object.getPosition(),
                lightSourceRandomPoint
            ),
            intersect.getHitPoint()
        );

        IntersectPoint shadowRay = this.trace(
            new Ray(
                intersect.getHitPoint(),
                Vector.normalize(rayLine)
            )
        );

        if (
            shadowRay.isIntersected() &&
            shadowRay.getOwner().getMaterial().isLightSource()
        ) {
            return hitPointColor
                .filter(
                    emissionColor
                        .scale(
                            lightPower - rayLine.getLength() * (lightPower / fadeRadius)
                        )
                );
        } else {
            return RGBColor.BLACK;
        }
    }

    private IntersectPoint trace (Ray ray) {
        IntersectPoint intersection = new IntersectPoint();
        IntersectData intersectData;
        double minDistance = Double.POSITIVE_INFINITY;
        List<SceneObject> sceneObjects = this.scene.getObjects();

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

        List<Color> buffer = new ArrayList<>();
        RGBColor color;
        double rand;
        Ray ray;

        for (int y = 0; y < this.screenHeight; y++) {
            for (int x = 0; x < this.screenWidth; x++) {
                color = RGBColor.BLACK;

                rand = 0;

                if (Math.random() > 0.5) {
                    rand += Math.random() * randomMultiplier;
                } else {
                    rand -= Math.random() * randomMultiplier;
                }

                ray = new Ray(
                    this.scene.getCamera().getPosition(),
                    this.getPerspectiveVector(x + rand, y + rand)
                );

                color = color.add(this.getColor(ray));

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
