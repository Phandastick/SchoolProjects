import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class RefuelingTruck implements Runnable {
    private final LinkedList<Gate> gates;
    private final LinkedList<Gate> gateQueue = new LinkedList<>();
    private final Semaphore refuelLock = new Semaphore(1);

    public RefuelingTruck(LinkedList<Gate> gates) {
        System.out.println("Initializing Refueling truck...");
        this.gates = gates;
    }

    public void refuel(Gate gate) {
        try {
            synchronized (this) {
                refuelLock.acquire();
                Plane plane = gate.getPlane();
                System.out.println(
                        c.truck + "Refuel truck: Refuelling plane " + plane.getId() + "..." + c.r);
                int fuelcount = 0;
                while (fuelcount < 50)
                    try {
                        Thread.sleep(30);// total of 1500ms to refuel
                        plane.addFuel(1);
                        fuelcount = plane.getFuel();
                    } catch (InterruptedException e) {
                    }
                System.out.println(c.truck + "Refuel truck: Plane " + plane.getId() + " refuelled" + c.r);
                refuelLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                synchronized (this) {
                    System.out.println(c.truck + "Refuel truck: Waiting for planes to fuel" + c.r);
                    wait(); // wait for gate to call for fuelling
                    System.out.println(c.truck + "Refuel truck: notified" + c.r);
                    if (gateQueue.peek() != null) {
                        Gate currentGate = gateQueue.pop();
                        refuel(currentGate);
                    } else {
                        wait();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // join queue to request for refuel on gate
    public void requestRefuel(Gate gate) {
        gateQueue.addLast(gate);
    }

    // debugging purposes
    public void wakeTruck() {
        synchronized (this) {
            notify();
        }
    }
}
