package Tests;

import main.UserRequest;
import org.junit.Before;
import org.junit.Test;
import schedulerSubsystem.Scheduler;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class SchedulerTest {
    private Scheduler scheduler;

    private UserRequest userRequest;

    @Before
    public void setUp() throws Exception {
        scheduler = new Scheduler();
        userRequest =
                new UserRequest
                        (LocalTime.of(12,12,12), 1, true, 5, 0);
    }

    @Test
    public void getFloorRequestAvailable() {
        assertFalse(scheduler.getFloorRequestAvailable());
    }

    @Test
    public void getElevatorRequestAvailable() {
        assertFalse(scheduler.getElevatorRequestAvailable());
    }
    
    
    @Test
    public void getFloorRequest() {
        scheduler.addFloorRequest(userRequest);
        assertEquals(scheduler.getFloorRequest(), userRequest);
    }

}
