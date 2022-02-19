package elevatorSubsystem;

import main.RequestEvent;
import main.Scheduler;

/**
 * Implements the Elevator thread
 * @author Mohammad Alkhaledi
 * @author James Anderson
 */
public class Elevator extends Thread{
    private RequestEvent elevatorRequest;
    private int elevatorNumber;
    private int floorNumber;
    private Scheduler scheduler;

    private enum doorState {
        OPEN,
        CLOSED
    }

    private enum movingState {
        UP,
        DOWN,
        STILL
    }

    doorState door;
    movingState moving;

    public Elevator(Scheduler scheduler){
        floorNumber = 1;
        this.scheduler = scheduler;
        elevatorNumber = 1;
        door = doorState.OPEN;
        moving = movingState.STILL;
    }

    /**
     * elevator goes to the floor that is requested
     */
    private void serveElevatorRequest(){
        int passengerFloor = elevatorRequest.getCurrentFloor();
        int requestedFloor = elevatorRequest.getFloorStop();
        floorNumber = goToFloor(floorNumber, passengerFloor);

        moving = movingState.STILL;
        door = doorState.OPEN;

        System.out.printf("Elevator %d has reached the passenger\n", elevatorNumber);
        System.out.println("Current States; Door State: " + door + ". Moving State: " + moving + ".");

        if(floorNumber != requestedFloor){
            floorNumber = goToFloor(floorNumber, requestedFloor);
        }

        moving = movingState.STILL;
        door = doorState.OPEN;
        System.out.printf("Elevator %d has reached the destination..\n", elevatorNumber);
        System.out.println("Current States; Door State: " + door + ". Moving State: " + moving + ".");
    }

    private int goToFloor(int currentFloor, int desiredFloor){
        door = doorState.CLOSED;
        while(currentFloor != desiredFloor){
            System.out.printf("Elevator %d is at floor %d..\n", elevatorNumber, currentFloor);
            System.out.println("Current States; Door State: " + door + ". Moving State: " + moving + ".");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(currentFloor > desiredFloor && door == doorState.CLOSED){
                moving = movingState.DOWN;
                --currentFloor;
            }
            else if (door == doorState.CLOSED){
                moving = movingState.UP;
                ++currentFloor;
            }

        }
        return currentFloor;
    }


    /**
     * the elevator's thread of execution
     */
    @Override
    public void run(){
        while(true){
            elevatorRequest = scheduler.getElevatorRequest();
            serveElevatorRequest();
            scheduler.addFloorRequest(elevatorRequest);
        }

    }

}

