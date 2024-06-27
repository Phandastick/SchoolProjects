/*
 * Methods:
 * - Receive request for runway
 * 
 * Process for plane to land:
 * - Plane requests to land
 * - ATC requests runway to clear out
 * - 
 */

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ATC {
    public RefuelingTruck truck;
    private Runway runway;
    private Gate g1, g2, g3;
    Semaphore sem;

    private ArrayList<Gate> gates;

    // initializing ATC, Runway, gates, refuel trucks
    public ATC(Semaphore sem) {
        System.out.println(colors.RED_BOLD + "Initializing ATC...");
        truck = new RefuelingTruck();
        runway = new Runway();
        gates = new ArrayList<Gate>();
        g1 = new Gate(this, 1);
        g2 = new Gate(this, 2);
        g3 = new Gate(this, 3);
        gates.add(g1);
        gates.add(g2);
        gates.add(g3);

        this.sem = sem;

        System.out.println(colors.BLUE + "\n Testing gates: ");

        for (Gate gate : gates) {
            System.out.println("Gate number " + gate.getID() + " : " + gate.getID());
            Thread gThread = new Thread(gate);
            gThread.start();
        }
        System.out.println(colors.RESET);
    }

    public boolean requestLanding(Plane plane) {
        synchronized (this) {
            try {
                sem.acquire(1);

                // synchronized (sem) {
                Gate gateCheck = checkGate();
                while (gateCheck == null) { // check if all gates are occupied;
                    // System.out.println(colors.RED_BOLD + Thread.currentThread().getName() + "is
                    // waiting " + colors.RESET);
                    System.out.println(colors.GREEN + "ATC: " + Thread.currentThread().getName() + " is waiting...");
                    wait();
                }
                // debug checks
                // System.out.println("Runway Occupied: " + runway.checkOccupied());
                // System.out.println(colors.RED_BOLD + "Gatecheck: " + gateCheck.getID());

                if (!runway.checkOccupied()) {
                    System.out.println(colors.GREEN + "ATC: Gate found for " + Thread.currentThread().getName()
                            + " gate " + gateCheck.getID() + colors.RESET);
                    System.out.println(colors.GREEN_BOLD + "ATC: " + colors.RESET + Thread.currentThread().getName()
                            + " cleared to taxi to gate " + gateCheck.getID());
                    runway.setPlane(plane);
                    runway.taxiPlane(plane, gateCheck, sem);
                    notifyAll();
                    System.out.println(colors.GREEN + "ATC: Notified everybody!");
                    return false;
                } else {
                    System.out.println(
                            colors.GREEN + "ATC: Landing rejected for " + Thread.currentThread().getName());
                    return true;
                    // }
                }

            } catch (InterruptedException e) {
            } finally {
                sem.release();
            }
            return true;

        }
    }

    public Gate checkGate() {
        for (Gate gate : gates) {
            if (gate.plane == null) {
                return gate;
            }
        }
        return null;
    }

    public static void main(String[] args) {

    }
}
