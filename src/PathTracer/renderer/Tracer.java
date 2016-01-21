package PathTracer.renderer;

import PathTracer.interfaces.RayTracer;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.colorComputation.ColorComputationService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Tracer implements RayTracer, Callable<List<Color>> {
    private Scene scene;
    private int startX = 0;
    private int startY = 0;
    private int imageWidth;
    private int imageHeight;

    public Tracer (int imageWidth, int imageHeight, Scene scene) {
        this.startX = 0;
        this.startY = 0;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.scene = scene;
    }

    public Tracer (int startX, int startY, int imageWidth, int imageHeight, Scene scene) {
        this.startX = startX;
        this.startY = startY;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.scene = scene;
    }

    @Override
    public List<Color> call () {
        return this.render();
    }

    /**
     * Trace ray into scene and look for intersection.
     * Return IntersectionPoint data of closest intersection to camera
     *
     * @param ray
     * @param scene
     * @return IntersectPoint
     */
    public static IntersectPoint trace (Ray ray, Scene scene) {
        IntersectPoint intersection = new IntersectPoint();
        IntersectData intersectData;

        double minDistance = Double.POSITIVE_INFINITY;

        List<SceneObject> sceneObjects = scene.getObjects();

        for (SceneObject object : sceneObjects) {
            intersectData = object.getIntersectData(ray);

            if (
                intersectData == null ||
                intersectData.getDistance() >= minDistance
            ) {
                continue;
            }

            minDistance = intersectData.getDistance();

            intersection.intersected();
            intersection.setHitPoint(intersectData.getHitPoint());
            intersection.setNormal(intersectData.getNormal());
            intersection.setDistanceFromOrigin(intersectData.getDistance());
            intersection.setOwner(object);
        }

        return intersection;
    }

    /**
     * Render one sample
     *
     * @return List<Color>
     */
    public List<Color> render () {
        double randomMultiplier = 0.5;
        double randX;
        double randY;

        ColorComputationService colorComputationService;

        List<Color> buffer = new ArrayList<>();
        RGBColor color;
        Ray ray;

        for (int y = this.startY; y < this.startY + this.imageHeight; y++) {
            for (int x = this.startX; x < this.startX + this.imageWidth; x++) {
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
                    this.scene.getCamera().getPerspectiveVector(randX, randY)
                );

                colorComputationService = new ColorComputationService(ray, this.scene);
                colorComputationService.calculateColor();
                color = color.add(RGBColor.clampRGBColor(colorComputationService.getPixelColor()));

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
}
