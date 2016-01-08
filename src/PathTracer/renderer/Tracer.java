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
                .setMaterial(new Material(new RGBColor(115, 115, 115), new RGBColor(355, 355, 355)))
        );

        // center sphere
        objects.add(
            new Sphere(new Vector(0, -100, 400), 300)
                .setMaterial(new Material(new RGBColor(115, 115, 115)))
        );

        // top plane
        objects.add(
            new Plane(new Vector(0, -1, 0), new Vector (0, 700, 0))
                .setMaterial(
                    new Material(new RGBColor(245, 245, 245))
                        .setLambertCoeff(1)
                )
        );

        // bottom plane
        objects.add(
            new Plane(new Vector(0, 1, 0), new Vector (0, -500, 0))
                .setMaterial(
                    new Material(new RGBColor(245, 245, 245))
                        .setLambertCoeff(1)
                )
        );

        // right plane
        objects.add(
            new Plane(new Vector(-1, 0, 0), new Vector (700, 0, 0))
                .setMaterial(
                    new Material(new RGBColor(0.3 * 255, 255, 0.1 * 255))
                        .setLambertCoeff(1)
                )
        );

        // left plane
        objects.add(
            new Plane(new Vector(1, 0, 0), new Vector (-700, 0, 0))
                .setMaterial(
                    new Material(new RGBColor(255, 0.3 * 255, 0.1 * 255))
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
                    new Material(new RGBColor(0, 0, 0))
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
        RGBColor color;
        Double cost;
        IntersectPoint intersection = this.trace(ray);
        RGBColor lightSamplingColor;
        Vector newDirection;
        Vector newPoint;
        double randFactor = 1;
        int rayIteration = ray.getIteration();
        RGBColor tempColor;

        if (rayIteration >= 5) {
            if (Math.random() <= 0.1) {
                return new RGBColor(0, 0, 0);
            }

            randFactor = 1 / (1 - 0.1);
        }

        if (!intersection.isIntersected()) {
            return new RGBColor(0, 0, 0);
        }

        lightSamplingColor = new RGBColor(0, 0, 0);

        for (SceneObject object : this.scene.getObjects()) {
            if (object.getMaterial().getEmission() == new RGBColor(0, 0, 0)) {
                continue;
            }

            lightSamplingColor = lightSamplingColor
                .add(this.getLightPower(ray, intersection, object));
        }

        color = lightSamplingColor.scale(ray.getIteration()).add(
            intersection
                .getOwner()
                .getMaterial()
                .getColor()
                .multiple(
                    intersection
                        .getOwner()
                        .getMaterial()
                        .getEmission()
                        .scale(randFactor)
                )
        );

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

        cost = Vector.dot(newDirection, intersection.getNormal());
        tempColor = this.getColor(
            new Ray(
                newPoint,
                newDirection,
                ++rayIteration
            )
        );

        return color.add(
            tempColor
                .add(
                    intersection
                        .getOwner()
                        .getMaterial()
                        .getColor()
                )
                .scale(cost)
                .scale(0.1)
                .scale(randFactor)
        );
    }

    private RGBColor getReflectionColor (Ray ray, IntersectPoint intersect) {
        RGBColor reflectionColor;
        double reflectionValue = intersect.getOwner().getMaterial().getReflectionCoeff();
        Vector reflectedRay;

        if (reflectionValue == 0) {
            return new RGBColor(0, 0, 0);
        }

        reflectedRay = Vector.reflect(
            ray.getDirection(),
            intersect.getNormal()
        );

        reflectionColor = this.getColor(
            new Ray(intersect.getHitPoint(), reflectedRay, ray.getIteration())
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

    private RGBColor getLightPower (Ray ray, IntersectPoint intersect, SceneObject object) {
        Vector l = object.getRandomPoint();

        IntersectPoint shadowRay = this.trace(
            new Ray(
                intersect.getHitPoint(),
                Vector.substract(
                    Vector.substract(
                        object.getPosition(),
                        l
                    ),
                    intersect.getHitPoint()
                )
            )
        );

        if (
            shadowRay.isIntersected() &&
                shadowRay.getOwner().getMaterial().getEmissionValue() > 0
            ) {
            return intersect
                .getOwner()
                .getMaterial()
                .getColor();
        } else {
            return new RGBColor(0, 0, 0);
        }

        /*let lightPower = object.getMaterial().getEmission(),
            lightRandomPoint: Vector,
            shadowRay: IntersectPoint,
            resultPower: number = 0;
        lightRandomPoint = object.getRandomPoint();
        shadowRay = this.trace(
            new Ray(
                intersect.getHitPoint(),
                Vector.substract(
                    Vector.substract(
                        object.getPosition(),
                        lightRandomPoint
                    ),
                    intersect.getHitPoint()
                )
            )
        );
        if (
            shadowRay.isIntersected() &&
            shadowRay.getOwner().getMaterial().getEmissionValue() > 0
        ) {
            resultPower = 1;
        }
        return resultPower;
        /*lightRandomPoint = this.cosineSampleHemisphere(
            intersect.getOwner().getNormal(intersect.getHitPoint())
        );
        shadowRay = this.trace(
            new Ray(
                intersect.getHitPoint(),
                lightRandomPoint
            )
        );
        if (
            shadowRay.isIntersected() &&
            shadowRay.getOwner() instanceof AbstractLight
        ) {
            resultPower = (
                (
                    Vector.substract(
                        Vector.substract(
                            light.getPosition(),
                            lightRandomPoint
                        ),
                        intersect.getHitPoint()
                    ).getLength() * (lightPower / light.getFadeRadius())
                )
            );
            if (resultPower < 0) {
                resultPower = 0;
            }
        }*/

        /*return resultPower;*/
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
        double randoMultiplier = 0.5;
        int spp = 1;

        List<Color> buffer = new ArrayList<>();
        RGBColor color;
        double rand;
        Ray ray;

        for (int y = 0; y < this.screenHeight; y++) {
            for (int x = 0; x < this.screenWidth; x++) {
                color = new RGBColor(0, 0, 0);

                for (int iteration = 0; iteration < spp; iteration++) {
                    rand = 0;

                    if (Math.random() > 0.5) {
                        rand += Math.random() * randoMultiplier;
                    } else {
                        rand -= Math.random() * randoMultiplier;
                    }

                    ray = new Ray(
                        this.scene.getCamera().getPosition(),
                        this.getPerspectiveVector(x + rand, y + rand)
                    );

                    color = color.add(this.getColor(ray));
                }

                color = color.divide(spp);

                buffer.add(
                    new Color(
                        RGBColor.clampColor(color.getRed()),
                        RGBColor.clampColor(color.getGreen()),
                        RGBColor.clampColor(color.getBlue())
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
