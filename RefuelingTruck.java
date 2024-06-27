public class RefuelingTruck implements Runnable {

    public RefuelingTruck() {
        System.out.println("Initializing Refueling truck...");
    }

    public synchronized void refuel(Plane plane) {
        System.out.println(colors.BLUE + "Refuel truck: Refuelling plane " + Thread.currentThread().getName() + "...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println(colors.BLUE + "Refuel truck: Plane " + plane.getID() + " refuelled");
    }

    @Override
    public void run() {

    }
}
