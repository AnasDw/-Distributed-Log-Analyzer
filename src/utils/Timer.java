package src.utils;

/**
 * Simple utility for measuring execution time in milliseconds.
 */
public class Timer {
    private long startTime;

    /**
     * Starts the timer by recording the current time in nanoseconds.
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * Stops the timer and prints the duration with a label.
     *
     * @param label A description of what was timed.
     */
    public void stopAndPrint(String label) {
        long durationMs = stop();
        System.out.println("⏱️ " + label + " took " + durationMs + " ms");
    }

    /**
     * Stops the timer and returns the duration in milliseconds.
     *
     * @return duration in ms since start()
     */
    public long stop() {
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }
}
