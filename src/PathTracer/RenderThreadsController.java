package PathTracer;

import PathTracer.interfaces.Callback;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RenderThreadsController <T> implements Runnable {
    final public int threadsCount = Runtime.getRuntime().availableProcessors();

    public int currentSample = 1;

    private ExecutorService executorService;
    private List<Future<Color>> threadsPool;
    private Callback<T> callback;

    /**
     * @param callback
     */
    RenderThreadsController(Callback<T> callback) {
        Thread thread = new Thread(this, "RenderThreadsController");
        thread.start();

        this.callback = callback;
    }

    public void run () {
        this.executorService = Executors.newCachedThreadPool();
        this.threadsPool = new ArrayList<>();

        for (int i = 0; i < this.threadsCount; i++) {
            this.threadsPool.add(
                this.executorService.submit(
                    new RenderThread(this.currentSample++)
                )
            );
        }

        int t = 20;

        while (t > 0) {
            this.startThread();
            t--;
        }

        /*while (this.threadsPool.size() > 0) {
            this.startThread();
        }*/

        executorService.shutdown();
    }

    /**
     * Start thread from threadsPool, get thread data and run callback with that data.
     */
    @SuppressWarnings("unchecked")
    private void startThread () {
        try {
            Future<Color> thread = this.threadsPool.get(0);
            Color color = thread.get();

            Map<String, T> data = new HashMap<>();
            data.put("color", (T) color);

            this.callback.callback(data);

            this.threadsPool.remove(0);
            this.threadsPool.add(
                this.executorService.submit(
                    new RenderThread(this.currentSample++)
                )
            );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
