package elevatorSubsystem;

/**
 * Class that controls the light on the elevator button
 * @author Sara Shikhhassan
 * @version 2.0 (iteration 2)
 */
public class ElevatorLight {

    private int floorButton;
    private boolean light;

    /**
     * The constructor for the class elevator control panel
     * @param floorButton as an int representing the floor number button
     */
    public ElevatorLight(int floorButton){
        this.floorButton = floorButton;
        this.light = false; // starts w the state off
    }

    /**
     * Method that turns the light on
     */
    public void lightOn(){
        this.light = true;
        System.out.println("The light button for floor number " + floorButton + " is on.");
    }

    /**
     * Method that turns the light off
     */
    public void lightOff(){
        this.light = false;
        System.out.println("The light button for floor number " + floorButton + " is off.");
    }

    /**
     * Method that checks to see if the light is on or off
     * @return true if button light is on, false if off
     */
    public boolean checkLight(){
        return this.light;
    }

    /**
     * Getter method of the floorButton attribute
     * @return the floor number button
     */
    public int getFloorButton(){
        return this.floorButton;
    }

}
