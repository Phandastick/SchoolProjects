package V1;

import java.util.concurrent.Semaphore;

public class Gate implements Runnable {
    @SuppressWarnings("unused")
    private final ATC atc;
    private Plane plane;
    final RefuelingTruck refuelingTruck;
    private final int ID;
    private final Runway runway;
    private final Semaphore runwayMutex;

    public Gate(ATC atc, int id, Semaphore runwayMutex, Runway runway) {
        this.atc = atc;
        this.ID = id;
        this.runwayMutex = runwayMutex;
        this.refuelingTruck = atc.truck;
        this.runway = runway;
        plane = null;
    }

    public void setPlane(Plane plane) {
        if (this.plane != null) {
            try {
                throw new InterruptedException("Cannot set plane in gate " + this.getID());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.plane = plane;
        System.out.println(colors.RED_BOLD + "GATE " + this.getID() + ": plane set");

        // try {
        // sem.acquire(1);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
    }

    public void boardPlane(Plane plane) {
    }

    public void preparePlane(Plane plane) {
        // disembarking
        System.out.println(colors.gate + "Gate " + this.getID() + ": plane " + plane.getID()
                + " is being disembarked..." + colors.RESET);
        plane.disEmbark();

        // 500ms to clean plane
        System.out
                .println(colors.gate + "Gate " + this.getID() + ": Cleaning plane " +
                        plane.getID() + colors.RESET);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        // refuling plane
        refuelingTruck.refuel(this.plane);
        // boarding plane
        plane.embark();
        synchronized (plane) { // notify the plane is ready to takeoff
            System.out.println(colors.gate + "Gate " + this.getID() + ": Plane " + plane.getID()
                    + " is ready to take off with\nPassengers: " + plane.getPasasengers() + "\n Fuel: "
                    + plane.getFuel() + colors.RESET);
            plane.notify(); // notify plane ready to leave
            try {
                System.out.println(colors.gate + "Gate: Waiting for plane notify" + colors.RESET);
                wait(); // wait until plane leaves the gate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(colors.gate + "Gate: reset plane" + colors.RESET);
            this.plane = null;
        }
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
        synchronized (runway) {
            System.out.println(colors.gate + "Gate " + this.getID() + ": Running gate thread..." + colors.RESET);
            while (plane == null) {
                System.out.println(colors.gate + "Gate " + this.getID() + ": Awaiting Plane..." + colors.RESET);
                try {
                    runway.wait();
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
                // System.out.println(colors.gate + "Deleting plane " + this.plane.getID() +
                // "..." + colors.RESET);
                // this.plane = null;
                notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}