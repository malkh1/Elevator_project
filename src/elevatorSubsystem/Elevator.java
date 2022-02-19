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
    private ElevatorState moving;
    private ElevatorState door;

    public Elevator(Scheduler scheduler){
        floorNumber = 1;
        this.scheduler = scheduler;
        elevatorNumber = 1;
        door = ElevatorState.OPEN;
        moving = ElevatorState.STILL;
    }

    /**
     * elevator goes to the floor that is requested
     */
    private void serveElevatorRequest(){
        int passengerFloor = elevatorRequest.getCurrentFloor();
        int requestedFloor = elevatorRequest.getFloorStop();
        floorNumber = goToFloor(floorNumber, passengerFloor);

        setStill();
        openDoor();

        System.out.printf("Elevator %d has reached the passenger\n", elevatorNumber);
        System.out.println("Current States; Door State: " + door + ". Moving State: " + moving + ".");

        if(floorNumber != requestedFloor && moving == ElevatorState.STILL){
            floorNumber = goToFloor(floorNumber, requestedFloor);
        }

        setStill();
        openDoor();
        System.out.printf("Elevator %d has reached the destination..\n", elevatorNumber);
        System.out.println("Current States; Door State: " + door + ". Moving State: " + moving + ".");
    }

    private int goToFloor(int currentFloor, int desiredFloor){
        closeDoor();
        while(currentFloor != desiredFloor){
            System.out.printf("Elevator %d is at floor %d..\n", elevatorNumber, currentFloor);
            System.out.println("Current States; Door State: " + door + ". Moving State: " + moving + ".");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(currentFloor > desiredFloor && door == ElevatorState.CLOSED){
                setDown();
                --currentFloor;
            }
            else if (door == ElevatorState.CLOSED){
                setUp();
                ++currentFloor;
            }

        }
        return currentFloor;
    }

    public void setUp() {
        moving = ElevatorState.UP;
    }

    public void setDown() {
        moving = ElevatorState.DOWN;
    }

    public void setStill() {
        moving = ElevatorState.STILL;
    }

    public void openDoor() {
        door = ElevatorState.OPEN;
    }

    public void closeDoor() {
        door = ElevatorState.CLOSED;
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

