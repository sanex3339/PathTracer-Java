package PathTracer.renderer;

public class LightSourceSamplingData {
    RGBColor lightBRDF;
    double lightPDF;

    public LightSourceSamplingData(RGBColor lightBRDF, double lightPDF) {
        this.lightBRDF = lightBRDF;
        this.lightPDF = lightPDF;
    }


    public RGBColor getLightBRDF () {
        return this.lightBRDF;
    }

    public double getLightPDF () {
        return this.lightPDF;
    }
}
