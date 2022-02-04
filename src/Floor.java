import java.time.LocalTime;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Implements the Floor class/thread for sending requests
 *  @author James Anderson, 101147068
 * @version 1.0 (iteration 1)
 */

public class Floor extends Thread {

    private Scheduler scheduler;
    private ArrayList<RequestEvent> details;
    private ArrayList<RequestEvent> currentEvents;

    /**
     * Floor constructor
     */

    public Floor(Scheduler sch) {
        this.details = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.scheduler = sch;
    }

    /**
     * Reads events from given text file
     * @return the parsed event info from the text file derived from path
     */

    public static ArrayList<RequestEvent> getEvents(String path) {
        ArrayList<RequestEvent> evs = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String l;
            l = reader.readLine();

            ArrayList<String> input = parseLine(l);

            while (l != null) {
                userRequest newEvent = new userRequest(LocalTime.parse(input.get(0)), Integer.parseInt(input.get(1)),
                        Boolean.parseBoolean(input.get(2)), Integer.parseInt(input.get(3)));
                evs.add(newEvent);
                l = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return evs;
    }

    /**
     * Parses through a given input string
     * @return an ArrayList of strings
     */

    private static ArrayList<String> parseLine(String l) {
        String[] temp = l.split(",");
        List<String> al;
        al = Arrays.asList(temp);

        return (ArrayList<String>) al;
    }


    /**
     * Registers given event to ongoing events
     *
     */

    public void register(RequestEvent x) {
        currentEvents.add(x);
    }

    /**
     * @return the events scheduled for this floor
     */

    public ArrayList<RequestEvent> getDetails(){
        return this.details;
    }

    /**
     * method for executing a floor thread
     */

    public synchronized void run() {
        details = getEvents("src/test.txt"); // replace this obv

        for (RequestEvent x : details) {
            scheduler.addElevatorRequest(x);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.register(scheduler.getFloorRequest());
        }
    }
}
