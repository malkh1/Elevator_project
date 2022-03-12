package schedulerSubsystem;

import elevatorSubsystem.Elevator;
import main.Floor;
import main.RequestEvent;

import java.util.LinkedList;

/**
 * The Scheduler which is used as a communication channel from the Floor thread to the Elevator thread
 *  @author sarashikhhassan, Mohammad Alkhaledi
 *  @version 3.0 (iteration 3)
 */
public class Scheduler {
    private LinkedList<RequestEvent> elevatorRequests;
    private LinkedList<RequestEvent> floorRequests;

    /**\\
     * Default constructor for scheduler
     */
    public Scheduler(){
        elevatorRequests = new LinkedList<>();
        floorRequests = new LinkedList<>();

    }

    /**
     * @return true if a floor request is available, or not null
     */
    public boolean getFloorRequestAvailable(){
        return !floorRequests.isEmpty();
    }

    /**
     * @return true if an elevator request is available, or not null
     */
    public boolean getElevatorRequestAvailable(){
        return !elevatorRequests.isEmpty();
    }

    /**
     *
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
        RequestEvent eventToBeReturned = elevatorRequests.pop();
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
        RequestEvent eventToBeReturned = floorRequests.pop();
        notifyAll();
        return eventToBeReturned;
    }

    /**
     * To be called by a floor.
     * Floor adds a request, and then waits until its request has been served
     * @param elevatorRequest the elevator request to be added by floor
     */
    public synchronized void addElevatorRequest(RequestEvent elevatorRequest){

        this.elevatorRequests.add(elevatorRequest);
        notifyAll();

        while(elevatorRequests.contains(elevatorRequest)){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        this.floorRequests.add(floorRequest);
        notifyAll();
    }




    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        Floor[] floors = new Floor[Floor.NUMBER_OF_FLOORS];
        Elevator[] elevators = new Elevator[7];

        for(int i = 0; i < Floor.NUMBER_OF_FLOORS; ++i){
            floors[i] = new Floor(scheduler);
        }

        for (int i = 0; i < elevators.length; ++i){
            elevators[i] = new Elevator(scheduler);
        }

        for(Floor f : floors)
            f.start();

        for(Elevator e : elevators)
            e.start();


    }


}