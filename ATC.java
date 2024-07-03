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
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ATC {
    public RefuelingTruck truck;
    private LinkedList<Plane> planeQueue;
    private final Runway runway;
    private final Semaphore runwayMutex = new Semaphore(1);

    private Gate g1, g2, g3;
    private ArrayList<Gate> gates;

    // initializing ATC, Runway, gates, refuel trucks
    public ATC() {
        System.out.println(colors.RED_BOLD + "Initializing ATC...");
        this.planeQueue = new LinkedList<>();
        this.truck = new RefuelingTruck(this);
        this.runway = new Runway(this, runwayMutex);
        this.gates = new ArrayList<Gate>();
        g1 = new Gate(this, 1, runwayMutex, runway);
        g2 = new Gate(this, 2, runwayMutex, runway);
        g3 = new Gate(this, 3, runwayMutex, runway);
        gates.add(g1);
        gates.add(g2);
        gates.add(g3);

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

    public void requestLanding(Plane plane) {
        try {
            synchronized (this) {
                System.out.println(colors.atc + "ATC: Synchronized with " + plane.getID() + colors.RESET);

                Gate gateChecked = checkGate();

                while (gateChecked == null) {
                    System.out
                            .println(colors.atc + "ATC: plane " + plane.getID() + " waiting for gates" + colors.RESET);
                    System.out.println(colors.testing + "Queue: " + planeQueue.toString());

                    wait(); // wait until there are gates free, gate takeoff should notify this wait()
                    gateChecked = checkGate();
                }

                planeQueue.add(plane);
                while (planeQueue.peek() != plane) {
                    System.out.println(colors.atc + "ATC: Plane " + plane.getID() + " waiting in queue");
                    System.out.println(colors.testing + "Queue: " + planeQueue.toString());
                    notifyAll(); // if there are planes who are not executed for some reason
                    wait(); // wait until notifies for the next plane to land
                }

                System.out.println(colors.atc + "ATC: plane " + plane.getID() + " cleared to land at gate "
                        + gateChecked.getID() + colors.RESET);
                runway.landPlane(plane); // set plane on runway
                runway.taxiPlane(gateChecked); // set plane in gate
                notifyAll(); // notify for the next plane to use runway
                planeQueue.poll(); // remove plane from queue
                System.out.println(colors.atc + "ATC: notified all" + colors.RESET);
            }
        } catch (InterruptedException e) {
        } finally {
        }
    }

    public void requestTakeoff(Plane plane) {
        try {
            synchronized (this) {
                while (runway.checkOccupied()) {
                    System.out
                            .println(colors.atc + "ATC: plane " + plane.getID() + " waiting for runway to be free"
                                    + colors.RESET);
                    wait(); // wait until runway free
                }

                planeQueue.add(plane);
                while (planeQueue.peek() != plane) {
                    System.out.println(colors.atc + "ATC: Plane " + plane.getID() + " waiting in queue");
                    wait(); // wait until notifies for the next plane to land
                }
                runway.gateToRunway(plane);
                runway.takeOff(plane);
                planeQueue.poll(); // remove plane from queue
                notifyAll(); // notify for the next plane to use runway
                System.out.println(colors.atc + "ATC: notified all" + colors.RESET);
            }
        } catch (InterruptedException e) {
        } finally {
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
