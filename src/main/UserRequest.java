package main;

import java.time.LocalTime;

/**
 * The request made by a passenger in an elevator or a floor
 * @version 1.0 (iteration 1)
 */
public class UserRequest implements RequestEvent{

    /**
     * A time attribute
     */
    private LocalTime time;
    /**
     * The current floor the request is being made from
     */
    private int currentFloor;
    /**
     * The floor destination stop that a passenger wants to go to
     */
    private int floorStop;
    /**
     * The direction of where the elevator is headed. True if upwards, false otherwise
     */
    private boolean floorDirection;

    /**
     * Constructor of the userRequest class
     */
    public UserRequest(LocalTime time, int currentFloor, boolean floorDirection, int floorStop){
        this.time = time;
        this.currentFloor = currentFloor;
        this.floorDirection = floorDirection;
        this.floorStop = floorStop;
    }

    /**
     * Getter method of the time attribute
     * @return the time in the format 3:45.30
     */
    @Override
    public LocalTime getTime() {
        return time;
    }

    /**
     * Getter method of the current floor attribute
     * @return the floor number of where the passenger is requesting the elevator
     */
    @Override
    public int getCurrentFloor() {
        return this.currentFloor;
    }

    /**
     * The direction of the floor
     * @return true if the direction is up, false if the direction is down
     */
    @Override
    public boolean floorDirection(){
        return this.floorDirection;
    }

    /**
     * Getter method of the floor stop attribute
     * @return the floor number of where the passenger is headed
     */
    @Override
    public int getFloorStop() {
        return this.floorStop;
    }

    /**
     * A toString method
     * @return the string representation of the objects
     */
    @Override
    public String toString() {
        return "Time: " + time + " | Floor: " + currentFloor + " | Floor Button: " + (floorDirection ? "Up" : "Down") + " | Car Button: " + floorStop + "\n";
    }


}
