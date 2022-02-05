package main;

/**
 * The Scheduler which is used as a communication channel from the Floor thread to the Elevator thread
 *  @author sarashikhhassan, Mohammad Alkhaledi
 *  @version 1.0 (iteration 1)
 */
public class Scheduler {
    private RequestEvent floorRequest, elevatorRequest;

    /**
     * Default constructor for scheduler
     */
    public Scheduler(){
        floorRequest = null;
        elevatorRequest = null;

    }

    /**
     * @return true if a floor request is available, or not null
     */
    public boolean getFloorRequestAvailable(){
        return floorRequest != null;
    }

    /**
     * @return true if an elevator request is available, or not null
     */
    public boolean getElevatorRequestAvailable(){
        return elevatorRequest != null;
    }

    /**
     * @return elevator request
     */
    public synchronized RequestEvent getElevatorRequest() {

        while (!getElevatorRequestAvailable()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RequestEvent eventToBeReturned = elevatorRequest;
        elevatorRequest = null;
        notifyAll();
        return eventToBeReturned;
    }

    /**
     *
     * @return the floor request added by the elevator
     */
    public synchronized RequestEvent getFloorRequest() {
        while(!getFloorRequestAvailable()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RequestEvent eventToBeReturned = floorRequest;
        floorRequest = null;
        notifyAll();
        return eventToBeReturned;
    }

    /**
     * signals elevator needs to pick someone up
     * @param elevatorRequest the elevator request to be added by floor
     */
    public synchronized void addElevatorRequest(RequestEvent elevatorRequest){
        while(getElevatorRequestAvailable()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.elevatorRequest = elevatorRequest;
        notifyAll();
    }

    /**
     * signals elevator reached the floor
     * @param floorRequest the floor request to be added by the elevator
     */
    public synchronized void addFloorRequest(RequestEvent floorRequest){
        while (getFloorRequestAvailable()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.floorRequest = floorRequest;
        notifyAll();
    }


    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        Floor floor = new Floor(scheduler);
        Elevator elevator = new Elevator(scheduler);

        floor.start();
        elevator.start();

    }


}