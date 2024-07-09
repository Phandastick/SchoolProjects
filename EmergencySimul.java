import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class EmergencySimul {
    private static Long startTime;
    private static LinkedList<Plane> planeList = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        ATC atc = new ATC();
        Random rand = new Random();
        int NumberOfPlanes = 6;
        int MaxMilliseconds = 50;
        int num;
        boolean Emergency = false;
        ArrayList<Thread> planes = new ArrayList<>();
        startTime = System.currentTimeMillis();

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
            planeList.add(plane);
            String name = "Plane" + num;
            Thread planeThread = new Thread(plane, name);
            planes.add(planeThread);
            planeThread.start();
            Thread.sleep(rand.nextInt(MaxMilliseconds));
        }

        for (Thread plane : planes) {
            plane.join();
        }

        // stops all threads
        atc.shutdown();
        Thread.sleep(2000);
        // check gates empty or not, print out statistics
        atc.sanityCheck();

        statisticsCheck();
    }

    public static double getTime() {
        long Endtime = System.currentTimeMillis();
        long duration = (Endtime - startTime);
        double seconds = duration / 1000.0; // seconds
        return seconds; // divide by 1 million to get milliseconds
    }

    public static void statisticsCheck() {
        String formatDouble = String.format("%.3f", getTime());
        System.out.println("Total simulated time (seconds`  ): " + formatDouble);
        for (Plane plane : planeList) {
            plane.showTime();
        }
        showWaitingStats();
    }

    public static void showWaitingStats() {
        double AVG = 0.0, MIN = Double.MAX_VALUE, MAX = 0.0;
        long Total = 0;
        int planeCount = 0;

        for (Plane plane : planeList) {
            Total += plane.waitingTime;
            planeCount++;

            if (plane.waitingTime > MAX) {
                MAX = plane.waitingTime;
            }
            if (plane.waitingTime < MIN) {
                MIN = plane.waitingTime;
            }
        }

        if (planeCount > 0) {
            AVG = (double) Total / planeCount;
        }

        // Convert milliseconds to seconds
        double totalSeconds = Total / 1000.0;
        double avgSeconds = AVG / 1000.0;
        double minSeconds = MIN / 1000.0;
        double maxSeconds = MAX / 1000.0;

        // print out result
        System.out.printf(c.GREEN + "Summary of Waiting Times:\n");
        System.out.printf("Average Waiting Time: %.2f seconds\n", avgSeconds);
        System.out.printf("Minimum Waiting Time: %.2f seconds\n", minSeconds);
        System.out.printf("Maximum Waiting Time: %.2f seconds\n", maxSeconds);
        System.out.printf("Total Waiting Time: %.2f seconds\n", totalSeconds);
        System.out.printf("Number of Planes: %d\n", planeCount);
    }

}