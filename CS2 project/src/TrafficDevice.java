/**
 * Common parent for devices attached to a road.
 * A device's immutable ID determines equality, even across subclasses.
 */
public class TrafficDevice {
    private final String deviceId;
    private final String roadName;

    public TrafficDevice(String id, String road) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Device ID must not be blank.");
        }
        if (road == null || road.trim().isEmpty()) {
            throw new IllegalArgumentException("Road name must not be blank.");
        }
        deviceId = id.trim();
        roadName = road.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getRoadName() {
        return roadName;
    }

    /** Subclasses override this method to supply their own status. */
    public String getStatus() {
        return "Device " + deviceId + " | road=" + roadName;
    }

    @Override
    public String toString() {
        return getStatus();
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TrafficDevice)) {
            return false;
        }
        TrafficDevice device = (TrafficDevice) other;
        return deviceId.equals(device.deviceId);
    }

    @Override
    public final int hashCode() {
        return deviceId.hashCode();
    }
}