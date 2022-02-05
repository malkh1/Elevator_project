package Tests;

import main.UserRequest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class UserRequestTest {
    private UserRequest userRequest;

    @Before
    public void setUp() throws Exception {
        userRequest = new UserRequest(LocalTime.of(13,45,30,0), 2
                , true, 4);
    }

    @Test
    public void getTime() {
        assertEquals(LocalTime.of(13, 45, 30), userRequest.getTime());
    }

    @Test
    public void getCurrentFloor() {
        assertEquals(2, userRequest.getCurrentFloor());

    }

    @Test
    public void floorDirection() {
        assertTrue(userRequest.floorDirection());
    }

    @Test
    public void getFloorStop() {
        assertEquals(4, userRequest.getFloorStop());
    }
}