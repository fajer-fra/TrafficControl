/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cs2.project;

public class TrafficLight extends TrafficDevice {

    private String state = "RED";

    public TrafficLight(String id, String road) {
        super(id, road);
    }

    public String getState() {
        return state;
    }

    public boolean isGreen() {
        return state.equals("GREEN");
    }

    public void changeTo(String newState) {
        if (newState == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        if (newState.equalsIgnoreCase("RED") || newState.equalsIgnoreCase("YELLOW") || newState.equalsIgnoreCase("GREEN")) {
            state = newState.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid state: " + newState);
        }
    }

    @Override
    public String getStatus() {
        return getRoadName() + " light: " + state;
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
