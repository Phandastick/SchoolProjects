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
import java.util.Currency;
import java.util.concurrent.Semaphore;

public class ATC {
    public RefuelingTruck truck;
    private Runway runway;
    private Gate g1, g2, g3;
    Semaphore sem;

    private ArrayList<Gate> gates;

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
        }
        System.out.println(colors.RESET);
    }

    public void requestLanding(Plane plane) {
        try {
            sem.acquire();

            synchronized (sem) {
                Gate gateCheck = checkGate();
                if (gateCheck == null) { // check if all gates are occupied
                    System.out
                            .println(colors.RED_BOLD + Thread.currentThread().getName() + "is waiting " + colors.RESET);
                }
                // debug checks
                // System.out.println("Runway Occupied: " + runway.checkOccupied());
                // System.out.println(colors.RED_BOLD + "Gatecheck: " + gateCheck.getID());

                if (!runway.checkOccupied()) {
                    System.out.println(colors.GREEN + "ATC: Gate found for " + Thread.currentThread().getName()
                            + " gate " + gateCheck.getID() + colors.RESET);
                    System.out.println(colors.GREEN_BOLD + "ATC: " + colors.RESET + Thread.currentThread().getName()
                            + " cleared to land at gate " + gateCheck.getID());
                    try {
                        runway.setPlane(plane);
                        runway.taxiPlane(plane, gateCheck);
                        notify();
                    } catch (InterruptedException e) {
                    }
                } else {
                    System.out.println(
                            colors.GREEN + "ATC: Landing rejected for " + Thread.currentThread().getName());
                }
            }

        } catch (InterruptedException e) {
        } finally {
            sem.release();
        }
    }

    public synchronized void requestGate() {

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
