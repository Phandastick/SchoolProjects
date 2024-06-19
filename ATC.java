import Threads.*;

/*
 * Methods:
 * - Receive request for runway
 * 
 * Process for plane to land:
 * - Plane requests to land
 * - ATC requests runway to clear out
 * - 
 */

public class ATC implements Runnable {

    public synchronized void requestGate() {

    }

    public void requestLanding(Plane plane) {
        // plane uses runway to get to gate

    }

    @Override
    public void run() {

    }
}
