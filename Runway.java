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
    public void landPlane(Plane plane) {
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
            e.printStackTrace();
        }
    }

    public void taxiPlane(Gate gate) {
        synchronized (this) {
            // System.out.println(colors.RED_BOLD + "Runway: Calling taxi to gate
            // function");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.plane.taxiToGate(gate);
            // plane is no longer in the runway
            this.plane = null;
            this.occupied = false;
            notifyAll();
            System.out.println(colors.RED_BOLD + "Runway: runwayMutex released!" + colors.RESET);
        }
    }

    public void takeOff(Plane plane) {
        try {
            if (occupied) {
                throw new InterruptedException("Go away plane aint taking off broski");
            }

            System.out.println(colors.runway + "Runway: Plane " + plane.getID() + " is taking off on runway");
            Thread.sleep(1000);
            this.plane = null;
            this.occupied = false;
            System.out.println(
                    colors.runway + "Runway: plane " + plane.getID() + " has took off from the runway." + colors.RESET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gateToRunway(Plane plane) {
        System.out.println(colors.plane + "Plane " + plane.getID() + ": is taxiing to the runway");

    }

    // tells which plan is on runway
    public Plane getPlane() {
        return plane;
    }

    // is runway avaialble
    public boolean checkOccupied() {
        return occupied;
    }
}
