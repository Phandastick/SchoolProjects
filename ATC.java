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

public class ATC {
    public RefuelingTruck truck;
    private Runway runway;
    private Gate g1, g2, g3;

    private ArrayList<Gate> gates;

    public ATC() {
        System.out.println("Initializing ATC...");
        runway = new Runway();
        gates = new ArrayList<Gate>();
        g1 = new Gate(this, 1);
        g2 = new Gate(this, 2);
        g3 = new Gate(this, 3);
        gates.add(g1);
        gates.add(g2);
        gates.add(g3);

        System.out.println("\n Testing gates: ");

        for (Gate gate : gates) {
            System.out.println("Gate number one: " + gate.getID());
        }

        truck = new RefuelingTruck();
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

    public synchronized void requestLanding(Plane plane) {
        while (true) {
            if (runway.checkOccupied() && checkGate()) {
                System.out.println("Plane " + plane.getID() + " waiting for runway to clear");
            }
        }
    }
}
