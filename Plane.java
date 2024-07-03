
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
        System.out.println(colors.plane + "Initializing Plane " + id + "..." + colors.RESET);
    }

    public void land() {
        System.out.println(colors.plane + "Plane " + this.getID() + " is landing on the runway");
    }

    public void takeoff() {
        System.out.println(colors.plane + "Plane " + this.getID() + " is taking off from the runway");
        notify();
    }

    public void taxiToGate(Gate gate) {
        System.out.println(
                colors.unimportant + "Plane: Taxiing plane to gate " + gate.getID() + colors.RESET);
        try {
            Thread.sleep(850); // simluate taxi
        } catch (InterruptedException e) {
        }
        gate.setPlane(this); // taxi to gate
    }

    public void taxiToRunway() {
        // use ATC toa
    }

    public void embark() {
        System.out.println(colors.unimportant + "Plane " + this.getID() + ": boarding passengers..." + colors.RESET);

        while (this.passengers < 50) {
            this.passengers++;
            try {
                Thread.sleep(10);
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

    public int getFuel() {
        return this.fuel;
    }

    @Override
    public void run() {
        System.out.println(colors.plane + "Plane " + this.getID() + " : Requesting to land..." + colors.RESET);
        atc.requestLanding(this);
        synchronized (this) {
            try {
                // System.out
                // .println(colors.testing + "Plane " + this.getID() + " waiting for
                // processes..." + colors.RESET);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(colors.plane + "Plane " + this.getID() + " requesting take off" + colors.RESET);
            atc.requestTakeoff(this);
        }
    }

    public int getID() {
        return ID;
    }

    public int getPasasengers() {
        return passengers;
    }

}