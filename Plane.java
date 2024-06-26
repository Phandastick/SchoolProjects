
/**
 * Plane
 * methods:
 * - Queue for runway
 * - Allow passengers to board plane
 * - Take off after some time
 */
public class Plane implements Runnable {

    private int ID;

    private ATC atc;

    public Plane(int id, ATC atc) {
        this.ID = id;
        this.atc = atc;
        System.out.println(colors.CYAN + "Initializing Plane " + id + "..." + colors.RESET);
    }

    public int getID() {
        return ID;
    }

    public void taxiToGate(Gate gate) {
        System.out.println("Plane number " + this.ID + " : Taxi to gate number " + gate.getID());
    }

    @Override
    public void run() {
        System.out.println("Plane " + this.getID() + " : Requesting to land...");
        atc.requestLanding(this);
    }

}