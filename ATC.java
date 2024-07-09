import java.lang.invoke.VarHandle;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ATC implements Runnable {

    public RefuelingTruck truck;
    private LinkedList<Plane> landingQueue; // two queues to ensure that take off
    private LinkedList<Plane> takeoffQueue; // can happen first before landing
    private final Runway runway;
    private boolean shutdown = false; // only stop while loop when false

    private LinkedList<Gate> availableGates;
    private LinkedList<Gate> gates;

    private LinkedList<Thread> gateThreads = new LinkedList<>();
    private Thread truckThread;

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
            String gateName = "Gate" + i;
            Thread gThread = new Thread(gate, gateName);
            availableGates.addLast(gate); // add to gate queues
            gates.add(gate); // add to full list of gates
            gateThreads.add(gThread);
            gThread.start(); // start gate thread
        }

        this.truck = new RefuelingTruck();
        Thread truckThread = new Thread(truck);
        this.truckThread = truckThread;
        truckThread.start();

        System.out.println(c.init + "ATC has finished initializations");
    }

    public void handleLanding() {
        synchronized (runway) {
            System.out.println(c.atc + "ATC: ");
            while (landingQueue.peek() != null) {
                notifyAll();
                // handles a plane at a time
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

            if (plane.isEmergency()) { // add plane to the top if plane has emergency
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
            // Thread.sleep(1000);
            synchronized (this) {
                while (!shutdown) {
                    while (getEmergency()) {
                        handleEmergencyLanding();
                    }

                    if (takeoffQueue.peek() != null) {
                        handleTakeoff();
                    }

                    if (landingQueue.peek() != null) {
                        handleLanding();
                    }

                    System.out.println(c.atc + "ATC: Waiting..." + c.r);
                    if (landingQueue.peek() != null || takeoffQueue.peek() != null || getEmergency()) {
                        continue; // if one of the queue has a plane, it will not wait and handle additional
                                  // planes
                        // implement time out to avoid deadlock by notification before waiting
                    } else {
                        wait(4000);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // shutdown sequence for all threads after simulation finishes
            System.out.println(c.atc + "ATC: Shutting down simulation" + c.r);
            truck.shutdown(); // shutdown refuelling truck
            for (Gate gate : gates) {
                gate.shutdown();
            }
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

    public void sanityCheck() {
        System.out.println(c.atc + "==== Sanity check ==== \n" + c.r);
        checkEmpty();
    }

    public void checkEmpty() {
        for (Gate gate : gates) {
            try {
                System.out.println(c.atc + "ATC: Gate " + gate.getID() + "'s plane - " + gate.getPlane().getId() + c.r);
            } catch (NullPointerException e) {
                System.out.println(c.atc + "ATC: Gate " + gate.getID() + "'s plane does not exist." + c.r);
            }
        }
    }

    public void shutdown() {
        synchronized (this) {
            System.out.println(c.atc + "ATC: Shutting down...");
            this.shutdown = true;
            notify();
        }
    }
}