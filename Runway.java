/*
 * Methods:
 * - receive plane landing
 */

import java.util.concurrent.Semaphore;

public class Runway implements Runnable {
    private boolean occupied;
    private Plane plane;
    private final ATC atc;

    public Runway(ATC atc) {
        System.out.println("Initializing Runway...");
        this.occupied = false;
        this.atc = atc;
    }

    // Lands plane on runway
    public synchronized void setPlane(Plane plane) throws InterruptedException {

        while (occupied) {
            System.out.println(
                    colors.runway + "Runway: waiting for runway to clear for plane " + plane.getID() + colors.RESET);
        }

        this.plane = plane;
        this.occupied = true;
        Thread.sleep(1000);

        System.out.println(colors.BLACK + "Runway: plane " + plane.getID() + " has landed on the runway.");
        notifyAll();
    }

    public void taxiPlane(Plane plane, Gate gate, Semaphore sem) {
        synchronized (atc) {
            // System.out.println(colors.RED_BOLD + "Runway: Calling taxi to gate
            // function");
            // put while loop to check if not occupied,
            while (!occupied || this.plane != null) {
                System.out.println(colors.runway + "Runway: Waiting for plane..." + colors.RESET);
                try {
                    atc.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            plane.taxiToGate(gate);
            this.plane = null;
            this.occupied = false;
            atc.notifyAll();
            System.out.println(colors.runway + "Runway: Notified everybody!" + colors.RESET);
            sem.release();
            System.out.println(colors.RED_BOLD + "Runway: Semaphore released!" + colors.RESET);
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

    @Override
    public void run() {
    }
}
