import java.util.concurrent.Semaphore;

public class Gate implements Runnable {
    private final ATC atc;
    Plane plane;
    final RefuelingTruck refuelingTruck;
    private final int ID;
    private final Semaphore sem;

    public Gate(ATC atc, int id, Semaphore sem) {
        this.sem = sem;
        this.atc = atc;
        this.ID = id;
        this.refuelingTruck = atc.truck;
        plane = null;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
        System.out.println(colors.RED_BOLD + "GATE: plane set");
    }

    public void boardPlane(Plane plane) {
    }

    public void preparePlane(Plane plane) {
        // disembarking
        System.out.println(colors.unimportant + "Gate " + this.getID() + ": plane " +
                plane.getID() + " is being disembarked..." + colors.RESET);
        plane.disEmbark();

        // 2000ms to clean plane
        System.out
                .println(colors.unimportant + "Gate " + this.getID() + ": Cleaning plane " +
                        plane.getID() + colors.RESET);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        // refuling plane
        refuelingTruck.refuel(this.plane);

        // boarding plane
        plane.embark();
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
            while (plane == null) {
                System.out.println(colors.gate + "Gate " + this.getID() + ": Awaiting Plane..." + colors.RESET);
                try {
                    atc.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        synchronized (this) {
            try {
                System.out.println(colors.gate + "Gate " + this.getID() + ": is awake!" + colors.RESET);
                preparePlane(this.plane);
                Thread.sleep(1000);
                System.out.println(colors.gate + "Deleting plane " + this.plane.getID() + "..." + colors.RESET);
                this.plane = null;
            } catch (Exception e) {

            }
        }
    }
}