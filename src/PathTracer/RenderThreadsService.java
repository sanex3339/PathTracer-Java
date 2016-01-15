package PathTracer;

import PathTracer.interfaces.RenderDataHandler;
import PathTracer.interfaces.RenderDataProvider;

import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

final public class RenderThreadsService {
    final private int threadsCount = Runtime.getRuntime().availableProcessors() - 2;

    private RenderDataProvider renderDataProvider;
    private RenderDataHandler renderDataHandler;

    private ExecutorCompletionService<List<Color>> completionService;

    RenderThreadsService(RenderDataProvider renderDataProvider, RenderDataHandler renderDataHandler) {
        this.renderDataProvider = renderDataProvider;
        this.renderDataHandler = renderDataHandler;
    }

    /**
     * Fill threadsPool with RenderDataProvider' objects (`Tracer` class objects).
     */
    public void run () {
        ExecutorService executorService = Executors.newFixedThreadPool(this.threadsCount);
        this.completionService = new ExecutorCompletionService<>(executorService);

        for (int i = 0; i < this.threadsCount; i++) {
            completionService.submit(
                this.renderDataProvider.callback()
            );
        }

        int i = 0;

        while (i++ < 2000) {
            this.startThread();
        }
    }

    /**
     * Start thread from threadsPool, get thread data (colors collection) and run renderDataHandler with that data.
     */
    private void startThread () {
        try {
            this.renderDataHandler.callback(
                this.completionService.take()
            );
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        this.completionService.submit(
            this.renderDataProvider.callback()
        );
    }
}
