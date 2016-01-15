package PathTracer;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import PathTracer.renderer.Objects.Plane;
import PathTracer.renderer.Objects.Polygon;
import PathTracer.renderer.Objects.Sphere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final public class Main {
    public static void main (String[] args) {
        int screenWidth = 200;
        int screenHeight = 200;

        List<SceneObject> objects = new ArrayList<>();

        // light sphere
        /*objects.add(
            new Sphere(new Vector(0, 580, 0), 150)
                .setMaterial(
                    new Material(
                        new RGBColor(115, 115, 115),
                        new Emission(new RGBColor(255, 250, 249), 1.6, 1600)
                    )
                )
        );*/

        // light square
        objects.add(
            new Polygon(Arrays.asList(
                new Vector(-700, 699, 700),
                new Vector(200, 299, 200),
                new Vector(200, 299, -200),
                new Vector(-700, 699, -700)
            ))
            .setMaterial(
                new Material(
                    new RGBColor(115, 115, 115),
                    new Emission(new RGBColor(255, 250, 249), 1.8, 2000)
                )
            )
        );

        // mirror sphere
        /*objects.add(
            new Sphere(new Vector(-300, -475, 480), 250)
                .setMaterial(new Material(RGBColor.BLACK, 1))
        );*/

        // gray sphere
        objects.add(
            new Sphere(new Vector(-350, -475, 480), 250)
                .setMaterial(new Material(new RGBColor(0.8 * 255, 0.8 * 255, 0.8 * 255)))
        );

        // gray sphere
        objects.add(
            new Sphere(new Vector(400, -475, 450), 250)
                .setMaterial(new Material(new RGBColor(0.8 * 255, 0.8 * 255, 0.8 * 255)))
        );

        // top polygon
        objects.add(
            new Polygon(Arrays.asList(
                new Vector(-700, 700, 700),
                new Vector(700, 700, 700),
                new Vector(700, 700, -700),
                new Vector(-700, 700, -700)
            ))
                .setMaterial(
                    new Material(new RGBColor(0.75 * 255, 0.75 * 255, 0.75 * 255))
                        .setLambertCoeff(1)
                )
        );

        // bottom plane
        objects.add(
            new Plane(new Vector(0, 1, 0), new Vector (0, -700, 0))
                .setMaterial(
                    new Material(new RGBColor(0.8 * 255, 0.8 * 255, 0.8 * 255))
                        .setLambertCoeff(1)
                )
        );

        // right plane
        objects.add(
            new Plane(new Vector(-1, 0, 0), new Vector (700, 0, 0))
                .setMaterial(
                    new Material(new RGBColor(0.5 * 255, 0.5 * 255, 0.8 * 255))
                        .setLambertCoeff(1)
                )
        );

        // left plane
        objects.add(
            new Plane(new Vector(1, 0, 0), new Vector (-700, 0, 0))
                .setMaterial(
                    new Material(new RGBColor(0.8 * 255, 0.5 * 255, 0.5 * 255))
                        .setLambertCoeff(1)
                )
        );

        // front plane
        objects.add(
            new Plane(new Vector(0, 0, -1), new Vector (0, 0, 700))
                .setMaterial(
                    new Material(new RGBColor(0.8 * 255, 0.8 * 255, 0.8 * 255))
                        .setLambertCoeff(1)
                )
        );

        // back plane
        objects.add(
            new Plane(new Vector(0, 0, 1), new Vector (0, 0, -700))
                .setMaterial(
                    new Material(new RGBColor(0.8 * 255, 0.8 * 255, 0.8 * 255))
                        .setLambertCoeff(1)
                )
        );

        PathTracer pathTracer = new PathTracer(
            screenWidth,
            screenHeight,
            new Scene(
                objects,
                new Camera(
                    new Vector(0, 0, -699),
                    new Vector(0, 0, 1),
                    screenWidth,
                    screenHeight
                )
            )
        );
        pathTracer.init();
    }
}