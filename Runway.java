/*
 * Methods:
 * - receive plane landing
 */

public class Runway extends Thread {
    private boolean occupied;

    public Runway() {
        this.occupied = false;
    }

    public boolean checkLanding() {
        return occupied;
    }

    public void planeLanding() {
        occupied = true;
    }
}
