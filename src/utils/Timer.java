package src.utils;

public class Timer {
    private long startTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stopAndPrint(String label) {
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        System.out.println("⏱️" + label + " took " + durationMs + " ms");
    }
}

