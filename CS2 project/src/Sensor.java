import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/** Counts vehicles and records each accepted reading exactly once. */
public class Sensor extends TrafficDevice {
    private int vehicleCount;
    private final ArrayList<Integer> history;
    private final Random random;

    public Sensor(String id, String road) {
        super(id, road);
        vehicleCount = 0;
        history = new ArrayList<>();
        random = new Random();
    }

    /** Optional seeded constructor for repeatable simulation runs. */
    public Sensor(String id, String road, long seed) {
        this(id, road);
        random.setSeed(seed);
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Vehicle count must be nonnegative.");
        }
        vehicleCount = count;
        history.add(count);
    }

    /** Returns and records a random count from 0 through max, inclusive. */
    public int generateRandomCount(int max) {
        if (max < 0 || max == Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Maximum count must be from 0 through 2147483646.");
        }
        int count = random.nextInt(max + 1);
        setVehicleCount(count); // One place updates both the count and history.
        return count;
    }

    public double getAverageCount() {
        if (history.isEmpty()) {
            return 0.0;
        }
        long total = 0; // A sum of several large int readings can exceed int.
        for (int count : history) {
            total += count;
        }
        return (double) total / history.size();
    }

    /** A copy prevents callers from changing this sensor's actual history. */
    public ArrayList<Integer> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public String getStatus() {
        return String.format(Locale.ROOT,
                "Sensor %s | road=%s | vehicles=%d | average=%.2f | samples=%d",
                getDeviceId(), getRoadName(), vehicleCount,
                getAverageCount(), history.size());
    }

    @Override
    public String toString() {
        return getStatus();
    }
}