package PathTracer;

import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.*;
import PathTracer.renderer.materials.DiffuseMaterial;
import PathTracer.renderer.materials.GlassMaterial;
import PathTracer.renderer.materials.LightMaterial;
import PathTracer.renderer.materials.MirrorMaterial;
import PathTracer.renderer.objects.Plane;
import PathTracer.renderer.objects.Polygon;
import PathTracer.renderer.objects.Sphere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final public class Main {
    public static void main (String[] args) {
        int screenWidth = 512;
        int screenHeight = 512;

        List<SceneObject> objects = new ArrayList<>();

        // light sphere
        /*objects.add(
            new Sphere(
                new Vector(0, 580, 0),
                150,
                new LightMaterial(
                    new PTColor(new RGBColor(255, 250, 249)),
                    10
                )
            )
        );*/

        // light square
        objects.add(
            new Polygon(
                Arrays.asList(
                    new Vector(-250, 699, 250),
                    new Vector(250, 699, 250),
                    new Vector(250, 699, -250),
                    new Vector(-250, 699, -250)
                ),
                new LightMaterial(
                    new PTColor(new RGBColor(255, 250, 249)),
                    10
                )
            )
        );

        // mirror sphere
        objects.add(
            new Sphere(
                new Vector(-330, -400, 300),
                300,
                new MirrorMaterial(new PTColor(RGBColor.BLACK), 1)
            )
        );

        // gray sphere
        objects.add(
            new Sphere(
                new Vector(330, -400, -50),
                300,
                new DiffuseMaterial(new PTColor(new RGBColor(0.8 * 255, 0.8 * 255, 0.8 * 255)))
            )
        );

        // top polygon
        objects.add(
            new Polygon(
                Arrays.asList(
                    new Vector(-700, 700, 700),
                    new Vector(700, 700, 700),
                    new Vector(700, 700, -700),
                    new Vector(-700, 700, -700)
                ),
                new DiffuseMaterial(new PTColor(new RGBColor(186,186, 186)))
            )
        );

        // bottom plane
        objects.add(
            new Plane(
                new Vector(0, 1, 0),
                new Vector (0, -700, 0),
                new DiffuseMaterial(new PTColor(new RGBColor(186,186, 186)))
            )
        );

        // right plane
        objects.add(
            new Plane(
                new Vector(-1, 0, 0),
                new Vector (700, 0, 0),
                new DiffuseMaterial(new PTColor(new RGBColor(166, 13, 13)))
            )
        );

        // left plane
        objects.add(
            new Plane(
                new Vector(1, 0, 0),
                new Vector (-700, 0, 0),
                new DiffuseMaterial(new PTColor(new RGBColor(31, 115, 38)))
            )
        );

        // front plane
        objects.add(
            new Plane(
                new Vector(0, 0, -1),
                new Vector (0, 0, 700),
                new DiffuseMaterial(new PTColor(new RGBColor(186,186, 186)))
            )
        );

        // back polygon
        objects.add(
            new Polygon(
                Arrays.asList(
                    new Vector(-700, 700, -700),
                    new Vector(700, 700, -700),
                    new Vector(700, -700, -700),
                    new Vector(-700, -700, -700)
                ),
                new DiffuseMaterial(new PTColor(new RGBColor(186,186, 186)))
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