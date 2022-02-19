package Tests;

import elevatorSubsystem.ElevatorLight;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sara Shikhhassan
 * @version 2.0 (iteration 2)
 */
public class ElevatorLightTest {
    @Test
    public void lightTest() throws InterruptedException{

        // Creating an elevator light for the floor number 5
        ElevatorLight elevatorLight = new ElevatorLight(5);

        // Turning on the button light
        elevatorLight.lightOn();

        // Checking if the light turned on
        assertEquals(true, elevatorLight.checkLight());

        // Turning off the button light
        elevatorLight.lightOff();

        // Checking if the light turned off
        assertEquals(false, elevatorLight.checkLight());

        // Checking if the light button corresponds to the correct floor 5
        assertEquals(5, elevatorLight.getFloorButton());

    }

}
