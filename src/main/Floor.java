package main;

import schedulerSubsystem.Scheduler;
import main.Utilities;

import java.net.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import static main.Utilities.*;

/**
 * Implements the Floor class/thread for sending requests
 *  @author James Anderson, 101147068, Mohammad Alkhaledi 101162465
 * @version 3.0 (iteration 3)
 */

public class Floor extends Thread {

    private Scheduler scheduler;
    private ArrayList<RequestEvent> details;
    private ArrayList<RequestEvent> currentEvents;
    private int floorNumber;
    private static int floorCount = 1;
    public static final int NUMBER_OF_FLOORS = 7;
    private DatagramPacket requestPacket;
    private DatagramSocket socket;


    /**
     * Floor constructor
     */
    public Floor() {
        this.details = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.scheduler = scheduler;
        floorNumber = floorCount;
        floorCount++;

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }

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
     * Registers given event to ongoing events
     *
     */
    public void register(RequestEvent x) {
        currentEvents.add(x);
    }

    private void sendPacket(RequestEvent x) {
        String msg = backToString(x);

        byte[] byteMsg = msg.getBytes();

        try {
            requestPacket = new DatagramPacket(byteMsg, byteMsg.length, InetAddress.getLocalHost(), FLOOR_SERVICE_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    private void receivePacket() {
        byte[] msg = new byte[100];

        DatagramPacket receivedPacket = new DatagramPacket(msg, msg.length);

        try {
            socket.receive(receivedPacket);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("client received something");
    }

    /**
     * method for executing a floor thread
     */

    public void run() {
        details = getEvents("src\\main\\requests.txt");
        getRelevantEvents();

        while(true) {
            for (RequestEvent x : currentEvents) {
                sendPacket(x); //Maybe

                receivePacket();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*var currentEvent = scheduler.getFloorRequest();
                System.out.printf("Passenger from Floor %d reached their destination to floor %d.\n",
                        currentEvent.getCurrentFloor(), currentEvent.getFloorStop());*/
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        Floor[] floors = new Floor[Floor.NUMBER_OF_FLOORS];

        for (int i = 0; i < Floor.NUMBER_OF_FLOORS; ++i) {
            floors[i] = new Floor();
        }

        for (Floor f : floors) {
            f.start();
        }

        System.out.println("Floors Created");
    }
}
