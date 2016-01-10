package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;

public class PixelColor {
    private Ray ray;
    private Scene scene;

    private RGBColor pixelColor = RGBColor.BLACK;

    private RGBColor surfaceColor = RGBColor.BLACK;
    private RGBColor lightSamplingColor = RGBColor.BLACK;
    private RGBColor globalIlluminationColor = RGBColor.BLACK;

    public PixelColor (Ray ray, Scene scene) {
        this.ray = ray;
        this.scene = scene;
    }

    public RGBColor getPixelColor() {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);

        if (intersection.getOwner().getMaterial().isLightSource()) {
            return intersection.getOwner().getMaterial().getEmission().getEmissionColor();
        }

        if (!intersection.isIntersected() || ray.getIteration() >= 5) {
            return RGBColor.BLACK;
        }

        try {
            this.pixelColor = this.getDiffuseColor(ray)
                .add(this.getReflectionColor(ray, intersection));
        } catch (NullPointerException e) {
            // TODO: fix that
            return RGBColor.BLACK;
        }

        return this.pixelColor;
    }

    private RGBColor getDiffuseColor (Ray ray) {
        PixelColor pixelColor;

        int iteration = ray.getIteration();
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);

        RGBColor nextIterationPixelColor;

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

        pixelColor = new PixelColor(
            this.getNextIterationRandomRay(ray, intersection),
            this.scene
        );
        nextIterationPixelColor = pixelColor.getPixelColor();

        this.globalIlluminationColor = this.lightSamplingColor
            .scale(iteration + 1)
            .add(
                nextIterationPixelColor.divide(iteration + 1)
            );

        return this.lightSamplingColor.add(
            this.surfaceColor.filter(
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
        reflectionColor = pixelColor.getPixelColor().scale(reflectionValue);

        return reflectionColor;
    }

    private RGBColor getLightSamplingColor (IntersectPoint intersection, SceneObject object) {
        Vector lightSourceRandomPoint = object.getRandomPoint();
        Vector rayLine;

        RGBColor emissionColor = object.getMaterial().getEmission().getEmissionColor();
        RGBColor hitPointColor = intersection.getOwner().getMaterial().getColor();

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

            return hitPointColor
                .filter(
                    emissionColor
                        .scale(lightPower - rayLine.getLength() * (lightPower / fadeRadius))
                        .scale(lambertCos)
                        .scale(surfaceCost)
                );
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
