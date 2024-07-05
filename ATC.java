import java.util.LinkedList;

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

        this.truck = new RefuelingTruck(this, gates);
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

        Thread threadRunway = new Thread(runway, "Runway");
        threadRunway.start();
        System.out.println(c.init + "Starting Runway thread" + c.RESET);
    }

    public synchronized void handleLanding() {
        if (landingQueue.peek() != null) {
            Plane plane = landingQueue.pop();
            plane.clearLand(runway);
            System.out.println(c.atc + "ATC: Cleared plane " + plane.getId() + "'s landing");
        }
    }

    public void handleTakeoff() {

    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this) {
                    wait();
                    if (takeoffQueue.peek() != null) {
                        handleTakeoff();
                    }

                    if (landingQueue.peek() != null) {
                        handleLanding();
                    }
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
            // wakes up runway
            notify(); // put at runnable

            // runway calls atc.getLanding/Takeoff (prioritise takeoff)
            // check gate free
            // check runway free
            // call lock runway for plane
            // land on runway
            // notify runway
            // when plane == null in runway
            // runway should release lock

        }

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
        return availableGates.pop();
    }
}