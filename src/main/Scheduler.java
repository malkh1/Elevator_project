package main;

import elevatorSubsystem.Elevator;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Scheduler which is used as a communication channel from the Floor thread to the Elevator thread
 *  @author sarashikhhassan, Mohammad Alkhaledi
 *  @version 2.0 (iteration 2)
 */
public class Scheduler {
    private LinkedList<RequestEvent> elevatorRequests;
    private LinkedList<RequestEvent> floorRequests;

    /**
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
     * Floor adds a request, and then waits until it's request has been served
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
        for(int i = 0; i < Floor.NUMBER_OF_FLOORS; ++i){
            floors[i] = new Floor(scheduler);
        }
        Elevator elevator = new Elevator(scheduler);
        Elevator elevator1 = new Elevator(scheduler);
        Elevator elevator2 = new Elevator(scheduler);

        Elevator elevator3 = new Elevator(scheduler);

        Elevator elevator4 = new Elevator(scheduler);
        Elevator elevator5 = new Elevator(scheduler);


        for(Floor f : floors)
            f.start();

        elevator.start();
        elevator1.start();
        elevator2.start();
        elevator3.start();
        elevator4.start();
        elevator5.start();


    }


}