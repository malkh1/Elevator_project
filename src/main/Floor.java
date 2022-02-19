package main;

import java.time.LocalTime;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implements the Floor class/thread for sending requests
 *  @author James Anderson, 101147068
 * @version 1.0 (iteration 1)
 */

public class Floor extends Thread {

    private Scheduler scheduler;
    private ArrayList<RequestEvent> details;
    private CopyOnWriteArrayList<RequestEvent> currentEvents;
    private int floorNumber;
    private static int floorCount = 1;
    public static final int NUMBER_OF_FLOORS = 7;

    /**
     * Floor constructor
     */
    public Floor(Scheduler scheduler) {
        this.details = new ArrayList<>();
        this.currentEvents = new CopyOnWriteArrayList<>();
        this.scheduler = scheduler;
        floorNumber = floorCount;
        floorCount++;

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


    /**
     * method for executing a floor thread
     */

    public void run() {
        details = getEvents("src\\main\\requests.txt");
        getRelevantEvents();

        for (RequestEvent x : currentEvents) {
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
