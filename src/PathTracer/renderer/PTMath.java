package PathTracer.renderer;

public final class PTMath {
    public final static double EPSILON = 0.001;

    public static Vector cosineSampleHemisphere (Vector normal) {
        double u = Math.random();
        double v = Math.random();
        double r = Math.sqrt(u);
        double angle = 2 * Math.PI * v;
        Vector sDir;
        Vector tDir;

        if (Math.abs(normal.getCoordinates().get("x")) < 0.5) {
            sDir = Vector.cross(normal, new Vector(1,0,0));
        } else {
            sDir = Vector.cross(normal, new Vector(0,1,0));
        }

        tDir = Vector.cross(normal, sDir);

        return Vector.add(
            Vector.scale(normal,  Math.sqrt(1 - u)),
            Vector.add(
                Vector.scale(sDir, r * Math.cos(angle)),
                Vector.scale(tDir, r * Math.sin(angle))
            )
        );
    }
}
