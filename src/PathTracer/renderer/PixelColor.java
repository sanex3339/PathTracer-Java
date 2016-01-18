package PathTracer.renderer;

import PathTracer.Main;
import PathTracer.interfaces.SceneObject;

public class PixelColor {
    private Ray ray;
    private Scene scene;

    private RGBColor pixelColor = RGBColor.BLACK;

    private RGBColor BRDF = RGBColor.BLACK;
    private RGBColor explicitColor = RGBColor.BLACK;
    private RGBColor directLightingColor = RGBColor.BLACK;
    private RGBColor ambientOcclusionColor = RGBColor.BLACK;
    private RGBColor globalIlluminationColor = RGBColor.BLACK;

    public PixelColor (Ray ray, Scene scene) {
        this.ray = ray;
        this.scene = scene;
    }

    public void calculatePixelColor() {
        IntersectPoint intersection = Tracer.trace(this.ray, this.scene);
        if (!intersection.isIntersected() || ray.getIteration() > 5) {
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

    public RGBColor getBRDF() {
        return this.BRDF;
    }

    public RGBColor getDirectLightingColor () {
        return this.directLightingColor;
    }

    public RGBColor getExplicitColor() {
        return this.explicitColor;
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
        Vector newDirection = PTMath.cosineSampleHemisphere(intersection.getNormal());

        // for light source we must assign color passes variables
        if (intersection.getOwner().getMaterial().isLightSource()) {
            this.BRDF = intersection.getOwner().getMaterial().getEmission().getEmissionColor();
            this.explicitColor = RGBColor.WHITE;
            this.ambientOcclusionColor = this.explicitColor;
            this.globalIlluminationColor = this.BRDF;

            return this.BRDF;
        }

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

            this.explicitColor = this.explicitColor
                .add(this.getExplicitColor(intersection, object, newDirection));
        }

        double cosTheta = Vector.dot(
            newDirection,
            intersection.getNormal()
        );

        double pdf = cosTheta / Math.PI;

        this.BRDF = intersection
            .getOwner()
            .getMaterial()
            .getColor()
            .scale(cosTheta)
            .divide(Math.PI);

        nextIterationPixelColor = new PixelColor(
            this.getNextIterationRandomRay(ray, intersection, newDirection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        /*this.directLightingColor = this.BRDF.filter(this.explicitColor);

        nextIterationPixelColor = new PixelColor(
            this.getNextIterationRandomRay(ray, intersection, newDirection),
            this.scene
        );
        nextIterationPixelColor.calculatePixelColor();

        this.ambientOcclusionColor = this.explicitColor
            .filter(
                nextIterationPixelColor
                    .getDirectLightingColor()
                    .add(
                        nextIterationPixelColor
                            .getExplicitColor()
                    )
            )
            .add(
                nextIterationPixelColor
                    .getAmbientOcclusionColor()
            );

        this.globalIlluminationColor = this.BRDF
            .filter(
                nextIterationPixelColor
                    .getDirectLightingColor()
            )
            .scale(iteration + 1)
            .add(
                nextIterationPixelColor
                    .getGlobalIlluminationColor()
                    .divide(iteration + 1)
            );

        return this.directLightingColor;

        /*return this.BRDF.filter(
            this.explicitColor.add(
                this.globalIlluminationColor
            )
        );*/

        return explicitColor
            .add(
                nextIterationPixelColor
                    .getPixelColor()
                    .filter(
                        this.BRDF
                            .divide(pdf)
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

    private RGBColor getExplicitColor(IntersectPoint intersection, SceneObject light, Vector newDirection) {
        Vector lightSourceRandomPoint = light.getRandomPoint();
        Vector rayLine = Vector.substract(
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
            RGBColor emissionColor = light.getMaterial().getEmission().getEmissionColor();

            double lightPower = light.getMaterial().getEmissionValue();
            double rayLength = rayLine.getLength();

            Vector sdir = Vector.normalize(
                Vector.substract(
                    lightSourceRandomPoint,
                    intersection.getHitPoint()
                )
            );

            double cosTheta1 = - Vector.dot(
                sdir,
                shadowRay.getNormal()
            );

            double cosTheta2 = Vector.dot(
                sdir,
                intersection.getNormal()
            );

            double lgtPdf = (1.0 / light.getArea()) * rayLength * rayLength / cosTheta1;

            RGBColor lgtVal = intersection
                .getOwner()
                .getMaterial()
                .getColor()
                .scale(cosTheta2)
                .divide(Math.PI)
                .filter(emissionColor)
                .scale(lightPower);

            return lgtVal.divide(lgtPdf);
        } else {
            return RGBColor.BLACK;
        }
    }

    private Ray getNextIterationRandomRay (Ray ray, IntersectPoint intersection, Vector newDirection) {
        Vector newPoint;

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
