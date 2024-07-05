public class Plane implements Runnable {
    private int id, fuel, passengers;
    private boolean emergency, prepared, landingClearance;
    private final ATC atc;
    private Gate targetGate;
    private Runway runway;

    public Plane(int id, ATC atc, boolean emergency) {
        this.id = id;
        this.atc = atc;
        this.fuel = 0;
        this.passengers = 50;
        this.emergency = emergency;
        this.prepared = false;
        this.targetGate = null;
    }

    public void requestLanding() {
        synchronized (this) {
            System.out.println(c.plane + "Plane " + id + ": requesting for landing..." + c.r);
            Gate gate = atc.requestLanding(this);
            this.targetGate = gate;
            System.out.println(c.plane + "Plane " + this.getId() + " assigned to gate " + targetGate.getID());
        }
    }

    // public void clearLand() {
    // try {
    // System.out.println(c.plane + "Plane " + this.getId() + ": cleared to land on
    // runway..." + c.r);
    // this.landingClearance = true;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    public void land(Runway runway) {
        System.out.println(c.plane + "Plane " + this.getId() + ": landing on runway");
        runway.land(this);
    }

    public void taxiToGate() {
        try {// taxi to gate
            System.out.println(
                    c.plane + "Plane " + this.getId() + " taxiing to gate " + targetGate.getID() + "..." + c.r);
            Thread.sleep(1000);

            targetGate.setPlane(this); // coast plane to gate and wake up gate in function
            synchronized (targetGate) {
                targetGate.notify();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void taxiToRunway() {
    }

    // #region Prepare plane

    public void disembark() {
        System.out.println(c.unimportant + "plane " + this.getId() + ": disembarking passengers..." + c.r);
        while (this.passengers > 0) {
            this.passengers--;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
        System.out.println(
                c.unimportant + "plane " + this.getId() + ": All passengers disembarked" + c.r);

    }

    public void resupply() {
        try {
            System.out.println(c.unimportant + "Plane " + this.getId() + ": Resupplying plane..." + c.r);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clean() {
        try {
            System.out.println(c.unimportant + "Plane " + this.getId() + ": Cleaning plane..." + c.r);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void embark() {
        System.out.println(c.unimportant + "Plane " + this.getId() + ": boarding passengers..." + c.r);

        while (this.passengers < 50) {
            this.passengers++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }

        System.out.println(c.plane + "plane " + this.getId() + ": All passengers boarded" + c.r);
    }

    public void addFuel(int fuel) {
        this.fuel += fuel;
    }

    public void setPrepared(Boolean prepared) {
        this.prepared = prepared;
    }

    // #endregion

    public boolean isEmergency() {
        return emergency;
    }

    // thread function
    @Override
    public void run() {
        System.out.println(c.init + "Initializing Plane" + this.getId() + c.r);
        try {
            Thread.sleep(1500);
            synchronized (this) {
                requestLanding(); // get gate, if its null it will wait
                while (targetGate == null) {
                    System.out.println(c.testing + "Plane " + this.getId() + ": waiting for gates..." + c.r);
                    wait(); // wait for gate to be available and notify all planes
                    requestLanding();
                }
                // System.out.println(c.testing + "Plane " + this.getId() + ": assigned to " +
                // targetGate.getID() + c.r);
                // System.out.println(
                // c.testing + "Plane " + this.getId() + ": Synchronized with gate " +
                // targetGate.getID() + c.r);

                while (!prepared) {
                    System.out.println(c.plane + "Plane " + id + " Waiting...");
                    wait(); // wait for prepared to be called
                }
                System.out.println(c.testing + "Plane " + this.getId() + ": has woken up from its deep slumber" + c.r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    @Override
    public String toString() {
        String text = c.testing + "Plane " + this.getId() + ": Passengers " + passengers + ", fuel: " + fuel;
        return text;
    }
}
