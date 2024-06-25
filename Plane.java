
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
        System.out.println("Initializing Plane " + id);
    }

    public int getID() {
        return ID;
    }

    public void taxiToGate() {
        System.out.println("Plane number " + this.ID + " : Taxi to gate number ");
    }

    @Override
    public void run() {
        System.out.println("Plane " + this.getID() + " : Requesting to land...");
        atc.requestLanding(this);
    }

}