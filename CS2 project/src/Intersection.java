import java.util.ArrayList;

/** Aggregates sensors and lights in one polymorphic collection. */
public class Intersection {
    private final String name;
    private final ArrayList<TrafficDevice> devices;

    public Intersection(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Intersection name must not be blank.");
        }
        this.name = name.trim();
        devices = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addDevice(TrafficDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("Device must not be null.");
        }
        // contains() calls TrafficDevice.equals(), which compares IDs.
        if (devices.contains(device)) {
            throw new IllegalArgumentException(
                    "Duplicate device ID: " + device.getDeviceId());
        }
        devices.add(device);
    }

    public ArrayList<Sensor> getSensors() {
        ArrayList<Sensor> sensors = new ArrayList<>();
        for (TrafficDevice device : devices) {
            if (device instanceof Sensor) {
                sensors.add((Sensor) device);
            }
        }
        return sensors;
    }

    public ArrayList<TrafficLight> getLights() {
        ArrayList<TrafficLight> lights = new ArrayList<>();
        for (TrafficDevice device : devices) {
            if (device instanceof TrafficLight) {
                lights.add((TrafficLight) device);
            }
        }
        return lights;
    }

    /** Detects a conflict; it deliberately does not change any light. */
    public boolean hasConflict() {
        int greenCount = 0;
        for (TrafficLight light : getLights()) {
            if (light.isGreen()) {
                greenCount++;
            }
        }
        return greenCount > 1;
    }

    public void printAllStatus() {
        System.out.println("Intersection: " + name);
        for (TrafficDevice device : devices) {
            // Dynamic dispatch chooses the subclass implementation.
            System.out.println("  " + device.getStatus());
        }
    }
}