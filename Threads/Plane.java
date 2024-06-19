
package Threads;

/**
 * Plane
 * methods:
 * - Queue for runway
 * - Allow passengers to board plane
 * - Take off after some time
 */
public class Plane implements Runnable {

    private int ID;

    public Plane(int id) {
        this.ID = id;
    }

    int getID() {
        return ID;
    }

    void taxiToGate() {
        System.out.println("Plane number " + this.ID + "Taxi to gate number");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}