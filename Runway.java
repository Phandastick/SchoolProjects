/*
 * Methods:
 * - receive plane landing
 */

import java.util.concurrent.Semaphore;

public class Runway implements Runnable {
    private boolean occupied;
    private Plane plane;

    public Runway() {
        System.out.println("Initializing Runway...");
        this.occupied = false;
    }

    // Lands plane on runway
    public void setPlane(Plane plane) throws InterruptedException {
        this.plane = plane;
        this.occupied = true;
        Thread.sleep(1000);

        System.out.println(colors.BLACK + "Runway: " + Thread.currentThread().getName() + " has landed on the runway.");
    }

    public synchronized void taxiPlane(Plane plane, Gate gate, Semaphore sem) {
        // System.out.println(colors.RED_BOLD + "Runway: Calling taxi to gate
        // function");
        plane.taxiToGate(gate);
        this.plane = null;
        this.occupied = false;
        notifyAll();
        // System.out.println(colors.RED_BOLD + "Runway: Notify function called!");
        sem.release();
        System.out.println(colors.RED_BOLD + "Runway: Semaphore released!" + colors.RESET);

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
