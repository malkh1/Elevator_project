package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utilities class that carries static fields and methods for other classes to use
 * @author Mohammad Alkhaledi
 * @version 4.0
 */
public class Utilities {
    public static final int ELEVATOR_SERVICE_PORT = 7777;
    public static final int FLOOR_SERVICE_PORT = 8888;
    public static final long ELEVATOR_TRAVEL_SPEED = 2000;
    public static final long ELEVATOR_DOOR_SPEED = 1000;

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
                UserRequest newEvent = (UserRequest) parseEvent(input);
                events.add(newEvent);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    /**
     * takes a plainText request broken into parts, and returns a requestevent
     * @param input parsed plainText request event
     * @return requestevent object
     */
    public static RequestEvent parseEvent(ArrayList<String> input){
        return new UserRequest(LocalTime.parse(input.get(0)), Integer.parseInt(input.get(1)),
                Boolean.parseBoolean(input.get(2)), Integer.parseInt(input.get(3)), Integer.parseInt(input.get(4)));
    }

    /**
     * takes a plainText request broken into parts, and returns a requestevent
     * @param unparsedString the plainText of a request event
     * @return requestevent object
     */
    public static RequestEvent parseEvent(String unparsedString){
        String[] input = unparsedString.split(",");
        return new UserRequest(LocalTime.parse(input[0]), Integer.parseInt(input[1]),
                Boolean.parseBoolean(input[2]), Integer.parseInt(input[3]), Integer.parseInt(input[4]));
    }

    /**
     * Parses through a given input string
     * @return an ArrayList of strings
     */
    private static ArrayList<String> parseLine(String l) {
        String[] temp = l.split(",");

        return new ArrayList<>(Arrays.asList(temp));
    }
}
