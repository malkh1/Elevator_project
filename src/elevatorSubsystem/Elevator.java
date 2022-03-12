package elevatorSubsystem;

import main.RequestEvent;
import main.Utilities;
import schedulerSubsystem.Scheduler;

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
    private Scheduler scheduler;
    private ElevatorState moving;
    private ElevatorState door;
    private int elevatorNumber;
    private static int elevatorCount = 1;
    private DatagramSocket serviceSocket;

    public Elevator(Scheduler scheduler){
        floorNumber = 1;
        this.scheduler = scheduler;
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
                serviceSocket = new DatagramSocket();
                String requestWork = "1" + floorNumber;
                DatagramPacket requestPacket = new DatagramPacket(requestWork.getBytes(), requestWork.length(),
                        InetAddress.getByName("localhost"), Utilities.ELEVATOR_SERVICE_PORT);
                serviceSocket.send(requestPacket);
                byte[] requestData = new byte[128];
                requestPacket = new DatagramPacket(requestData, requestData.length);
                serviceSocket.receive(requestPacket);

                //parse requestdata given to elevator
                String requestAsString = new String(requestPacket.getData()).trim();
                elevatorRequest = Utilities.parseEvent(requestAsString);
                serveElevatorRequest();


            } catch (IOException e) {
                e.printStackTrace();
            }
            //scheduler.addFloorRequest(elevatorRequest);
        }

    }

}

