package sensor;

import java.util.ArrayList;
import java.util.Random;

public class Sensor {
    
    private int vehicleCount;
    private ArrayList<Integer> history;

    public Sensor() {
        this.vehicleCount = 0;
        this.history = new ArrayList<>();
    }

    public void setVehicleCount(int vehicleCount) {
        if (vehicleCount >= 0) {
            this.vehicleCount = vehicleCount;
            history.add(vehicleCount);
        } else {
            System.out.println("Error: Vehicle count cannot be negative!");
        }
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void generateRandomCount(int max) {
        if (max >= 0) {
            Random random = new Random();
            int randomCount = random.nextInt(max + 1);
            setVehicleCount(randomCount);
        } else {
            System.out.println("Error: Max value cannot be negative!");
        }
    }

    public ArrayList<Integer> getHistory() {
        return history;
    }

    public double getAverageCount() {
        if (history.isEmpty()) {
            return 0.0;
        }
        int sum = 0;
        for (int count : history) {
            sum += count;
        }
        return (double) sum / history.size();
    }

    public String getStatus() {
        return "Current Vehicle Count: " + vehicleCount + ", Average Count: " + getAverageCount();
    }

    @Override
    public String toString() {
        return "Sensor [Vehicle Count=" + vehicleCount + ", History=" + history + "]";
    }
}
