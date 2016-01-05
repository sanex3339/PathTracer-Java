package PathTracer.interfaces;

import java.util.Map;

@FunctionalInterface
public interface Callback {
    void callback (Map data);
}