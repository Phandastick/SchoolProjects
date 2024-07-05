import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class RefuelingTruck implements Runnable {
    @SuppressWarnings("unused")
    private final ATC atc;
    private final LinkedList<Gate> gates;
    private final Semaphore refuelLock = new Semaphore(1);

    public RefuelingTruck(ATC atc, LinkedList<Gate> gates) {
        System.out.println("Initializing Refueling truck...");
        this.atc = atc;
        this.gates = gates;
    }

    public void refuel(Plane plane) {
        try {
            synchronized (this) {
                refuelLock.acquire();
                System.out.println(c.truck + "Refuel truck: Refuelling plane " + Thread.currentThread().getName()
                        + "..." + c.RESET);
                int fuelcount = 0;
                while (fuelcount < 50)
                    try {
                        Thread.sleep(30);// total of 1500ms to refuel
                        plane.addFuel(1);
                        fuelcount = plane.getFuel();
                    } catch (InterruptedException e) {
                    }
                System.out
                        .println(c.truck + "Refuel truck: Plane " + plane.getId() + " refuelled & notified" + c.RESET);
                notifyAll();
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
                    for (Gate gate : gates) {
                        if (!gate.getPlane().isPrepared()) {
                            refuel(gate.getPlane());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
