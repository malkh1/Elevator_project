package schedulerSubsystem;

import main.UserRequest;
import main.Utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Service thread created by the elevator server to deal with elevator requests
 * @author Mohammad Alkhaledi 101162465
 * @version 3.0
 */
public class ElevatorServiceThread extends Thread{
    private Scheduler scheduler;
    private InetAddress ipAddress;
    private int port;
    private boolean elevatorRequest;
    private boolean floorRequest;
    private String requestToAdd;
    /**
     * Default constructor for ElevatorServiceThread when servicing requests for work
     * @param scheduler scheduler that the thread will communicate with
     * @param inetAddress ip address of the elevator
     * @param port port to send
     */
    public ElevatorServiceThread(Scheduler scheduler, InetAddress inetAddress,
                                 int port){
        this.scheduler = scheduler;
        ipAddress = inetAddress;
        this.port = port;
        elevatorRequest = true;
        floorRequest = false;
    }

    /**
     * Constructor for the elevator service thread when adding a floor request
     * @param scheduler the scheduler to communicate with
     * @param floorRequest
     */
    public ElevatorServiceThread(Scheduler scheduler, String floorRequest){
        this.scheduler = scheduler;
        elevatorRequest = false;
        this.floorRequest = true;
        requestToAdd = floorRequest;
    }

    /**
     *
     */
    @Override
    public void run(){
        while(elevatorRequest){
            try {
                var userRequest = ((UserRequest) scheduler.getElevatorRequest()).toPlainText();
                DatagramPacket sendRequestPacket = new DatagramPacket(userRequest.getBytes(),userRequest.length(),
                        ipAddress, port);
                DatagramSocket serviceSocket = new DatagramSocket();
                serviceSocket.send(sendRequestPacket);
                serviceSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            elevatorRequest = false;

        }
        while (floorRequest) {
            var userRequest = Utilities.parseEvent(requestToAdd);
            scheduler.addFloorRequest(userRequest);
            floorRequest = false;
        }
    }
}
