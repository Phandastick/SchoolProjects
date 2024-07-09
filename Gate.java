public class Gate implements Runnable {
    private final ATC atc;
    private Plane plane;
    private RefuelingTruck refuelingTruck;
    private final int ID;
    private final Runway runway;
    private boolean running = true;
    private long preparePlaneRequestTime;
    private long planePreparedTime;

    public Gate(ATC atc, int id, Runway runway) {
        this.atc = atc;
        this.ID = id;
        this.runway = runway;
        plane = null;
    }

    public boolean preparePlane(Plane plane) {// disembark, clean, resupply, refuel, embark
        System.out.println(c.gate + "Gate " + this.getID() + ": preparing plane " + plane.getId() + c.r);
        this.refuelingTruck = atc.truck;
        refuelingTruck.requestRefuel(this); // use gate to request, because we call the truck to the gate not the plane
        refuelingTruck.wakeTruck(); // notifies truck
        plane.disembark();
        plane.resupply(); // 1000ms
        plane.clean(); // 500 ms

        plane.embark();
        while (plane.getFuel() < 50) {// refuel: 1500 ms
            try {
                refuelingTruck.wakeTruck(); // notifies truck
                wait(1600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // System.out.println(plane.toString()); // debug
        System.out.println(c.gate + "Gate " + this.getID() + ": refueled plane " + plane.getId() + c.r);
        return true;
    }

    @Override
    public void run() {
        try {
            // Thread.sleep(1000);
            synchronized (this) {
                System.out.println(c.gate + "Gate " + this.getID() + ": Running gate thread..." + c.r);
                wait();
                while (running) { // keep repeating until no more planes arrive, then stop functions
                    System.out.println(
                            c.gate + "Gate " + this.getID() + ": is awake with plane " + plane.getId() + "!" + c.r);
                    plane.setPrepared(preparePlane(this.plane)); // do preparation for plane (disembark, supplies,
                                                                 // etc.)
                    System.out.println(c.gate + "Gate " + ID + ": prepared plane " + plane.getId() + c.r);
                    wait(); // wait for plane to take off before deleting plane from gate's memory in order
                            // to get next plane
                    atc.addGate(this); // add gate back to queue for planes to be assigned to
                    while (plane == null && running) { // if there are no planes in gate or a plane has departed from a
                                                       // plane
                        System.out.println(c.gate + "Gate " + this.getID() + ": Awaiting Plane..." + c.r);
                        wait(); // waiting for plane to taxi to gate
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearPlane() {
        synchronized (this) {
            this.plane = null;
            notify(); // notifies the gate to continue to wait for next plane
        }
    }

    public void shutdown() {
        synchronized (this) {
            System.out.println(c.gate + "Gate " + this.getID() + ": Shutting down..." + c.r);
            this.running = false;
            notify();
        }
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public int getID() {
        return this.ID;
    }

}
