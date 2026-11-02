package cs2.project;

import java.util.ArrayList;
import sensor.Sensor;

public class TrafficController {

    // Information needed to control the traffic lights
    private Intersection intersection;
    private int threshold;
    private int greenTime;
    private int yellowTime;
    private int lastGreenIndex = -1;

    // Constructor to set the traffic controller values
    public TrafficController(Intersection intersection, int threshold,
                             int greenTime, int yellowTime) {

        // Check if the intersection exists
        if (intersection == null) {
            throw new IllegalArgumentException("Intersection cannot be null");
        }

        // Make sure the numbers are not negative
        if (threshold < 0 || greenTime < 0 || yellowTime < 0) {
            throw new IllegalArgumentException("Values cannot be negative");
        }

        this.intersection = intersection;
        this.threshold = threshold;
        this.greenTime = greenTime;
        this.yellowTime = yellowTime;
    }

    // Decide which road should get the green light
    public int decidePriority(int count0, int count1) {

        // Find the difference between the two roads
        int diff = Math.abs(count0 - count1);

        // If the difference is big, choose the busier road
        if (diff > threshold) {
            return (count0 > count1) ? 0 : 1;
        }

        // Otherwise, keep the previous road
        // If there is no previous road, choose road 0
        return (lastGreenIndex >= 0) ? lastGreenIndex : 0;
    }

    // Change the lights safely
    public void switchToGreen(TrafficLight target, TrafficLight other) {

        // Check that both lights exist
        if (target == null || other == null) {
            throw new IllegalArgumentException("Lights cannot be null");
        }

        // If the target is already green, stop
        if (target.isGreen()) {
            return;
        }

        // Change the other light from green to yellow
        if (other.isGreen()) {
            other.changeTo("YELLOW");
            System.out.println(other.getStatus() + " (wait " + yellowTime + "s)");
        }

        // Make both lights red before changing to green
        other.changeTo("RED");
        target.changeTo("RED");

        System.out.println("Both RED: " + other.getStatus()
                + " | " + target.getStatus());

        // Give the green light to the selected road
        target.changeTo("GREEN");

        System.out.println(target.getStatus()
                + " (wait " + greenTime + "s)");
    }

    // Run one traffic cycle
    public void runCycle() {

        // Get the sensors and lights from the intersection
        ArrayList<Sensor> sensors = intersection.getSensors();
        ArrayList<TrafficLight> lights = intersection.getLights();

        // We need at least two sensors and two lights
        if (sensors.size() < 2 || lights.size() < 2) {
            System.out.println("Error: need 2 sensors and 2 lights");
            return;
        }

        // Get the number of cars on each road
        int c0 = sensors.get(0).getVehicleCount();
        int c1 = sensors.get(1).getVehicleCount();

        // Choose the road with priority
        int winner = decidePriority(c0, c1);
        int loser = 1 - winner;

        System.out.println("Counts -> road0: " + c0
                + ", road1: " + c1
                + " | Priority: road" + winner);

        // Change the traffic lights
        switchToGreen(lights.get(winner), lights.get(loser));

        // Save the last road that got green
        lastGreenIndex = winner;

        // Check if two lights are green at the same time
        if (intersection.hasConflict()) {
            System.out.println("WARNING: conflict, two lights are green!");
        }
    }
}
