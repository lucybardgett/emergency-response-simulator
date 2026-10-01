package cityrescue;

import cityrescue.exceptions.*;
import cityrescue.enums.*;

// represents emergency 
public class Incident {
    private int id;
    private IncidentType type;
    private int severity;
    private int x;
    private int y;
    private IncidentStatus status;
    private int assignedUnitId; // -1 = no unit assigned

// reported incident with no unit assigned
    public Incident(int id, IncidentType type, int severity, int x, int y) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.status = IncidentStatus.REPORTED;
        this.assignedUnitId = -1;
    }

    // incident id
    public int getIncidentId() { return id; }

    // incident type
    public IncidentType getType() { return type; }

    // current severity
    public int getSeverity() { return severity; }

    // updates severity
    public void setSeverity(int severity) { this.severity = severity; }

    // x-coordinate
    public int getX() { return x; }

    // y-coordinate
    public int getY() { return y; }

    // current status
    public IncidentStatus getStatus() { return status; }
    
    // update status
    public void setStatus(IncidentStatus status) { this.status = status; }

    // assigned unit Id
    public int getAssignedUnitId() { return assignedUnitId; }

    // assign a unit
    public void setAssignedUnitId(int unitId) { this.assignedUnitId = unitId; }

    // formatted string representation of inciddent
    public String toString() {
       String StringIncident="I#" + id + " TYPE=" + type + " SEV=" + severity +
        " LOC =(" + x + "," + y + ") STATUS=" + status + " UNIT=" + assignedUnitId;
        return StringIncident;
    }


}