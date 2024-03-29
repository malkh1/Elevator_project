package main;

import java.time.LocalTime;

/**
 * The request made by a passenger in an elevator or a floor
 * @version 4.0 (iteration 4)
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
     * 0 - functional. 1 - transient fault. 2 - hard fault.
     */
    private int errorLevel;

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
    public UserRequest(LocalTime time, int currentFloor, boolean floorDirection, int floorStop, int errorLevel){
        this.time = time;
        this.currentFloor = currentFloor;
        this.floorDirection = floorDirection;
        this.floorStop = floorStop;
        this.errorLevel = errorLevel;
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

    @Override
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
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
        return "Time: " + time + " | Floor: " + currentFloor + " | Floor Button: " + (floorDirection ? "Up" : "Down")
                + " | Car Button: " + floorStop + "\n";
    }

    /**
     *
     * @return the userRequest in its original form, as plainText
     */
    public String toPlainText(){
        return time + "," + currentFloor + "," + floorDirection + "," + floorStop + "," + errorLevel;
    }

    /**
     * @return true if there is no error in the request
     */
    public boolean returnNoFault(){
        return errorLevel == 0;
    }

    /**
     * @return true if there's a small error in the request
     */
    public boolean returnTransientFault(){
        return errorLevel == 1;
    }

    /**
     * @return true if there's a large error in the request
     */
    public boolean returnHardFault(){
        return errorLevel == 2;
    }
}
