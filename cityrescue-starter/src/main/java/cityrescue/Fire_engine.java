package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

// fire unit handles fire (resolve on scene = 2 ticks)
public class Fire_engine extends Unit {
    // new engine at given location
    public Fire_engine(int id, int x, int y, int stationId) {
        super(id, UnitType.FIRE_ENGINE, x, y, stationId);
    }
    // fire engine can only handle fire incidents
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.FIRE;
    }
    // takes 4 ticks to resolve incident
    @Override
    public int getTicksToResolve(int severity) {
        return 4;
    }
}