/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cs2.project;

import java.util.ArrayList;

public class Intersection {

    private String name;
    private ArrayList<TrafficDevice> devices;

    public Intersection(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
    }

    public void addDevice(TrafficDevice d) {
        if (d == null) {
            throw new IllegalArgumentException("Device cannot be null");
        }
        if (devices.contains(d)) {
            throw new IllegalArgumentException("Duplicate device ID: " + d.getDeviceId());
        }
        devices.add(d);
    }

//    public ArrayList<Sensor> getSensors() {
//        ArrayList<Sensor> sensors = new ArrayList<>();
//        for (TrafficDevice d : devices) {
//            if (d instanceof Sensor) {
//                sensors.add((Sensor) d);
//            }
//        }
//        return sensors;
//    }

    public ArrayList<TrafficLight> getLights() {
        ArrayList<TrafficLight> lights = new ArrayList<>();
        for (TrafficDevice d : devices) {
            if (d instanceof TrafficLight) {
                lights.add((TrafficLight) d);
            }
        }
        return lights;
    }

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
        for (TrafficDevice d : devices) {
            System.out.println("  " + d.getStatus());
        }
    }
}
