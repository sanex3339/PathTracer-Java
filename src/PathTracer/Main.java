package PathTracer;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import PathTracer.renderer.Materials.DiffuseMaterial;
import PathTracer.renderer.Materials.EmissiveMaterial;
import PathTracer.renderer.Materials.MirrorMaterial;
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
                    new AbstractMaterial(
                        new RGBColor(115, 115, 115),
                        new Emission(new RGBColor(255, 250, 249), 4000)
                    )
                )
        );*/

        // light square
        objects.add(
            new Polygon(Arrays.asList(
                new Vector(-250, 699, 250),
                new Vector(250, 699, 250),
                new Vector(250, 699, -250),
                new Vector(-250, 699, -250)
            ))
            .setMaterial(
                new EmissiveMaterial(
                    new RGBColor(255, 250, 249),
                    10
                )
            )
        );

        // mirror sphere
        objects.add(
            new Sphere(new Vector(-330, -400, 300), 300)
                .setMaterial(new MirrorMaterial(RGBColor.BLACK, 1))
        );

        // gray sphere
        objects.add(
            new Sphere(new Vector(330, -400, -50), 300)
                .setMaterial(new DiffuseMaterial(new RGBColor(0.8 * 255, 0.8 * 255, 0.8 * 255)))
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
                    new DiffuseMaterial(new RGBColor(0.95 * 255, 0.95 * 255, 0.95 * 255))
                )
        );

        // bottom plane
        objects.add(
            new Plane(new Vector(0, 1, 0), new Vector (0, -700, 0))
                .setMaterial(
                    new DiffuseMaterial(new RGBColor(0.95 * 255, 0.95 * 255, 0.95 * 255))
                )
        );

        // right plane
        objects.add(
            new Plane(new Vector(-1, 0, 0), new Vector (700, 0, 0))
                .setMaterial(
                    new DiffuseMaterial(new RGBColor(0.5 * 255, 0.5 * 255, 0.8 * 255))
                )
        );

        // left plane
        objects.add(
            new Plane(new Vector(1, 0, 0), new Vector (-700, 0, 0))
                .setMaterial(
                    new DiffuseMaterial(new RGBColor(0.8 * 255, 0.5 * 255, 0.5 * 255))
                )
        );

        // front plane
        objects.add(
            new Plane(new Vector(0, 0, -1), new Vector (0, 0, 700))
                .setMaterial(
                    new DiffuseMaterial(new RGBColor(0.95 * 255, 0.95 * 255, 0.95 * 255))
                )
        );

        // back polygon
        objects.add(
            new Polygon(Arrays.asList(
                new Vector(-700, 700, -700),
                new Vector(700, 700, -700),
                new Vector(700, -700, -700),
                new Vector(-700, -700, -700)
            ))
            .setMaterial(
                new DiffuseMaterial(new RGBColor(0.95 * 255, 0.95 * 255, 0.95 * 255))
            )
        );

        PathTracer pathTracer = new PathTracer(
            screenWidth,
            screenHeight,
            new Scene(
                objects,
                new Camera(
                    new Vector(0, 0, -2400),
                    new Vector(0, 0, 1),
                    screenWidth,
                    screenHeight
                )
            )
        );
        pathTracer.init();
    }
}