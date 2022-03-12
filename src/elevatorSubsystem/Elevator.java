package elevatorSubsystem;

import main.RequestEvent;
import main.Utilities;


import java.io.IOException;
import java.net.*;

/**
 * Implements the Elevator thread
 * @author Mohammad Alkhaledi
 * @author James Anderson
 * @version 3.0 (iteration 3)
 */
public class Elevator extends Thread{
    private RequestEvent elevatorRequest;
    private int floorNumber;
    private ElevatorState moving;
    private ElevatorState door;
    private int elevatorNumber;
    private static int elevatorCount = 1;

    public Elevator(){
        floorNumber = 1;
        door = ElevatorState.OPEN;
        moving = ElevatorState.STILL;
        elevatorNumber = elevatorCount;
        elevatorCount++;
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

    /**
     * sets elevator state to UP
     */
    public void setUp() {
        moving = ElevatorState.UP;
    }

    /**
     * sets elevator state to DOWN
     */
    public void setDown() {
        moving = ElevatorState.DOWN;
    }

    /**
     * sets elevator state to STILL
     */
    public void setStill() {
        moving = ElevatorState.STILL;
    }

    /**
     * sets elevator state to OPEN
     */
    public void openDoor() {
        door = ElevatorState.OPEN;
    }

    /**
     * sets elevator state to CLOSED
     */
    public void closeDoor() {
        door = ElevatorState.CLOSED;
    }


    /**
     * the elevator's thread of execution
     */
    @Override
    public void run(){
        while(true){
            try{
                DatagramSocket serviceSocket = new DatagramSocket();
                byte[] elevatorRequest = {1, (byte) floorNumber};
                DatagramPacket requestPacket = new DatagramPacket(elevatorRequest, elevatorRequest.length,
                        InetAddress.getByName("localhost"), Utilities.ELEVATOR_SERVICE_PORT);
                serviceSocket.send(requestPacket);
                byte[] requestData = new byte[1024];
                requestPacket = new DatagramPacket(requestData, requestData.length);
                serviceSocket.receive(requestPacket);

                //parse request data given to elevator
                String requestAsString = new String(requestPacket.getData()).trim();
                this.elevatorRequest = Utilities.parseEvent(requestAsString);
                serveElevatorRequest();

                byte[] floorRequest = new byte[1024];
                floorRequest[0] = 2;
                requestData = requestAsString.getBytes();
                System.arraycopy(requestData, 0, floorRequest, 1, requestData.length);
                requestPacket = new DatagramPacket(floorRequest, floorRequest.length,
                        InetAddress.getByName("localhost"), Utilities.ELEVATOR_SERVICE_PORT);
                serviceSocket.send(requestPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //scheduler.addFloorRequest(elevatorRequest);
        }

    }

    public static void main(String[] args) {
        Elevator[] elevators = new Elevator[7];
        for (int i = 0; i < elevators.length; ++i){
            elevators[i] = new Elevator();
        }

        for(Elevator e : elevators)
            e.start();


    }

}

