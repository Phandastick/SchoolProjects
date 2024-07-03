package V1;

public class RefuelingTruck implements Runnable {
    @SuppressWarnings("unused")
    private final ATC atc;

    public RefuelingTruck(ATC atc) {
        System.out.println("Initializing Refueling truck...");
        this.atc = atc;
    }

    public synchronized void refuel(Plane plane) {
        System.out.println(colors.unimportant + "Refuel truck: Refuelling plane " + Thread.currentThread().getName()
                + "..." + colors.RESET);
        int fuelcount = 0;
        while (fuelcount < 50)
            try {
                Thread.sleep(30);// total of 1500ms to refuel
                plane.addFuel(1);
                fuelcount = plane.getFuel();
            } catch (InterruptedException e) {
            }
        System.out.println(colors.truck + "Refuel truck: Plane " + plane.getID() + " refuelled" + colors.RESET);
    }

    @Override
    public void run() {

    }
}
