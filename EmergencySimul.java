import java.util.Random;

public class EmergencySimul {
    public static void main(String[] args) throws InterruptedException {
        ATC atc = new ATC();
        Random rand = new Random();
        int NumberOfPlanes = 6;
        int MaxMilliseconds = 50;
        int num;
        boolean Emergency = false;

        Thread atcThread = new Thread(atc);
        atcThread.start();

        for (int i = 1; i <= NumberOfPlanes; i++) {
            if (i == 3) {
                Emergency = true;
                num = 99;
            } else {
                Emergency = false;
                num = i;
            }
            Plane plane = new Plane(num, atc, Emergency);
            String name = "Plane" + num;
            Thread planeThread = new Thread(plane, name);
            planeThread.start();
            Thread.sleep(rand.nextInt(MaxMilliseconds));
        }

    }
}