import java.util.Date;

/** Application entry point. Open this file in IntelliJ and run main(). */
public class TrafficSimulation {
    private static Date startTime;

    public static void main(String[] args) {
        try {
            run(args);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            System.err.println("Error: " + exception.getMessage());
            System.err.println("Use --help for the available program arguments.");
            System.exit(1);
        }
    }

    private static void run(String[] args) {
        int cycles = 6;
        int threshold = 5;
        int maximumCount = 30;
        int greenTime = 1000;
        int yellowTime = 500;
        long seed = 42;
        boolean demo = false;
        boolean fast = false;

        for (String argument : args) {
            if ("--help".equals(argument)) {
                printHelp();
                return;
            } else if ("--demo".equals(argument)) {
                demo = true;
            } else if ("--fast".equals(argument)) {
                fast = true;
            } else if (argument.startsWith("--cycles=")) {
                cycles = nonnegativeInt(argument, "--cycles=");
                if (cycles == 0) {
                    throw new IllegalArgumentException("Cycle count must be positive.");
                }
            } else if (argument.startsWith("--threshold=")) {
                threshold = nonnegativeInt(argument, "--threshold=");
            } else if (argument.startsWith("--max=")) {
                maximumCount = nonnegativeInt(argument, "--max=");
                if (maximumCount == Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("--max must be less than 2147483647.");
                }
            } else if (argument.startsWith("--green-ms=")) {
                greenTime = nonnegativeInt(argument, "--green-ms=");
            } else if (argument.startsWith("--yellow-ms=")) {
                yellowTime = nonnegativeInt(argument, "--yellow-ms=");
            } else if (argument.startsWith("--seed=")) {
                try {
                    seed = Long.parseLong(argument.substring("--seed=".length()));
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("--seed requires a whole number in long range.");
                }
            } else {
                throw new IllegalArgumentException("Unknown argument: " + argument);
            }
        }
        if (fast) {
            greenTime = 0;
            yellowTime = 0;
        }

        startTime = new Date();
        Intersection intersection = new Intersection("Campus Intersection");
        Sensor sensorA = new Sensor("S-A", "Road A", seed);
        Sensor sensorB = new Sensor("S-B", "Road B", seed + 1);
        TrafficLight lightA = new TrafficLight("L-A", "Road A");
        TrafficLight lightB = new TrafficLight("L-B", "Road B");

        intersection.addDevice(sensorA);
        intersection.addDevice(sensorB);
        intersection.addDevice(lightA);
        intersection.addDevice(lightB);

        TrafficController controller = new TrafficController(
                intersection, threshold, greenTime, yellowTime);
        int[][] demoCounts = {
                {20, 5}, {5, 20}, {10, 10},
                {10, 10}, {15, 10}, {16, 10}
        };

        System.out.println("Java OOP Traffic Simulation");
        System.out.println("Started: " + startTime);
        System.out.println("Mode: " + (demo ? "fixed demo" : "seeded random")
                + " | cycles=" + cycles + " | threshold=" + threshold);
        System.out.println("Timing: green=" + greenTime + " ms | yellow="
                + yellowTime + " ms | seed=" + seed);
        if (demo) {
            System.out.println("Demo uses fixed counts; --max and --seed do not affect them.");
        }
        intersection.printAllStatus();

        for (int cycle = 0; cycle < cycles; cycle++) {
            if (demo) {
                int[] counts = demoCounts[cycle % demoCounts.length];
                sensorA.setVehicleCount(counts[0]);
                sensorB.setVehicleCount(counts[1]);
            } else {
                sensorA.generateRandomCount(maximumCount);
                sensorB.generateRandomCount(maximumCount);
            }
            controller.runCycle();
        }

        System.out.println();
        System.out.println("Simulation complete. Completed cycles: " + controller.getCycleNumber());
        System.out.println("Final sensor summaries:");
        System.out.println("  " + sensorA);
        System.out.println("  " + sensorB);
        System.out.println("Both lights RED: "
                + (TrafficLight.RED.equals(lightA.getState())
                && TrafficLight.RED.equals(lightB.getState())));
        System.out.println("Conflict detected: " + intersection.hasConflict());
    }

    private static int nonnegativeInt(String argument, String prefix) {
        try {
            int value = Integer.parseInt(argument.substring(prefix.length()));
            if (value < 0) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(prefix + " requires a nonnegative int value.");
        }
    }

    private static void printHelp() {
        System.out.println("Usage: TrafficSimulation [options]");
        System.out.println("  --demo           Use the six fixed classroom examples (repeat if needed).");
        System.out.println("  --fast           Use zero waiting times, overriding timing options.");
        System.out.println("  --cycles=N       Positive number of cycles (default 6).");
        System.out.println("  --threshold=N    Nonnegative priority threshold (default 5).");
        System.out.println("  --max=N          Inclusive random maximum, 0..2147483646 (default 30).");
        System.out.println("  --seed=N         Random seed, a long integer (default 42).");
        System.out.println("  --green-ms=N     Green phase milliseconds, nonnegative (default 1000).");
        System.out.println("  --yellow-ms=N    Yellow phase milliseconds, nonnegative (default 500).");
        System.out.println("  --help           Print this help and exit.");
    }
}