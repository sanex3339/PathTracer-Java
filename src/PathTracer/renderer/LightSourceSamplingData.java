package PathTracer.renderer;

public class LightSourceSamplingData {
    PTColor lightBRDF;
    double lightPDF;

    public LightSourceSamplingData(PTColor lightBRDF, double lightPDF) {
        this.lightBRDF = lightBRDF;
        this.lightPDF = lightPDF;
    }

    /**
     * @return PTColor
     */
    public PTColor getLightBRDF () {
        return this.lightBRDF;
    }

    /**
     * @return double
     */
    public double getLightPDF () {
        return this.lightPDF;
    }
}
