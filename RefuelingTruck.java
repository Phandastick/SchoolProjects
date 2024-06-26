public class RefuelingTruck implements Runnable {

    public RefuelingTruck() {
        System.out.println("Initializing Refueling truck...");
    }

    public synchronized void refuel(Gate gate) {
        System.out.println("Refuel truck: Refuelling plane " + Thread.currentThread().getName() + "...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println("Refuel truck: Plane " + gate.plane.getID() + " refuelled");
    }

    @Override
    public void run() {
    }
}
