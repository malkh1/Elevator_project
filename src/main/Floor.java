package main;

import java.net.*;
import java.io.IOException;

import java.util.ArrayList;

import static main.Utilities.*;

/**
 * Implements the Floor class/thread for sending requests
 *  @author James Anderson, 101147068, Mohammad Alkhaledi 101162465
 * @version 3.0 (iteration 3)
 */

public class Floor extends Thread {

    private ArrayList<RequestEvent> details;
    private ArrayList<RequestEvent> currentEvents;
    private int floorNumber;
    private static int floorCount = 1;
    public static final int NUMBER_OF_FLOORS = 7;

    /**
     * Floor constructor
     */
    public Floor() {
        this.details = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        floorNumber = floorCount;
        floorCount++;
    }

    /**
     * ADD REQUEST EVENTS PERTAINING TO THIS FLOOR
     */
    private void getRelevantEvents(){
        for(RequestEvent event : details){
            if(event.getCurrentFloor() == floorNumber)
                currentEvents.add(event);
        }
    }

    /**
     * floor thread of execution
     */
    public void run() {
        details = getEvents("src\\main\\requests.txt");
        getRelevantEvents();

        for (RequestEvent x : currentEvents) {
            try {
                //sending elevator request info
                DatagramSocket serviceSocket = new DatagramSocket();
                byte[] elevatorRequest = new byte[1024];
                elevatorRequest[0] = 1;
                byte[] requestData = ((UserRequest)x).toPlainText().getBytes();
                System.arraycopy(requestData, 0, elevatorRequest,1, requestData.length);
                DatagramPacket sendPacket = new DatagramPacket(elevatorRequest, elevatorRequest.length,
                        InetAddress.getLocalHost(), FLOOR_SERVICE_PORT);
                serviceSocket.send(sendPacket);

                //requesting floor info
                byte[] floorRequestData = {2, (byte)floorNumber};
                sendPacket = new DatagramPacket(floorRequestData, floorRequestData.length,
                        InetAddress.getLocalHost(), FLOOR_SERVICE_PORT);
                serviceSocket.send(sendPacket);
                requestData = new byte[1024];
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
                serviceSocket.receive(requestPacket);
                String requestTextData = new String(requestPacket.getData()).trim();
                var floorRequest = parseEvent(requestTextData);
                System.out.printf("Passenger from floor %d reached their destination of floor %d\n",
                        floorRequest.getCurrentFloor(), floorRequest.getFloorStop());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * floor's main thread
     * @param args
     */
    public static void main(String[] args) {
        Floor[] floors = new Floor[Floor.NUMBER_OF_FLOORS];

        for (int i = 0; i < Floor.NUMBER_OF_FLOORS; ++i) {
            floors[i] = new Floor();
        }

        for (Floor f : floors) {
            f.start();
        }

    }
}
