import java.util.Random;

/*
 Deliverables:
 For this exercise, you are to model the ATC scenario and design a Java program to simulate activity for the airport:
• Altogether, 6 planes should try to land at the airport.
• Use a random number generator, so a new airplane arrives every 0, 1, or 2 seconds. (This might be accomplished by an appropriate statement sleep (rand.nextInt(2000));
• Assume each plane can accommodate maximum 50 passengers.
• Assume passengers are always ready to embark and disembark the terminal (i.e., no capacity issues inside the passenger terminals)
 */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ATC atc = new ATC();
        Random rand = new Random();
        int NumberOfPlanes = 6;
        int MaxMilliseconds = 2000;

        for (int i = 1; i <= NumberOfPlanes; i++) {
            Plane plane = new Plane(i, atc);
            Thread planeThread = new Thread(plane);
            planeThread.start();
            Thread.sleep(rand.nextInt(MaxMilliseconds));
        }
    }
}
