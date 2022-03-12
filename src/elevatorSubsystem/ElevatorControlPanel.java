package elevatorSubsystem;

/**
 * Class for the control panel of the elevator which controls the button switches, and the door
 * @author Sara Shikhhassan
 * @version 2.0 (iteration 2)
 */

public class ElevatorControlPanel {
    private ElevatorLight elevatorLight;
    private int floorButton;


    /**
     * Constructor method for the class
     * @param elevatorLight is the light button
     */
    public ElevatorControlPanel(ElevatorLight elevatorLight){
        this.elevatorLight = elevatorLight;
        this.floorButton = elevatorLight.getFloorButton();
    }

    /**
     * Getter method of the light switch
     * @return elevatorLight object
     */
    public ElevatorLight getElevatorLight(){
        return this.elevatorLight;
    }

    /**
     * Method for pressing the elevator button which turns on the light
     * corresponding to the floor number.
     */
    public void pressButton(){
        this.elevatorLight.lightOn();
        System.out.println("The button for floor number " + floorButton +" has been pressed.");
    }


    /**
     * Opens and closes the doors of an elevator
     * @throws InterruptedException when the thread is interrupted, throw exception
     */
    public void controlDoor() throws InterruptedException{
        Thread.sleep(500);
        System.out.println("The elevator door was open and it is now closed.");
    }

}
