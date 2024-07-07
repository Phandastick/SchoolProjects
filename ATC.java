import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.SynchronousQueue;

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

        this.truck = new RefuelingTruck(gates);
        Thread truckThread = new Thread(truck);
        truckThread.start();

        // Thread runwayThread = new Thread(runway);
        // runwayThread.start();
        System.out.println(c.init + "ATC has finished initializations");
    }

    public void handleLanding() {
        synchronized (runway) {
            System.out.println(c.atc + "ATC: ");
            while (landingQueue.peek() != null) {
                notifyAll();
                runway.handleLanding(landingQueue.pop());
            }
        }
    }

    public void handleTakeoff() {
        synchronized (runway) {
            while (takeoffQueue.peek() != null) {
                runway.handleTakeoff(takeoffQueue.pop());
            }
        }
    }

    public void handleEmergencyLanding() {
        // possible case when gates are all full and plane needs emergency landing
        synchronized (runway) {
            runway.handleLanding(landingQueue.pop());
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
            notifyAll(); // notifies the runway to handle the next landing
        }
        return gateChecked;
    }

    public void requestTakeoff(Plane plane) {
        synchronized (this) {
            takeoffQueue.add(plane);
            notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                synchronized (this) {
                    while (true) {
                        while (getEmergency()) {
                            handleEmergencyLanding();
                        }

                        if (takeoffQueue.peek() != null) {
                            handleTakeoff();
                            System.out.println(c.testing + "ATC: Finished take off handle" + c.r);
                        }

                        if (landingQueue.peek() != null) {
                            handleLanding();
                            System.out.println(c.testing + "ATC: Finished landing handle" + c.r);
                        }

                        System.out.println(c.atc + "ATC: Waiting..." + c.r);
                        System.out.println(c.testing + "ATC: " + landingQueue.toString() + c.r);
                        System.out.println(c.testing + "ATC: " + takeoffQueue.toString() + c.r);
                        if (landingQueue.peek() != null || takeoffQueue.peek() != null || getEmergency()) {
                            continue; // if one of the queue has a plane, it will not wait and handle additional
                                      // planes
                            // implement time out to avoid deadlock by notification before waiting
                        } else {
                            wait(4000);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean getEmergency() {
        if (landingQueue.peek() != null) {
            return landingQueue.peek().isEmergency();
        } else {
            return false;
        }
    }

    public Plane getNextLanding() {
        // returns and removes the first plane, emergency planes is also in first
        return landingQueue.pop();
    }

    public void addGate(Gate gate) {
        synchronized (this) {
            availableGates.addLast(gate);
            notify();
        }
    }

    public Gate checkGates() {
        try {
            return availableGates.pop();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}