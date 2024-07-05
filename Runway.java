import java.util.concurrent.Semaphore;

public class Runway {

    private Plane plane;
    private ATC atc;
    private Semaphore runwaySem;

    public Runway(ATC atc) {
        this.atc = atc;
        this.plane = null;
        this.runwaySem = new Semaphore(1);
    }

    public void handleLanding(Plane plane) {
        synchronized (plane) {
            try {
                // landing sequence
                System.out.println(c.plane + "Runway: Trying to get runway lock" + c.r);
                runwaySem.acquire(); // locks runway
                System.out.println(c.runway + "Runway: plane " + plane.getId() + " has acquired lock" + c.r);
                plane.land(this);
                System.out.println(c.runway + "Runway: plane " + plane.getId() + " has landed on runway" + c.r);
                this.plane = plane;

                // taxi sequence
                plane.taxiToGate(); // taxi to gate
                runwaySem.release(); // taxi sequence completes
            } catch (

            Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleTakeoff(Plane plane) {
        synchronized (plane) {
            try {
                // taxi sequence
                System.out.println(c.plane + "Runway: Trying to get runway lock" + c.r);
                runwaySem.acquire(); // locks runway
                System.out.println(c.runway + "Runway: plane " + plane.getId() + " has acquired lock" + c.r);
                plane.taxiToRunway();
                System.out.println(c.runway + "Runway: plane " + plane.getId() + " has taxiied to runway" + c.r);
                this.plane = plane;

                // takeoff
                plane.takeoff();
                this.plane = null;
            } catch (

            Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void land(Plane plane) {
        try {
            System.out.println(c.runway + "Runway: receving plane " + plane.getId() + " landing" + c.r);
            Thread.sleep(1850);
            this.plane = plane;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void taxi(Plane plane) {
        try {
            System.out.println(c.runway + "Runway: receving plane " + plane.getId() + " taxi" + c.r);
            Thread.sleep(1000);
            this.plane = plane;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // @Override
    // public void run() {
    // try {
    // System.out.println(c.init + "Runway initializing..." + c.r);
    // Thread.sleep(1000);
    // // synchronized (this) {
    // // while (true) {
    // // if (this.plane == null) {
    // // System.out.println(c.testing + "Runway waiting..." + c.r);
    // // wait(); // wait for planes to be added into queue, notified by atc (atc.)
    // // synchronized (atc) {
    // // System.out.println(c.testing + "Runway notifiying ATC runway is free..." +
    // c.r);
    // // atc.notify(); // wakes up atc because runway is free to be used by plane
    // (atc.49)
    // // }
    // // System.out.println(c.testing + "Runway waiting for runway to be finished
    // using" + c.r);
    // // wait(); // wait for runway to be finished using, and will wait for next
    // plane to be
    // // // queued, called by this runway thread
    // // }
    // // }
    // // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
