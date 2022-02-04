/**
 * Implements the Elevator thread
 * @author Mohammad Alkhaledi
 */
public class Elevator extends Thread{
    private RequestEvent elevatorRequest;
    private int elevatorNumber;
    private int floorNumber;
    private Scheduler scheduler;

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
        System.out.printf("Elevator %d has reached the destionation..\n", elevatorNumber);

        elevatorRequest = null;
    }
    private int goToFloor(int currentFloor, int desiredFloor){
        while(currentFloor != desiredFloor){
            System.out.printf("Elevator %d is at floor %d..\n", elevatorNumber, floorNumber);
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
