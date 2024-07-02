/*
 * Methods:
 * - receive plane landing
 */

import java.util.concurrent.Semaphore;

public class Runway {
    private boolean occupied;
    private Plane plane;
    private final ATC atc;
    private final Semaphore runwayMutex;

    public Runway(ATC atc, Semaphore runwayMutex) {
        System.out.println("Initializing Runway...");
        this.occupied = false;
        this.atc = atc;
        this.runwayMutex = runwayMutex;
        plane = null;
    }

    // Lands plane on runway
    public void setPlane(Plane plane) {
        try {
            if (occupied) {
                throw new InterruptedException(colors.RED_BOLD
                        + "Why tf are you here runway's set plane aint ready begone thot" + colors.RESET);
            }
            System.out.println(colors.runway + "Runway: receiving plane " + plane.getID() + colors.RESET);
            Thread.sleep(1000);
            this.plane = plane;
            this.occupied = true;
            System.out.println(
                    colors.BLACK + "Runway: plane " + plane.getID() + " has landed on the runway." + colors.RESET);

        } catch (Exception e) {
        }
    }

    public void taxiPlane(Gate gate) {
        synchronized (atc) {
            // System.out.println(colors.RED_BOLD + "Runway: Calling taxi to gate
            // function");
            // put while loop to check if not occupied,
            this.plane.taxiToGate(gate);
            // plane is no longer in the runway
            this.plane = null;
            this.occupied = false;
            // runwayMutex.release();
            System.out.println(colors.RED_BOLD + "Runway: runwayMutex released!" + colors.RESET);
            // notify();
            // System.out.println(colors.runway + "Runway: Notified everybody!" +
            // colors.RESET);
        }
    }

    // tells which plan is on runway
    public Plane getPlane() {
        return plane;
    }

    // is runway avaialble
    public boolean checkOccupied() {
        return occupied;
    }

    // @Override
    // public void run() {
    // try {
    // synchronized (atc) {
    // while (!occupied || plane == null) {
    // System.out.println(colors.runway + "Runway: Waiting..." + colors.RESET);
    // wait();
    // if (plane != null || occupied) {

    // }
    // }
    // }
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
}
