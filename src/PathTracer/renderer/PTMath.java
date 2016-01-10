package PathTracer.renderer;

public final class PTMath {
    public final static double EPSILON = 0.001;

    public static Vector cosineSampleHemisphere (Vector normal) {
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
}
