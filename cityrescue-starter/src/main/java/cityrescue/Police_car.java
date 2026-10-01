package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

// police unit handles crime (resolve on scene = 3 ticks)
public class Police_car extends Unit {
    // new police car at given location
    public Police_car(int id, int x, int y, int stationId) {
        super(id, UnitType.POLICE_CAR, x, y, stationId);
    }
    // police car can only handle crime incidents
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.CRIME;
    }
    // takes 3 ticks to resolve incident
    @Override
    public int getTicksToResolve(int severity) {
        return 3;
    }
}