import java.util.ArrayList;

/**
 * Controls exactly two roads, with one sensor and one light on each road.
 * greenTime and yellowTime are measured in milliseconds.
 */
public class TrafficController {
    public static final String NORMAL = "NORMAL";

    private final Intersection intersection;
    private final int threshold;
    private final int greenTime;
    private final int yellowTime;
    private int cycleNumber = 0;

    public TrafficController(Intersection intersection, int threshold,
                             int greenTime, int yellowTime) {
        if (intersection == null) {
            throw new IllegalArgumentException("Intersection must not be null.");
        }
        if (threshold < 0 || greenTime < 0 || yellowTime < 0) {
            throw new IllegalArgumentException("Threshold and times must be nonnegative.");
        }
        this.intersection = intersection;
        this.threshold = threshold;
        this.greenTime = greenTime;
        this.yellowTime = yellowTime;
        validateLayout();
    }

    /** Returns the priority road name, or NORMAL when neither gap is large enough. */
    public String decidePriority() {
        validateLayout();
        ArrayList<Sensor> sensors = intersection.getSensors();
        Sensor sensorA = sensors.get(0);
        Sensor sensorB = sensors.get(1);
        int countA = sensorA.getVehicleCount();
        int countB = sensorB.getVehicleCount();

        // Strict comparisons are intentional: equality with the threshold is NORMAL.
        if (countA - countB > threshold) {
            return sensorA.getRoadName();
        }
        if (countB - countA > threshold) {
            return sensorB.getRoadName();
        }
        return NORMAL;
    }

    public void runCycle() {
        validateLayout();
        if (Thread.currentThread().isInterrupted()) {
            setAllRed();
            throw new IllegalStateException("Cycle cancelled before it started.");
        }

        String priority = decidePriority();
        ArrayList<Sensor> sensors = intersection.getSensors();
        // Road A is the first registered sensor's road; Road B is the second.
        String selectedRoad = NORMAL.equals(priority)
                ? sensors.get(cycleNumber % 2).getRoadName() : priority;

        System.out.println();
        System.out.println("Cycle " + (cycleNumber + 1)
                + " | priority=" + priority + " | selected=" + selectedRoad);
        try {
            // Establish an all-red starting point, including after manual changes.
            setAllRed();
            switchToGreen(selectedRoad);
            printPhase("GREEN");
            waitFor(greenTime);

            for (TrafficLight light : intersection.getLights()) {
                if (light.getRoadName().equals(selectedRoad)) {
                    light.changeTo(TrafficLight.YELLOW);
                }
            }
            printPhase("YELLOW");
            waitFor(yellowTime);
        } finally {
            // Also runs if waiting is interrupted: do not leave a green signal behind.
            setAllRed();
        }
        printPhase("ALL RED");
        cycleNumber++; // Only completed cycles affect the next NORMAL decision.
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    private void switchToGreen(String roadName) {
        // First stop every other road, then grant the selected road.
        for (TrafficLight light : intersection.getLights()) {
            if (!light.getRoadName().equals(roadName)) {
                light.changeTo(TrafficLight.RED);
            }
        }
        for (TrafficLight light : intersection.getLights()) {
            if (light.getRoadName().equals(roadName)) {
                light.changeTo(TrafficLight.GREEN);
            }
        }
        if (intersection.hasConflict()) {
            throw new IllegalStateException("Conflicting green signals.");
        }
    }

    private void setAllRed() {
        for (TrafficLight light : intersection.getLights()) {
            light.changeTo(TrafficLight.RED);
        }
    }

    private void printPhase(String phase) {
        System.out.println("Phase: " + phase);
        intersection.printAllStatus();
    }

    private void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Cycle interrupted; all lights have been reset to RED.", exception);
        }
    }

    private void validateLayout() {
        ArrayList<Sensor> sensors = intersection.getSensors();
        ArrayList<TrafficLight> lights = intersection.getLights();
        if (sensors.size() != 2 || lights.size() != 2) {
            throw new IllegalArgumentException(
                    "The controller needs exactly two sensors and two lights.");
        }
        String roadA = sensors.get(0).getRoadName();
        String roadB = sensors.get(1).getRoadName();
        if (roadA.equals(roadB)) {
            throw new IllegalArgumentException("The two sensors must belong to different roads.");
        }
        if (NORMAL.equals(roadA) || NORMAL.equals(roadB)) {
            throw new IllegalArgumentException("NORMAL is reserved for the priority decision.");
        }
        for (Sensor sensor : sensors) {
            int matches = 0;
            for (TrafficLight light : lights) {
                if (light.getRoadName().equals(sensor.getRoadName())) {
                    matches++;
                }
            }
            if (matches != 1) {
                throw new IllegalArgumentException(
                        "Each sensor road must have exactly one matching light: "
                                + sensor.getRoadName());
            }
        }
    }
}