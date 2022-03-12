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

import static main.Utilities.backToString;
import static main.Utilities.getEvents;

/**
 * Implements the Floor class/thread for sending requests
 *  @author James Anderson, 101147068, Mohammad Alkhaledi 101162465
 * @version 3.0 (iteration 3)
 */

public class Floor extends Thread {

    private Scheduler scheduler;
    private ArrayList<RequestEvent> details;
    private CopyOnWriteArrayList<RequestEvent> currentEvents;
    private int floorNumber;
    private static int floorCount = 1;
    public static final int NUMBER_OF_FLOORS = 7;
    private DatagramPacket requestPacket;
    private DatagramSocket socket;


    /**
     * Floor constructor
     */
    public Floor(Scheduler scheduler) {
        this.details = new ArrayList<>();
        this.currentEvents = new CopyOnWriteArrayList<>();
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
            requestPacket = new DatagramPacket(byteMsg, byteMsg.length, InetAddress.getLocalHost(), 8888);
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
                scheduler.addElevatorRequest(x);
                sendPacket(x); //Maybe

                receivePacket();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.register(scheduler.getFloorRequest());
            }
        }
    }
}
