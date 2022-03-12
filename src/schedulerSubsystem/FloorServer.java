package schedulerSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static main.Utilities.*;


public class FloorServer extends Thread {
    private DatagramSocket receiverSocket;
    private DatagramPacket requestPacket;
    private Scheduler scheduler;

    public FloorServer(Scheduler scheduler) throws SocketException {
        receiverSocket = new DatagramSocket(FLOOR_SERVICE_PORT);
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while(true){
            byte[] requestData = new byte[100];
            requestPacket = new DatagramPacket(requestData, requestData.length);

            try{
                receiverSocket.receive(requestPacket);

                FloorServiceThread subThread = new FloorServiceThread(scheduler, requestData, requestPacket.getPort(), requestPacket.getAddress());
                subThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
