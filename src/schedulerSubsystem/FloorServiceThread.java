package schedulerSubsystem;

import main.UserRequest;
import main.Utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Service thread created by the floor server to deal with floor requests
 * @author Mohammad Alkhaledi, James Anderson
 * @version 3.0
 */
public class FloorServiceThread extends Thread {
    private Scheduler scheduler;
    private int sourcePort;
    private InetAddress sourceAddress;
    private boolean elevatorRequest;
    private boolean floorRequest;
    private String requestToAdd;

    /**
     * Constructor when servicing 'get floor requests'
     * @param scheduler scheduler to be communicated with
     * @param port port to communicate on
     * @param inetAddress address of the host
     */
    public FloorServiceThread (Scheduler scheduler, int port, InetAddress inetAddress) {
        this.scheduler = scheduler;
        sourcePort = port;
        sourceAddress = inetAddress;
        floorRequest = true;
        elevatorRequest = false;
    }

    /**
     * constructor when servicing 'send elevator requests'
     * @param scheduler scheduler to be communicated with
     * @param elevatorRequest the elevator request in plainText
     */
    public FloorServiceThread(Scheduler scheduler, String elevatorRequest){
        this.scheduler = scheduler;
        requestToAdd = elevatorRequest;
        floorRequest = false;
        this.elevatorRequest = true;
    }

    @Override
    public void run() {
        try {
            while(elevatorRequest){
                var userRequest = Utilities.parseEvent(requestToAdd);
                scheduler.addElevatorRequest(userRequest);
                elevatorRequest = false;
            }
            while (floorRequest){
                var userRequest = ((UserRequest) scheduler.getFloorRequest()).toPlainText();
                DatagramPacket sendRequestPacket = new DatagramPacket(userRequest.getBytes(), userRequest.length(),
                        sourceAddress, sourcePort);
                DatagramSocket serviceSocket = new DatagramSocket();
                serviceSocket.send(sendRequestPacket);
                serviceSocket.close();
                floorRequest = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}