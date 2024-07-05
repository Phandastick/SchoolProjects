import java.util.concurrent.Semaphore;

public class Runway implements Runnable {

    private Plane plane;
    private ATC atc;
    private Semaphore runwaySem;

    public Runway(ATC atc) {
        this.atc = atc;
        this.plane = null;
        this.runwaySem = new Semaphore(1);
    }

    public void handleLanding(Plane plane) {
        synchronized (plane) {
            try {
                // landing sequence
                System.out.println(c.plane + "Runway: Trying to get runway lock" + c.r);
                runwaySem.acquire(); // locks runway
                System.out.println(c.runway + "Runway: plane " + plane.getId() + " has acquired lock" + c.r);
                plane.land();
                System.out.println(c.runway + "Runway: plane " + plane.getId() + " has landed on runway" + c.r);
                this.plane = plane;

                // taxi sequence
                plane.taxiToGate(); // taxi to gate
                runwaySem.release(); // taxi sequence completes

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void land(Plane plane) {
        try {
            System.out.println(c.runway + "Runway: receving plane " + plane.getId() + " landing" + c.r);
            Thread.sleep(1850);
            this.plane = plane;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        /*
         * Runways arent supposed to do anything, but just receive plane and taxi them
         * to
         */
        try {
            synchronized (this) {
                while (true) {
                    wait(); // wait for notification that plane is queued
                    // handleTakeoff();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
