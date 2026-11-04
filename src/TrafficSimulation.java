import org.w3c.dom.ls.LSOutput;

import java.util.Date;

public class TrafficSimulation {
    private static Date startTime;

    public static void main(String[] args) {
        startTime = new Date();
        System.out.println("SMART TRAFFIC LIGHT SYSTEM");
        System.out.println("Simulation started: " + startTime);
        System.out.println(); // to make it more organized in the output

        //now the devices
        Sensor sensorA = new Sensor("S1", "Street: A");
        Sensor sensorB = new Sensor("S2", "Street: B");
        TrafficLight lightA = new TrafficLight ("L1", "Street: A");
        TrafficLight lightB = new
        ("L2", "Street: B");

        Intersection intersection = new Intersection();
        intersection addDevice(sensorA);
        intersection.addDevice(sensorB);
        intersection.addDevice(lightA);
        intersection.addDevice(lightB);

        TrafficController controller = new TrafficController(intersection, 5, 10, 3); //(threshold, greentime, yellowtime)

        int [][] testCases = { //{a, b} -> streets
                {8, 2},
                {3, 12},
                {4, 4},
                {0, 0},
                {8,9},
                {200, 7},
                {4, 195},
                {65, 1},


        };
        System.out.println("\t part 1: test cases \t");
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("Test: " + (i + 1) +": Street A = " + testCases [i][0] + ", Street B = " + testCases[i][1]);
            sensorA.setVehicleCount(testCases[i][0]);
            sensorB.setVehicleCount(testCases[i][1]);
            runSafeCycle(controller, intersection);
        }
        System.out.println("Test " + (testCases.length + 1) + ": negative vehicle count (-5)");
        try {
            sensorA.setVehicleCount(-5);
            System.out.println("Error: negative count was accepted!");
        } catch(IllegalArgumentException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }
        System.out.println();//same thing here


    }  System.out.println("\t part 2: Rabdom cycles \t");
    int numberOfCycles = 5;
    for (int i = 1; i <= numberOfCycles; i++) {
        int countA = sensorA.generateRandomCount(30);
        int countB = sensorB.generateRandomCount(30);
        System.out.println("Random cycle " + i + ": Street A = " + countA + ", Street B = " countB);
        runSafecycle(controller, intersection);

        System.out.println("\tpart 3 FINAL STATUS\t");
        intersection.printALLStatus(); //each device prints
        System.out.println("Average vehicles on " + sensorA.getRoadName() + ": " sensorA.getAverageCount());
        System.out.println("Average vehicles on " + sensorB.getRoadName() + ": " sensorB.getAverageCount());
        System.out.println("Simulation finished.");
    }
    private static void runSafeCycle (TrafficController controller, Intersection intersection){
        try{
            controller.runCycle();
        } catch (IllegalStateException e) {
            System.out.println("safety error: " e.getMessage());
        }
        if (intersection.hasConflict()){
            System.out.println("Error: both lights are GREEN!");
        }
        System.out.println(); //again for the sake of keeping it organized
    }
}
