
public class Gate {
    ATC atc;
    Plane plane;
    RefuelingTruck refuelingTruck;
    private int ID;

    public Gate(ATC atc, int id) {
        this.atc = atc;
        this.ID = id;
        plane = null;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public synchronized void boardPlane(Plane plane) throws InterruptedException {
        System.out.println("Gate " + this.getID() + ": " + Thread.currentThread().getName() + " is being boarded...");
        Thread.sleep(3000);
        System.out.println("Gate " + this.getID() + ": " + Thread.currentThread().getName() + " has boarded");

    }

    public void preparePlane(Plane plane) {

        System.out.println("Gate " + this.getID() + ": Cleaning plane " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        refuelingTruck.refuel(this);
        System.out.println(
                "Gate " + this.getID() + ": Plane " + Thread.currentThread().getName() + " is refueled & Cleaned");
    }

    public boolean checkPlane() {
        if (this.plane != null) {
            return true;
        } else {
            return false;
        }
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getID() {
        return this.ID;
    }
}