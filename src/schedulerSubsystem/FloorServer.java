package schedulerSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static main.Utilities.*;

/**
 * implements the floor server
 * @author Mohammad Alkhaledi, James Anderson
 * @version 3.0
 */
public class FloorServer extends Thread {
    private DatagramSocket receiverSocket;
    private Scheduler scheduler;

    /**
     * Default constructor for the floor server
     * @param scheduler scheduler to be passed down to threads
     * @throws SocketException in case sockets fails to bind to port
     */
    public FloorServer(Scheduler scheduler) throws SocketException {
        receiverSocket = new DatagramSocket(FLOOR_SERVICE_PORT);
        this.scheduler = scheduler;
    }

    /**
     * the floor server's thread of execution
     */
    @Override
    public void run() {
        while(true){
            byte[] requestData = new byte[1024];
            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
            System.out.printf("\nFloor_Server>>>Listening on port %d for elevator requests..\n", FLOOR_SERVICE_PORT);
            try{
                receiverSocket.receive(requestPacket);
                System.out.println("\nElevator_Server>>>Received an elevator request from " + requestPacket.getAddress());

                FloorServiceThread serviceThread = null;

                if(requestData[0] == 1){
                    String elevatorRequest = new String(requestData).trim();
                    serviceThread = new FloorServiceThread(scheduler, elevatorRequest);
                    serviceThread.start();
                }
                else if(requestData[0] == 2){
                    serviceThread = new FloorServiceThread(scheduler, requestPacket.getPort(), requestPacket.getAddress());
                    serviceThread.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}