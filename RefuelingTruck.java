public class RefuelingTruck implements Runnable {
    private final ATC atc;

    public RefuelingTruck(ATC atc) {
        System.out.println("Initializing Refueling truck...");
        this.atc = atc;
    }

    public synchronized void refuel(Plane plane) {
        System.out.println(colors.truck + "Refuel truck: Refuelling plane " + Thread.currentThread().getName() + "..."
                + colors.RESET);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println(colors.truck + "Refuel truck: Plane " + plane.getID() + " refuelled" + colors.RESET);
    }

    @Override
    public void run() {

    }
}
