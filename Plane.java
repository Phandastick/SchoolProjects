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
        synchronized (this) {
            Gate gate = atc.requestLanding(this);
            this.targetGate = gate;
            System.out.println(c.plane + "Plane " + this.getId() + " assigned to gate " + targetGate.getID());
        }
    }

    public void clearLand(Runway runway) {
        try { // take runway lock
            System.out.println(c.plane + "Plane " + this.getId() + ": cleared to land on runway..." + c.r);
            this.runway = runway;
            runway.handleLanding(this); // land on runway
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void land() {
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

    public boolean isEmergency() {
        return emergency;
    }

    public void setPrepared(Boolean prepared) {
        this.prepared = prepared;
    }

    // thread function
    @Override
    public void run() {
        System.out.println(c.init + "Initializing Plane" + this.getId() + c.r);
        try {
            synchronized (this) {
                requestLanding(); // get gate, if its null it will wait
                while (targetGate == null) {
                    wait(); // wait for gate to be available and notify all planes
                    requestLanding();
                }
                // System.out.println(c.testing + "Plane " + this.getId() + ": assigned to " +
                // targetGate.getID() + c.r);
            }
            synchronized (targetGate) {
                // System.out.println(
                // c.testing + "Plane " + this.getId() + ": Synchronized with gate " +
                // targetGate.getID() + c.r);
                while (!prepared) {
                    wait(); // wait for prepared to be called
                }
                System.out.println(c.testing + "Plane " + this.getId() + ": has woken up from its deep slumber" + c.r);
            }
        } catch (Exception e) {
            // TODO: handle exception
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

}
