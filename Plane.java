
/**
 * Plane
 * methods:
 * - Queue for runway
 * - Allow passengers to board plane
 * - Take off after some time
 */
public class Plane implements Runnable {

    private int ID, passengers, fuel;

    private ATC atc;

    public Plane(int id, ATC atc) {
        this.ID = id;
        this.atc = atc;
        this.passengers = 0;
        this.fuel = 0;
        System.out.println(colors.init + "Initializing Plane " + id + "..." + colors.RESET);
    }

    public void land() {

    }

    public void takeoff() {
    }

    public void taxiToGate(Gate gate) {
        System.out.println(
                colors.unimportant + "Plane: Taxiing plane to gate " + gate.getID() + colors.RESET + colors.RESET);
        try {
            Thread.sleep(850); // simluate taxi
        } catch (InterruptedException e) {
        }
        gate.setPlane(this); // taxi to gate
    }

    public void taxiToRunway() {
        // use ATC to
    }

    public void embark() {
        System.out.println(colors.unimportant + "Plane " + this.getID() + ": boarding passengers..." + colors.RESET);

        while (this.passengers < 50) {
            this.passengers++;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }

        System.out.println(colors.plane + "plane " + this.getID() + ": All passengers boarded" + colors.RESET);
    }

    public void disEmbark() {
        System.out.println(
                colors.unimportant + "plane " + this.getID() + ": disembarking passengers..." + colors.RESET);

        while (this.passengers > 0) {
            this.passengers--;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
        System.out.println(
                colors.plane + "plane " + this.getID() + ": All passengers disembarked" + colors.RESET);
    }

    public void addFuel(int fuel) {
        this.fuel += fuel;
    }

    @Override
    public void run() {
        // boolean cond = true;
        // while (cond) {
        System.out.println(colors.plane + "Plane " + this.getID() + " : Requesting to land..." + colors.RESET);
        atc.requestLanding(this);
        // }
    }

    public int getID() {
        return ID;
    }

}