package schedulerSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static main.Utilities.ELEVATOR_SERVICE_PORT;

/**
 * Server thread to receive requests from elevators, and creates an ElevatorServiceThread to handle each request
 */
public class ElevatorServer extends Thread{
    private DatagramSocket receiverSocket;
    private DatagramPacket requestPacket;
    private Scheduler scheduler;


    /**
     * Default constructor for the elevatorServer.
     * @param scheduler scheduler that the server will communicate with
     * @throws SocketException exception in case socket error occurs
     */
    public ElevatorServer(Scheduler scheduler) throws SocketException{
        receiverSocket = new DatagramSocket(ELEVATOR_SERVICE_PORT);
        this.scheduler = scheduler;
    }

    /**
     * the server's thread of execution
     */
    @Override
    public void run() {
        while(true){
            byte[] requestData = new byte[2];
            requestPacket = new DatagramPacket(requestData, requestData.length);

            System.out.printf("Scheduler>>>Listening on port %d for elevator requests..\n", ELEVATOR_SERVICE_PORT);
            try{
                receiverSocket.receive(requestPacket);
                String request = new String(requestData);
                //parse request information

                System.out.println("Scheduler>>>Received an elevator request at ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
