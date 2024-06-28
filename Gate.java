
public class Gate implements Runnable {
    private final ATC atc;
    Plane plane;
    final RefuelingTruck refuelingTruck;
    private final int ID;

    public Gate(ATC atc, int id) {
        this.atc = atc;
        this.ID = id;
        this.refuelingTruck = atc.truck;
        plane = null;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public synchronized void boardPlane(Plane plane) {
        System.out
                .println(colors.gate + "Gate " + this.getID() + ":  Passenger boarding plane " +
                        plane.getID() + "..." + colors.RESET);
        plane.embark();
    }

    public void preparePlane(Plane plane) {
        System.out
                .println(colors.gate + "Gate " + this.getID() + ": plane " +
                        plane.getID() + " is being disembarked..." + colors.RESET);
        plane.disEmbark();

        // 2000 to clean plane
        System.out
                .println(colors.gate + "Gate " + this.getID() + ": Cleaning pplane " +
                        plane.getID() + colors.RESET);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        refuelingTruck.refuel(this.plane);
        System.out.println(colors.gate + "Gate " + this.getID() + ": plane " + plane.getID() + " is refueled & Cleaned"
                + colors.RESET);
        boardPlane(plane);
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

    public int getID() {
        return this.ID;
    }

    @Override
    public void run() {
        synchronized (atc) {
            System.out.println(colors.gate + "Gate " + this.getID() + ": Running gate thread..." + colors.RESET);
            try {
                while (plane == null) {
                    System.out.println(colors.gate + "Gate " + this.getID() + ": is waiting..." + colors.RESET);
                    atc.wait();
                    System.out.println(colors.gate + "Gate " + this.getID() + ": is awake!" + colors.RESET);
                    if (plane != null) {
                        preparePlane(this.plane);
                    }
                }

                Thread.sleep(5000);
                System.out.println(colors.gate + "Deleting plane " + this.plane.getID() + "..." + colors.RESET);
                this.plane = null;
            } catch (Exception e) {
            }

        }
    }
}