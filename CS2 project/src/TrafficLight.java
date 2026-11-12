/** One traffic signal. Coordination between signals belongs to the controller. */
public class TrafficLight extends TrafficDevice {
    public static final String RED = "RED";
    public static final String YELLOW = "YELLOW";
    public static final String GREEN = "GREEN";

    private String state = RED;

    public TrafficLight(String id, String road) {
        super(id, road);
    }

    public String getState() {
        return state;
    }

    /**
     * Accepts only the three legal colors, ignoring letter case.
     * A rejected value leaves the previous state unchanged.
     */
    public void changeTo(String newState) {
        if (RED.equalsIgnoreCase(newState)) {
            state = RED;
        } else if (YELLOW.equalsIgnoreCase(newState)) {
            state = YELLOW;
        } else if (GREEN.equalsIgnoreCase(newState)) {
            state = GREEN;
        } else {
            throw new IllegalArgumentException("Light state must be RED, YELLOW, or GREEN.");
        }
    }

    public boolean isGreen() {
        return GREEN.equals(state);
    }

    @Override
    public String getStatus() {
        return "TrafficLight " + getDeviceId()
                + " | road=" + getRoadName() + " | state=" + state;
    }

    @Override
    public String toString() {
        return getStatus();
    }
}