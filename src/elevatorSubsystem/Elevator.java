package elevatorSubsystem;


import main.UserRequest;
import main.Utilities;


import java.io.IOException;
import java.net.*;

/**
 * Implements the Elevator thread
 * @author Mohammad Alkhaledi 101162465
 * @author James Anderson
 * @version 4.0 (iteration 4)
 */
public class Elevator extends Thread{
    private UserRequest elevatorRequest;
    private int floorNumber;
    private ElevatorState moving;
    private ElevatorState door;
    private final int elevatorNumber;
    private static int elevatorCount = 1;
    private boolean operational;

    public Elevator(){
        floorNumber = 1;
        door = ElevatorState.OPEN;
        moving = ElevatorState.STILL;
        elevatorNumber = elevatorCount;
        elevatorCount++;
        operational = true;
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

        if(elevatorRequest.returnNoFault()){
            openDoor();
        }
        else if(elevatorRequest.returnTransientFault()){
            handleTransientFault();
            openDoor();
        }
        else if(elevatorRequest.returnHardFault()){
            handleHardFault();
            return;
        }
        System.out.printf("Elevator %d has reached the destination..\n", elevatorNumber);

        System.out.println("Door State: " + door + ". Moving State: " + moving + ".");
    }

    /**
     * takes the elevator from its current floor to its desired floor
     * @param currentFloor the current floor of the elevator
     * @param desiredFloor the floor the elevator wants to go to
     * @return the current floor once elevator has reached its destination
     */
    private int goToFloor(int currentFloor, int desiredFloor){
        closeDoor();
        while(currentFloor != desiredFloor){
            System.out.printf("Elevator %d is at floor %d..\n", elevatorNumber, currentFloor);
            System.out.println("Current States; Door State: " + door + ". Moving State: " + moving + ".");
            try {
                sleep(Utilities.ELEVATOR_TRAVEL_SPEED);
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

    /**
     * sets elevator state to UP
     */
    private void setUp() {
        moving = ElevatorState.UP;
    }

    /**
     * sets elevator state to DOWN
     */
    private void setDown() {
        moving = ElevatorState.DOWN;
    }

    /**
     * sets elevator state to STILL
     */
    private void setStill() {
        moving = ElevatorState.STILL;
    }

    /**
     * sets elevator state to OPEN
     */
    private void openDoor() {
        door = ElevatorState.OPEN;
    }

    /**
     * sets elevator state to CLOSED
     */
    private void closeDoor() {
        door = ElevatorState.CLOSED;
    }

    /**
     * elevator behaviour that deals with a non-serious fault with the elevator
     */
    private void handleTransientFault(){
        System.out.printf("Error occurred in Elevator %d while opening door..\n", elevatorNumber);
        long slowDoorSpeed = Utilities.ELEVATOR_DOOR_SPEED * 3;
        try {
            sleep(slowDoorSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Attempting to resolve the issue..");
        try {
            sleep(slowDoorSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Error resolved. The door malfunctioned, but is now functional.");
    }

    /**
     * elevator behaviour that deals with a serious fault with the elevator
     */
    private void handleHardFault(){
        System.out.printf("An unknown error occurred in Elevator %d.\nPlease wait a moment.\n", elevatorNumber);
        long troubleShootSpeed = Utilities.ELEVATOR_DOOR_SPEED * 7;
        try {
            sleep(troubleShootSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openDoor();
        System.out.printf("""
                Error could not be resolved. Elevator %d is out of commission.
                Passengers have exited elevator thru emergency exit on floor %d.
                Please call maintenance team to resolve issue.
                """,elevatorNumber, floorNumber);
        elevatorRequest.setCurrentFloor(floorNumber);
        operational = false;
    }

    /**
     * the elevator's thread of execution
     */
    @Override
    public void run() {
        try {
            while (operational) {

                //requesting work
                DatagramSocket serviceSocket = new DatagramSocket();
                byte[] elevatorRequest = {1, (byte) floorNumber};
                DatagramPacket requestPacket = new DatagramPacket(elevatorRequest, elevatorRequest.length, InetAddress.getByName("localhost"), Utilities.ELEVATOR_SERVICE_PORT);
                serviceSocket.send(requestPacket);
                byte[] requestData = new byte[1024];
                requestPacket = new DatagramPacket(requestData, requestData.length);
                serviceSocket.receive(requestPacket);
                String requestAsString = new String(requestPacket.getData()).trim();
                this.elevatorRequest = (UserRequest) Utilities.parseEvent(requestAsString);

                serveElevatorRequest();
                requestAsString = this.elevatorRequest.toPlainText();
                //sending floor request info
                byte[] floorRequest = new byte[1024];
                floorRequest[0] = 2;
                requestData = requestAsString.getBytes();
                System.arraycopy(requestData, 0, floorRequest, 1, requestData.length);
                requestPacket = new DatagramPacket(floorRequest, floorRequest.length, InetAddress.getByName("localhost"), Utilities.ELEVATOR_SERVICE_PORT);
                serviceSocket.send(requestPacket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * the elevator process' main thread
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Elevator[] elevators = new Elevator[3];
        for (int i = 0; i < elevators.length; ++i){
            elevators[i] = new Elevator();
        }

        for(Elevator e : elevators)
            e.start();


    }

}

