package cs2.project;
public class CS2Project {

    public static void main(String[] args) {
        TrafficDevice device = new TrafficDevice("A1" , "Street A");
        System.out.println("Device id: " + device.getDeviceId());
        System.out.println("road name: "+ device.getRoadName());
    }
   
}

