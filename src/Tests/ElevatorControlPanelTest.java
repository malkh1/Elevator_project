package Tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author Sara Shikhhassan
 * @version 2.0 (iteration 2)
 */
public class ElevatorControlPanelTest {

    @Test
    void pressButtonTest() throws InterruptedException{
        // Creating an elevator light for the floor number 5
        ElevatorLight elevatorLight = new ElevatorLight(5);

        // Creating an elevator control panel
        ElevatorControlPanelTest elevatorControlPanelTest = new ElevatorControlPanelTest(elevatorLight);

        // Pressing the elevator button
        elevatorControlPanelTest.pressButton();

        // Checking if the elevator button's light turns on
        assertEquals(true, elevatorControlPanelTest.getElevatorLight().checkLight());

        // Checking if the button light is associated with it's corresponding floor
        assertEquals(5, elevatorControlPanel.getElevatorLight().getFloorButton());

// FIX ME: HOW DO WE CHECK IF THE CONTROLDOOR METHOD IS WORKING?

    }


}
