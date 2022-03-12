package schedulerSubsystem;

import main.RequestEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class FloorServiceThread extends Thread {
    private Scheduler scheduler;
    private byte[] event;
    private int sourcePort;
    private InetAddress sourceAddress;

    public FloorServiceThread (Scheduler scheduler, byte[] event, int sPort, InetAddress sAddress) {
        this.scheduler = scheduler;
        this.event = event;
        this.sourcePort = sPort;
        this.sourceAddress = sAddress;
    }

    @Override
    public void run() {
        try {
            DatagramPacket sendPacket = new DatagramPacket(event, event.length, this.sourceAddress, this.sourcePort);
            DatagramSocket socket = new DatagramSocket();
            socket.send(sendPacket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
