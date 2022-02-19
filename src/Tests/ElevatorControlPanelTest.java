package Tests;

import elevatorSubsystem.ElevatorLight;
import elevatorSubsystem.ElevatorControlPanel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author Sara Shikhhassan
 * @version 2.0 (iteration 2)
 */
public class ElevatorControlPanelTest {

    @Test
    public void pressButtonTest() throws InterruptedException{
        // Creating an elevator light for the floor number 5
        ElevatorLight elevatorLight = new ElevatorLight(5);

        // Creating an elevator control panel
        ElevatorControlPanel elevatorControlPanel = new ElevatorControlPanel(elevatorLight);

        // Pressing the elevator button
        elevatorControlPanel.pressButton();

        // Checking if the elevator button's light turns on
        assertEquals(true, elevatorControlPanel.getElevatorLight().checkLight());

        // Checking if the button light is associated with it's corresponding floor
        assertEquals(5, elevatorControlPanel.getElevatorLight().getFloorButton());

    }


}
