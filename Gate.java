
public class Gate implements Runnable {
    ATC atc;
    Plane plane;
    RefuelingTruck refuelingTruck;
    private int ID;

    public Gate(ATC atc, int id) {
        this.atc = atc;
        this.ID = id;
        this.refuelingTruck = atc.truck;
        plane = null;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
        // preparePlane(plane);
    }

    public synchronized void boardPlane(Plane plane) throws InterruptedException {
        System.out
                .println("Gate " + this.getID() + ":  Passenger boarding " + Thread.currentThread().getName() + "...");
        plane.embark();
    }

    public void preparePlane(Plane plane) {
        System.out
                .println("Gate " + this.getID() + ": " + Thread.currentThread().getName() + " is being disembarked...");
        plane.disEmbark();

        // 2000 to clean plane
        System.out.println("Gate " + this.getID() + ": Cleaning plane " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        refuelingTruck.refuel(this.plane);
        System.out.println(
                "Gate " + this.getID() + ": Plane " + Thread.currentThread().getName() + " is refueled & Cleaned");
        System.out
                .println("Gate " + this.getID() + ": " + Thread.currentThread().getName() + " is boarding passengers");
    }

    public boolean checkPlane() {
        if (this.plane != null) {
            System.out.println(colors.RED_BOLD + "Gate " + this.getID() + ": My plane is: "
                    + Thread.currentThread().getName() + colors.RESET);
            return true;
        } else {
            System.out.println(colors.RED_BOLD + "Gate " + this.getID() + ": No planes." + colors.RESET);
            return false;
        }
    }

    public Plane getplane() {
        return this.plane;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getID() {
        return this.ID;
    }

    @Override
    public void run() {
        synchronized (atc) {
            System.out.println(colors.PURPLE + "Gate " + this.getID() + ": Running gate thread..." + colors.RESET);
            try {
                while (plane == null) {
                    System.out.println(colors.PURPLE + "Gate " + this.getID() + ": is waiting...");
                    atc.wait(5000);
                    System.out.println(colors.PURPLE + "Gate " + this.getID() + ": is awake!");
                    if (plane != null) {
                        preparePlane(this.plane);
                    }
                }
            } catch (Exception e) {
            }

        }
    }
}