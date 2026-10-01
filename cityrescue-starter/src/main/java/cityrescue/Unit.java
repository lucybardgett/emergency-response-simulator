package cityrescue;

import cityrescue.exceptions.*;
import cityrescue.enums.*;

// all unit types are in this class
public abstract class Unit {
    private int id;
    private UnitType type;
    private int x;
    private int y;
    private UnitStatus status;
    private int stationId;
    private int assignedIncidentId;
    private int ticksRemaining;

// construct new unit  at location (status=idle)
public Unit(int id, UnitType type, int x, int y, int stationId) {
    this.id = id;
    this.type = type;
    this.x = x;
    this.y = y;
    this.stationId = stationId;
    this.status = UnitStatus.IDLE;
    this.assignedIncidentId = -1;
    this.ticksRemaining = 0;
}

// determines if umit can handle incident type
public abstract boolean canHandle(IncidentType type);

// number of ticks needed to resolve
public abstract int getTicksToResolve(int severity);

// calculates Manhattan distance from unit to incident location
public int manhattanDistance(int targetX, int targetY) {
    return Math.abs(x - targetX) + Math.abs(y - targetY);
}
// formatted string of representaion for unit
public String toString() {
    String incidentPart = (assignedIncidentId == -1) ? "-" : String.valueOf(assignedIncidentId);
    String result = "U#" + id + " TYPE=" + type + " HOME=" + stationId + " LOC=(" + x + "," + y + ") STATUS=" + status + " INCIDENT=" + incidentPart;
    if (status == UnitStatus.AT_SCENE) {
        result += " WORK=" + ticksRemaining;
    }
    return result;
}

// unit ID
public int getId() { return id; }

// unit type
public UnitType getType() { return type; }

// x-coordinate
public int getX() { return x; }

// y-coordinate
public int getY() { return y; }

// set x-coordinate
public void setX(int x) { this.x = x; }

// set y-coordinate
public void setY(int y) { this.y = y; }

// station ID
public int getStationId() { return stationId; }



// status
public UnitStatus getStatus() { return status; }

// set status
public void setStatus(UnitStatus status) { this.status = status; }

// Incident ID
public int getAssignedIncidentId() { return assignedIncidentId; }

// set incident ID
public void setAssignedIncidentId(int assignedIncidentId) { this.assignedIncidentId = assignedIncidentId; }

// ticks remaining
public int getTicksRemaining() { return ticksRemaining; }

// set ticks remaining
public void setTicksRemaining(int ticksRemaining) { this.ticksRemaining = ticksRemaining; }
}