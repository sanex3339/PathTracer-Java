package PathTracer;

import PathTracer.interfaces.RenderDataHandler;
import PathTracer.interfaces.RenderDataProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

final public class RenderThreadsService {
    final private int threadsCount = Runtime.getRuntime().availableProcessors() - 2;

    private ExecutorService executorService;
    private List<Future<List<Color>>> threadsPool;

    private RenderDataProvider renderDataProvider;
    private RenderDataHandler renderDataHandler;

    RenderThreadsService(RenderDataProvider renderDataProvider, RenderDataHandler renderDataHandler) {
        this.renderDataProvider = renderDataProvider;
        this.renderDataHandler = renderDataHandler;
    }

    /**
     * Fill threadsPool with RenderDataProvider' objects (`Tracer` class objects).
     */
    public void run () {
        this.executorService = Executors.newCachedThreadPool();
        this.threadsPool = new ArrayList<>();

        for (int i = 0; i < this.threadsCount; i++) {
            this.threadsPool.add(
                this.executorService.submit(
                    this.renderDataProvider.callback()
                )
            );
        }

        while (this.threadsPool.size() > 0) {
            this.startThread();
        }

        executorService.shutdown();
    }

    /**
     * Start thread from threadsPool, get thread data (colors collection) and run renderDataHandler with that data.
     */
    private void startThread () {
        try {
            this.renderDataHandler.callback(
                this.threadsPool.get(0)
            );
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        this.threadsPool.remove(0);
        this.threadsPool.add(
            this.executorService.submit(
                this.renderDataProvider.callback()
            )
        );
    }
}
