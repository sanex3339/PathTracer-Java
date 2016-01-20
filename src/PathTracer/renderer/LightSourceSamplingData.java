package PathTracer.renderer;

public class LightSourceSamplingData {
    RGBColor lightBRDF;
    double lightPDF;

    public LightSourceSamplingData(RGBColor lightBRDF, double lightPDF) {
        this.lightBRDF = lightBRDF;
        this.lightPDF = lightPDF;
    }

    /**
     * @return RGBColor
     */
    public RGBColor getLightBRDF () {
        return this.lightBRDF;
    }

    /**
     * @return double
     */
    public double getLightPDF () {
        return this.lightPDF;
    }
}
