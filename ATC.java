import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ATC implements Runnable {

    public RefuelingTruck truck;
    private LinkedList<Plane> landingQueue; // two queues to ensure that take off
    private LinkedList<Plane> takeoffQueue; // can happen first before landing
    private final Runway runway;

    private LinkedList<Gate> availableGates;
    private LinkedList<Gate> gates;

    // initializing ATC, Runway, gates, refuel trucks
    public ATC() {
        System.out.println(c.RED_BOLD + "Initializing ATC...");
        this.landingQueue = new LinkedList<>();
        this.takeoffQueue = new LinkedList<>();
        this.runway = new Runway(this);
        this.availableGates = new LinkedList<Gate>();
        this.gates = new LinkedList<Gate>();

        this.truck = new RefuelingTruck(gates);
        Thread truckThread = new Thread(truck);
        truckThread.start();

        for (int i = 1; i <= 3; i++) {
            Gate gate = new Gate(this, i, runway);
            Thread gThread = new Thread(gate);
            availableGates.addLast(gate); // add to gate queues
            gates.add(gate); // add to full list of gates
            gThread.start(); // start gate thread
        }

        for (Gate gate : availableGates) {
            // System.out.println("Gate number " + gate.getID() + " : " + gate.getID());
            String gateName = "Gate " + gate.getID();
            Thread gThread = new Thread(gate, gateName);
            gThread.start();
        }

        // Thread runwayThread = new Thread(runway);
        // runwayThread.start();
    }

    public void handleLanding() {
        synchronized (runway) {
            while (landingQueue.peek() != null) {
                notify();
                runway.handleLanding(landingQueue.pop());
            }
        }
    }

    public void handleTakeoff() {

    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                synchronized (this) {
                    if (takeoffQueue.peek() != null) {
                        handleTakeoff();
                    }

                    if (landingQueue.peek() != null) {
                        handleLanding();
                    }
                    System.out.println(c.atc + "ATC: Waiting..." + c.r);
                    wait(); // waiting for runway for planes to be queued
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Gate requestLanding(Plane plane) {
        Gate gateChecked = checkGates();
        synchronized (this) {
            // add plane to queue
            while (gateChecked == null) {
                try {
                    System.out.println(c.atc + "ATC: plane " + plane.getId() + " waiting for gates");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gateChecked = checkGates();
            }

            if (plane.isEmergency()) {
                landingQueue.addFirst(plane);
            } else {
                landingQueue.addLast(plane);
            }

            System.out.println(c.atc + "ATC: plane " + plane.getId() + " added to queue" + c.r);
            notify();

            // runway calls atc.getLanding/Takeoff (prioritise takeoff)
            // check gate free
            // check runway free
            // call lock runway for plane
            // land on runway
            // notify runway
            // when plane == null in runway
            // runway should release lock

        }
        System.out.println(c.testing + "ATC: plane " + plane.getId() + " returned gate " + gateChecked.getID() + c.r);

        return gateChecked;
    }

    public Plane getNextLanding() {
        // returns and removes the first plane, emergency planes is also in first
        return landingQueue.pop();
    }

    public void addGate(Gate gate) {
        availableGates.addLast(gate);
    }

    public Gate checkGates() {
        try {
            return availableGates.pop();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}