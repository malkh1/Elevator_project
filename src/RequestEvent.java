import java.time.LocalTime;

/**
 * An interface that wraps all events
 * @author sarashikhhassan
 * @version 1.0 (iteration 1)
 */
public interface RequestEvent {
    LocalTime getTime(); //hour minute second 13:45.30
    int getCurrentFloor(); // the floor that the request is coming from (og point)
    boolean floorDirection(); // whether the floor button is pressed up or down
    int getFloorStop(); // the floor destination where the passenger is trying to go
    String toString();
}
