package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;

public class PixelColor {
    private Ray ray;
    private Scene scene;

    private RGBColor pixelColor = RGBColor.BLACK;

    private RGBColor surfaceColor = RGBColor.BLACK;
    private RGBColor lightSamplingColor = RGBColor.BLACK;
    private RGBColor directLightingColor = RGBColor.BLACK;
    private RGBColor ambientOcclusionColor = RGBColor.BLACK;
    private RGBColor globalIlluminationColor = RGBColor.BLACK;

    public PixelColor (Ray ray, Scene scene) {
        this.ray = ray;
        this.scene = scene;
    }

    public void calculatePixelColor() {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);

        if (intersection.getOwner().getMaterial().isLightSource()) {
            this.pixelColor = intersection.getOwner().getMaterial().getEmission().getEmissionColor();

            return;
        }

        if (!intersection.isIntersected() || ray.getIteration() >= 5) {
            this.pixelColor = RGBColor.BLACK;

            return;
        }

        try {
            this.pixelColor = this.getDiffuseColor(ray)
                .add(this.getReflectionColor(ray, intersection));
        } catch (NullPointerException e) {
            // TODO: fix that
            this.pixelColor = RGBColor.BLACK;
        }
    }

    public RGBColor getPixelColor () {
        return this.pixelColor;
    }

    public RGBColor getSurfaceColor () {
        return this.surfaceColor;
    }

    public RGBColor getDirectLightingColor () {
        return this.directLightingColor;
    }

    public RGBColor getLightSamplingColor () {
        return this.lightSamplingColor;
    }

    public RGBColor getAmbientOcclusionColor () {
        return this.ambientOcclusionColor;
    }

    public RGBColor getGlobalIlluminationColor () {
        return this.globalIlluminationColor;
    }

    private RGBColor getDiffuseColor (Ray ray) {
        PixelColor nextIterationPixelColor;

        int iteration = ray.getIteration();
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);

        this.surfaceColor = intersection.getOwner().getMaterial().getColor();

        // get light sampling color
        for (SceneObject object : this.scene.getObjects()) {
            if (
                object.getMaterial()
                    .getEmission()
                    .getEmissionColor()
                    .equals(RGBColor.BLACK)
            ) {
                continue;
            }

            this.lightSamplingColor = this.lightSamplingColor
                .add(this.getLightSamplingColor(intersection, object));
        }

        this.directLightingColor = this.surfaceColor.filter(this.lightSamplingColor);

        nextIterationPixelColor = new PixelColor(
            this.getNextIterationRandomRay(ray, intersection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        this.ambientOcclusionColor = this.lightSamplingColor
            .filter(
                nextIterationPixelColor
                    .getDirectLightingColor()
                    .add(
                        nextIterationPixelColor
                            .getLightSamplingColor()
                    )
            )
            .add(
                nextIterationPixelColor
                    .getAmbientOcclusionColor()
            );

        this.globalIlluminationColor = this.surfaceColor
            .filter(
                nextIterationPixelColor
                    .getDirectLightingColor()
                    /*.filter(
                        nextIterationPixelColor
                            .getLightSamplingColor()
                    )*/
            )
            .scale(iteration + 1)
            .add(
                nextIterationPixelColor
                    .getGlobalIlluminationColor()
                .divide(iteration + 1)
            );

        return this.surfaceColor.filter(
            this.lightSamplingColor.add(
                this.globalIlluminationColor
            )
        );
    }

    private RGBColor getReflectionColor (Ray ray, IntersectPoint intersect) {
        PixelColor pixelColor;

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

        pixelColor = new PixelColor(
            new Ray(
                intersect.getHitPoint(),
                reflectedRay,
                ray.getIteration() + 1
            ),
            this.scene
        );
        pixelColor.calculatePixelColor();
        reflectionColor = pixelColor
            .getPixelColor()
            .scale(reflectionValue);

        return reflectionColor;
    }

    private RGBColor getLightSamplingColor (IntersectPoint intersection, SceneObject object) {
        Vector lightSourceRandomPoint = object.getRandomPoint();
        Vector rayLine;

        RGBColor emissionColor = object.getMaterial().getEmission().getEmissionColor();

        double lightPower = object.getMaterial().getEmissionValue();
        double fadeRadius = object.getMaterial().getEmission().getFadeRadius();
        double lambertCos;
        double surfaceCost;

        rayLine = Vector.substract(
            lightSourceRandomPoint,
            intersection.getHitPoint()
        );

        IntersectPoint shadowRay = Tracer.trace(
            new Ray(
                intersection.getHitPoint(),
                Vector.normalize(rayLine)
            ),
            this.scene
        );

        if (
            shadowRay.isIntersected() &&
            shadowRay.getOwner().getMaterial().isLightSource()
        ) {
            Vector lightDirection = Vector.normalize(
                Vector.substract(
                    intersection.getHitPoint(),
                    object.getPosition()
                )
            );

            lambertCos = -Vector.dot(
                lightDirection,
                intersection.getNormal()
            );

            surfaceCost = Vector.dot(
                lightDirection,
                shadowRay.getNormal()
            );

            return emissionColor
                .scale(lambertCos * Math.sqrt((lightPower - rayLine.getLength() * (lightPower / fadeRadius))))
                .scale(surfaceCost * Math.sqrt((lightPower - rayLine.getLength() * (lightPower / fadeRadius))));
        } else {
            return RGBColor.BLACK;
        }
    }

    private Ray getNextIterationRandomRay (Ray ray, IntersectPoint intersection) {
        Vector newDirection;
        Vector newPoint;

        newDirection = PTMath.cosineSampleHemisphere(intersection.getNormal());

        if (Vector.dot(newDirection, ray.getDirection()) > 0) {
            newPoint = Vector.add(
                ray.getOrigin(),
                Vector.scale(
                    ray.getDirection(),
                    intersection.getDistanceFromOrigin() * (1 + PTMath.EPSILON)
                )
            );
        } else {
            newPoint = Vector.add(
                ray.getOrigin(),
                Vector.scale(
                    ray.getDirection(),
                    intersection.getDistanceFromOrigin() * (1 - PTMath.EPSILON)
                )
            );
        }

        return new Ray(
            newPoint,
            newDirection,
            ray.getIteration() + 1
        );
    }
}
