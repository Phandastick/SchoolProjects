public class Plane implements Runnable {
    private int id, fuel, passengers;
    private boolean emergency, prepared;
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
        System.out.println(c.plane + "Plane " + id + ": requesting for landing..." + c.r);
        Gate gate = atc.requestLanding(this);
        this.targetGate = gate;
    }

    public void land(Runway runway) {
        System.out.println(c.plane + "Plane " + this.getId() + ": landing on runway" + c.r);
        runway.land(this); // calls this function to prove that runway is receving plane, and also calls
                           // runway.plane = plane
        this.runway = runway; // set runway to be used in takeoff
    }

    public void takeoff() {
        System.out.println(c.plane + "Plane " + this.getId() + ": Taking off from runway..." + c.r);
        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(c.plane + "Plane " + this.getId() + " has took off" + c.r);
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
        runway.taxi(this); // taxi plane to runway
        synchronized (targetGate) {
            this.clearGate();
            targetGate.notify(); // notify that plane has left the gate
        }
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

    public void requestTakeoff() {
        System.out.println(c.plane + "Plane " + this.getId() + ": Requesting take off" + c.r);
        atc.requestTakeoff(this);
    }

    public void clearGate() {
        targetGate.clearPlane();
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

                if (!prepared) {
                    System.out.println(c.plane + "Plane " + id + " Waiting to be prepared" + c.r);
                    wait(); // wait for prepared to be called
                }

                requestTakeoff();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(c.plane + "Ending Plane " + this.getId() + c.r);
    }

    public boolean isEmergency() {
        return emergency;
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
        synchronized (this) {
            notify();
        }
    }

    public Gate getTargetGate() {
        return targetGate;
    }

    @Override
    public String toString() {
        String text = c.testing + "Plane " + this.getId() + ": Passengers " + passengers + ", fuel: " + fuel + c.r;
        return text;
    }
}
