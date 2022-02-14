package main;

/**
 * Implements the Elevator thread
 * @author Mohammad Alkhaledi
 */
public class Elevator extends Thread{
    private RequestEvent elevatorRequest;
    private int elevatorNumber;
    private int floorNumber;
    private Scheduler scheduler;

    /**
     * default constructor for elevator
     * @param scheduler the scheduler that will communicate with the elevator
     */
    public Elevator(Scheduler scheduler){
        floorNumber = 1;
        this.scheduler = scheduler;
        elevatorNumber = 1;
    }

    /**
     * elevator goes to the floor that is requested
     */
    private void serveElevatorRequest(){
        int passengerFloor = elevatorRequest.getCurrentFloor();
        int requestedFloor = elevatorRequest.getFloorStop();

        floorNumber = goToFloor(floorNumber, passengerFloor);
        System.out.printf("Elevator %d has reached the passenger\n", elevatorNumber);

        if(floorNumber != requestedFloor){
            floorNumber = goToFloor(floorNumber, requestedFloor);
        }
        System.out.printf("Elevator %d has reached the destination..\n", elevatorNumber);
    }

    /**
     * Implements motor function -> refactor into motor please and thank you
     * @param currentFloor current floor of the elevator
     * @param desiredFloor floor to reach
     * @return the floor level once the elevator reaches its destination
     */
    private int goToFloor(int currentFloor, int desiredFloor){
        while(currentFloor != desiredFloor){
            System.out.printf("Elevator %d is at floor %d..\n", elevatorNumber, currentFloor);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(currentFloor > desiredFloor){
                --currentFloor;
            }
            else{
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
