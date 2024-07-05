public class Gate implements Runnable {
    private final ATC atc;
    private Plane plane;
    private RefuelingTruck refuelingTruck;
    private final int ID;
    private final Runway runway;

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
                System.out.println(c.testing + "Gate " + this.ID + " waiting for refuel..." + c.r);
                refuelingTruck.wakeTruck(); // notifies truck
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(plane.toString()); // debug
        System.out.println(c.gate + "Gate " + this.getID() + ": refueled plane " + plane.getId());
        return true;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            synchronized (this) {
                System.out.println(c.gate + "Gate " + this.getID() + ": Running gate thread..." + c.r);
                while (true) { // keep repeating until no more planes arrive, then stop functions
                    while (plane == null) { // if there are no planes in gate or a plane has departed from a plane
                        System.out.println(c.gate + "Gate " + this.getID() + ": Awaiting Plane..." + c.r);
                        wait(); // waiting for plane to taxi to gate
                    }
                    System.out.println(c.gate + "Gate " + this.getID() + ": is awake!" + c.r);
                    synchronized (plane) {
                        plane.setPrepared(preparePlane(this.plane)); // do preparation for plane (disembark, supplies,
                                                                     // etc.)
                        System.out.println(c.gate + "Gate " + ID + ": prepared plane " + plane.getId());
                        plane.notify(); // notify plane that it is prepared
                        System.out.println(c.gate + "Gate " + ID + ": notified plane " + plane.getId());
                        wait(); // wait for plane to take off before deleting plane from gate's memory in order
                                // to get next plane
                        this.plane = null;
                    }
                    atc.addGate(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
