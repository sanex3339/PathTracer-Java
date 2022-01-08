package PathTracer.renderer;

import PathTracer.interfaces.RayTracer;
import PathTracer.interfaces.SceneObject;
import PathTracer.renderer.colorComputation.ColorComputationService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Tracer implements RayTracer, Callable<RenderResult> {
    private Scene scene;
    private int startX = 0;
    private int endX = 0;
    private int startY = 0;
    private int endY = 0;

    public Tracer (int imageWidth, int imageHeight, Scene scene) {
        this.startX = 0;
        this.endX = this.startY + imageWidth;

        this.startY = 0;
        this.endY = this.startY + imageHeight;

        this.scene = scene;
    }

    public Tracer (int startX, int startY, int imageWidth, int imageHeight, Scene scene) {
        this.startX = startX;
        this.startY = startY;
        this.scene = scene;
    }

    @Override
    public RenderResult call () {
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
     * @return RenderResult
     */
    public RenderResult render () {
        long timeStartMs = System.currentTimeMillis();

        double randomMultiplier = 0.5;
        double randX;
        double randY;

        ColorComputationService colorComputationService;

        List<Color> buffer = new ArrayList<>();
        RGBColor color;
        Ray ray;

        double rand1 = RandomGenerator.getRandomDouble();
        double rand2 = RandomGenerator.getRandomDouble();

        for (int y = this.startY; y < this.endY; y++) {
            for (int x = this.startX; x < this.endX; x++) {
                color = RGBColor.BLACK;

                if (rand1 > 0.5) {
                    randX = x + rand1 * randomMultiplier;
                } else {
                    randX = x - rand1 * randomMultiplier;
                }

                if (rand2 > 0.5) {
                    randY = y + rand2 * randomMultiplier;
                } else {
                    randY = y - rand2 * randomMultiplier;
                }

                ray = new Ray(
                    this.scene.getCamera().getPosition(),
                    this.scene.getCamera().getPerspectiveVector(randX, randY)
                );

                colorComputationService = new ColorComputationService(ray, this.scene);
                colorComputationService.calculatePixelColor();
                color = color.add(
                    RGBColor.clampRGBColor(new RGBColor(colorComputationService.getPixelColor()))
                );

                buffer.add(
                    new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue()
                    )
                );
            }
        }

        long timeEndMs = System.currentTimeMillis();
        int renderTime = (int) (timeEndMs - timeStartMs);

        return new RenderResult(buffer, renderTime);
    }
}
