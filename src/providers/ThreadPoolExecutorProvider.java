package src.providers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides a configured thread pool for executing parallel tasks,
 * based on the thread pool size defined in the configuration.
 */
public class ThreadPoolExecutorProvider {

    private final ExecutorService executorService;

    /**
     * Constructs the ThreadPoolExecutorProvider with a fixed-size thread pool.
     *
     * @param poolSize the number of worker threads in the pool
     */
    public ThreadPoolExecutorProvider(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
        System.out.println("🧵 Thread pool initialized with size: " + poolSize);
    }

    /**
     * Gets the thread pool executor instance.
     *
     * @return ExecutorService instance
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Shuts down the thread pool gracefully.
     */
    public void shutdown() {
        executorService.shutdown();
        System.out.println("🔚 Thread pool shut down.");
    }
}
