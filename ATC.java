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

    public ATC(Semaphore sem) {
        System.out.println(colors.RED_BOLD + "Initializing ATC...");
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
        truck = new RefuelingTruck();
    }

    public synchronized void requestLanding(Plane plane) {
        while (true) {
            if (runway.checkOccupied() && checkGate()) {
                System.out.println("Plane " + plane.getID() + " waiting for runway to clear");
            }
        }
    }

    public synchronized void requestGate() {

    }

    public boolean checkGate() {
        for (Gate gate : gates) {
            if (gate.check()) {
                return true;
            }
        }
        return false;
    }
}
