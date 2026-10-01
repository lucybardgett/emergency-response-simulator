package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

// ambulance unit handles medical (resolve on scene = 2 ticks)
public class Ambulance extends Unit {
    // new abulance at given location
    public Ambulance(int id, int x, int y, int stationId) {
        super(id, UnitType.AMBULANCE, x, y, stationId);
    }
    // ambulance can only handle medical incidents
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.MEDICAL;
    }
    // takes 2 ticks to resolve incident
    @Override
    public int getTicksToResolve(int severity) {
        return 2;
    }
}