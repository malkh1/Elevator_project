package main;

import java.time.LocalTime;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    public Floor(Scheduler scheduler) {
        this.details = new ArrayList<>();
        this.currentEvents = new ArrayList<>();
        this.scheduler = scheduler;
    }

    /**
     * Reads events from given text file
     * @return the parsed event info from the text file derived from path
     */

    public static ArrayList<RequestEvent> getEvents(String path) {
        ArrayList<RequestEvent> events = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String lineInput;
            ArrayList<String> input;

            while ((lineInput = reader.readLine()) != null) {
                input = parseLine(lineInput);
                UserRequest newEvent = new UserRequest(LocalTime.parse(input.get(0)), Integer.parseInt(input.get(1)),
                        Boolean.parseBoolean(input.get(2)), Integer.parseInt(input.get(3)));
                events.add(newEvent);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    /**
     * Parses through a given input string
     * @return an ArrayList of strings
     */

    private static ArrayList<String> parseLine(String l) {
        String[] temp = l.split(",");
        ArrayList<String> al = new ArrayList<>();
        for(String s : temp) {
            al.add(s);
        }

        return al;
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

    public void run() {
        details = getEvents("/Users/sarashikhhassan/Desktop/Iteration1/src/main/requests.txt");

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
