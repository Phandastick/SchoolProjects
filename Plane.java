
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
        System.out.println(colors.CYAN + "Initializing Plane " + id + "..." + colors.RESET);
    }

    public int getID() {
        return ID;
    }

    public void taxiToGate(Gate gate) {
        System.out.println("Plane number " + this.ID + " : Taxi to gate number " + gate.getID() + "\n");
        try {
            Thread.sleep(850);
        } catch (InterruptedException e) {
        }
        gate.setPlane(this);
    }

    public void embark() {
        System.out.println(
                colors.BLACK + Thread.currentThread().getName() + ": boarding passengers..." + colors.RESET);

        while (this.passengers < 50) {
            this.passengers++;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }

        System.out.println(
                colors.BLACK + Thread.currentThread().getName() + ": All passengers boarded" + colors.RESET);
    }

    public void disEmbark() {
        System.out.println(
                colors.BLACK + Thread.currentThread().getName() + ": disembarking passengers..." + colors.RESET);

        while (this.passengers > 0) {
            this.passengers--;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }

        System.out.println(
                colors.BLACK + Thread.currentThread().getName() + ": All passengers disembarked" + colors.RESET);
    }

    @Override
    public void run() {
        boolean cond = true;
        while (cond) {
            System.out.println("Plane " + this.getID() + " : Requesting to land...");
            cond = atc.requestLanding(this);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

    }

}