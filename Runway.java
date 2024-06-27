/*
 * Methods:
 * - receive plane landing
 */

import java.util.Currency;

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

    public void taxiPlane(Plane plane, Gate gate) {
        plane.taxiToGate(gate);
        this.plane = null;
        this.occupied = false;
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
