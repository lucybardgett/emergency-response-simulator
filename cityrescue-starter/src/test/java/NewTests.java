import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class NewTests {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }

    @Test
    void tryAddunit() throws Exception {
        int s = cr.addStation("A", 0, 0);
        int u1 = cr.addUnit(s, UnitType.POLICE_CAR);
        assertEquals(1,u1);
    }
    @Test
    void StationNullname() throws Exception{
        assertThrows(InvalidNameException.class,() ->{
        int s =cr.addStation(null,0,0);
        });
    }
    @Test
    void invalidStationID() throws Exception{
        assertThrows(IDNotRecognisedException.class,()->{
            cr.removeStation(0);
        });
    }
}