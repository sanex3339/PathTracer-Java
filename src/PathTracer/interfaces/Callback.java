package PathTracer.interfaces;

import java.util.Map;

@FunctionalInterface
public interface Callback <T> {
    void callback (Map<String, T> data);
}