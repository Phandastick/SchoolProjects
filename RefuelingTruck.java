public class RefuelingTruck implements Runnable {
    Plane plane;

    public synchronized void refuel(Gate gate) {
        System.out.println("Refuelling plane " + plane.getID() + "...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println("Plane " + plane.getID() + " refuelled");
    }

    @Override
    public void run() {
    }
}
