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
    private Semaphore sem;

    private ArrayList<Gate> gates;

    // initializing ATC, Runway, gates, refuel trucks
    public ATC(Semaphore sem) {
        System.out.println(colors.RED_BOLD + "Initializing ATC...");
        truck = new RefuelingTruck(this);
        runway = new Runway(this);
        gates = new ArrayList<Gate>();
        g1 = new Gate(this, 1, sem);
        g2 = new Gate(this, 2, sem);
        g3 = new Gate(this, 3, sem);
        gates.add(g1);
        gates.add(g2);
        gates.add(g3);

        this.sem = sem;

        // System.out.println(colors.BLUE + "\n Testing gates: ");

        for (Gate gate : gates) {
            // System.out.println("Gate number " + gate.getID() + " : " + gate.getID());
            String gateName = "Gate " + gate.getID();
            Thread gThread = new Thread(gate, gateName);
            gThread.start();
        }

        // Thread threadRunway = new Thread(runway, "Runway");
        // threadRunway.start();
        // System.out.println(colors.RESET);
    }

    public boolean requestLanding(Plane plane) {
        try {
            // synchronizing the ATC and the planes on Semaphore lock to notify when a gate
            // is empty AND the runway is clear
            synchronized (this) {
                sem.acquire(1);
                Gate gateCheck = checkGate();
                while (gateCheck == null) { // check if all gates are occupied;
                    // System.out.println(colors.RED_BOLD + Thread.currentThread().getName() + "is
                    // waiting " + colors.RESET);
                    System.out.println(
                            colors.atc + "Plane " + plane.getID() + ": is awaiting landing request..." + colors.RESET);
                    Thread.currentThread().wait();
                }
                // debug checks
                // System.out.println("Runway Occupied: " + runway.checkOccupied());
                // System.out.println(colors.RED_BOLD + "Gatecheck: " + gateCheck.getID());

                while (!runway.checkOccupied()) {
                    System.out.println(colors.atc + "ATC: Gate found for Plane " + plane.getID() + " at gate "
                            + gateCheck.getID() + colors.RESET);
                    notifyAll();
                    Thread.sleep(200);
                    runway.setPlane(plane);
                    Thread.sleep(200);
                    runway.taxiPlane(plane, gateCheck, sem);
                    // notifyAll();
                    // System.out.println(colors.atc + "ATC: Notified everybody!" + colors.RESET);
                    return false;
                }
                System.out.println(colors.GREEN + "ATC: Landing rejected for " + Thread.currentThread().getName()
                        + colors.RESET);
                sem.release();
                return true;
            }
        } catch (InterruptedException e) {
        } finally {
            sem.release();
        }
        return true;
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
