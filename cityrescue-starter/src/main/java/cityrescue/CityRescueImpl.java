package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {


    public CityMap Map;

    //stations
    private Station[] stations;
    private int stationCount=0;
    private int nextStationId;

    //units
    private Unit[] units;
    private int unitsCount;
    private int nextUnitId;

    //incidents
    private Incident[] incidents;
    private int incidentCount;
    private int nextIncidentId;

    //time
    private int tick =0;
    
    private int TotalUnits = 0;
    private int width;
    private int height;


    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if(width <=0 || height <=0){
            throw new InvalidGridException("not valid width /height");
        }
        
        Map = new CityMap(width, height);

        // setting maximum number of stations, units and incidents
        stations = new Station[20];
        stationCount = 0;
        nextStationId = 1;

        units = new Unit[50];
        TotalUnits = 0;
        nextUnitId = 1;

        incidents = new Incident[200];
        incidentCount = 0;
        nextIncidentId = 1;

        tick = 0;
    }

    @Override
    // decide grid size
    public int[] getGridSize() {
       this.width = Map.getWidth();
       this.height = Map.getHeight();
       return new int[] {this.width, this.height};
     }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if(!Map.isInBounds(x,y)){
            throw new InvalidLocationException("not on the grid");
        }
        // use setBlocked from CityMap to show blocked coordinate
        Map.setBlocked(x, y,true);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
         if(!Map.isInBounds(x,y)){
            throw new InvalidLocationException("not on the grid"); //check in bounds
        } // use set blocked from cityMap
        Map.setBlocked(x, y,false);
    
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        // station name is required
        if(name == null || name.isBlank()){
        throw new InvalidNameException("please enter a name");
        }
        // coordinate must be within bounds of map
        if(!Map.isInBounds(x,y) || Map.isBlocked(x, y)){
            throw new InvalidLocationException("not a valid coordinate");
        }
        int stationId = ++stationCount; // incremental station counter
        Station newStation = new Station( stationId, name,x,y);
        stations[stationId-1] = newStation;
        Map.setBlocked(x, y, true);
        return stationId;
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        if(stationId<1 || stationId>stations.length){ 
            throw new IDNotRecognisedException("not a valid station ID");
        } //CHECK STATION ID is valid 
        if(stations[stationId-1]==null){
            throw new IDNotRecognisedException("not a valid station ID");
        }
        Station currentStation=stations[stationId-1];
        int units = currentStation.getUnitCount(); //dont allow removing a station with units
        if(units!=0){
            throw new IllegalStateException("cannot remove a station with units");
        }
        stations[stationId]=null; //remove station
        }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        if(stationId<1 || stationId>stations.length){
            throw new IDNotRecognisedException("not a valid station ID");
        } //check valid station id
        if(stations[stationId-1]==null){
            throw new IDNotRecognisedException("not a valid station ID");
        }
        Station currentStation = stations[stationId-1]; //check space for units 
        if(maxUnits<=0 || maxUnits< currentStation.getUnitCount()){
            throw new InvalidCapacityException("max stations already reached");
        }
        currentStation.setCapacity(maxUnits);
        }

    @Override
    public int[] getStationIds() {
      int idCount=0;
      int index = 0; 
      for(int i =0;i<stations.length;i++){
        if(stations[i]!=null){
            idCount++;
        }//check how many stations there are so blank stations are not printed
     }
      int[] tempIDS = new int[idCount];
      for(int i=0;i<stations.length;i++){
        if(stations[i]!=null){
            tempIDS[index]=i+1;// create new array of only not blank stations
            index++;
        }
      }
      return tempIDS;
     }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException, CapacityExceededException {
        Station currentStation = null;
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].getId() == stationId){//find correct station
                currentStation = stations[i];
                break;
            }
        }
        if(stationId<=0 ||stationId>=stations.length||currentStation==null){
            throw new IDNotRecognisedException("not a valid station ID");
        } //check real station and it has capacity
        if(currentStation.getUnitCount()==currentStation.getCapacity()){
            throw new CapacityExceededException("unit capacity reached");
        }
        int unitId = ++TotalUnits;
        int x = currentStation.getStationx();
        int y = currentStation.getStationy();
        Unit NewUnit=null;
        switch (type) {
            case AMBULANCE:
                NewUnit = new Ambulance(unitId, x, y, stationId);
                break;
        
            case FIRE_ENGINE :
                NewUnit= new Fire_engine(unitId, x, y, stationId);
                break;
            case POLICE_CAR :
                NewUnit = new Police_car(unitId, x, y, stationId);
                break;
        }
        currentStation.SetUnits(NewUnit);
        units[unitsCount] = NewUnit;
        unitsCount++;
        return unitId;
    }
            


    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
      if (unitId<=0 || unitId>TotalUnits || units[unitId-1]==null){
        throw new IDNotRecognisedException("ID not recognised");
      }
      Unit currentUnit = units[unitId-1];
      UnitStatus Status =currentUnit.getStatus();
      if(Status == UnitStatus.EN_ROUTE || Status == UnitStatus.AT_SCENE){
        throw new IllegalStateException("not a valid unit status for decomission");
      }
      units[unitId-1]=null;
      int stationId = currentUnit.getId();
      Station currentStation = stations[stationId-1];
      currentStation.removeUnit(unitId);  
    
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        if(newStationId>stations.length || newStationId<=0 || stations[newStationId-1]==null){
            throw new IDNotRecognisedException("not a valid station ID");
        }
        if(unitId<=0 || unitId>=TotalUnits || units[unitId-1]==null){
            throw new IDNotRecognisedException("not a valid unit id");
        }
        Unit chosenUnit=units[unitId-1];
        Station currentStation = stations[chosenUnit.getStationId()-1];
        Station chosenStation=stations[newStationId-1];
        if(chosenStation.getUnitCount()==chosenStation.getCapacity()|| chosenUnit.getStatus() != UnitStatus.IDLE){
            throw new IllegalStateException("invalid unit or station status");
        }
        currentStation.removeUnit(unitId);
        chosenStation.SetUnits(chosenUnit);
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        if(unitId<=0 || unitId>=TotalUnits || units[unitId-1]==null){
            throw new IDNotRecognisedException("not a valid unit ID");
        }
        Unit chosenUnit = units[unitId-1];
        if(chosenUnit.getStatus() != UnitStatus.IDLE && chosenUnit.getStatus() != UnitStatus.OUT_OF_SERVICE ){
            throw new IllegalStateException("invalid unit state");
        }
        if (outOfService){
            if(chosenUnit.getStatus() != UnitStatus.IDLE){
                throw new IllegalStateException("invalid unit state");
            }
            chosenUnit.setStatus(UnitStatus.OUT_OF_SERVICE);
        }  
        else {
            if(chosenUnit.getStatus() != UnitStatus.OUT_OF_SERVICE){
                throw new IllegalStateException("invalid unit state");
            }
            chosenUnit.setStatus(UnitStatus.IDLE);
        }


    }

    @Override
    public int[] getUnitIds() {
      int UnitsCount=0;
      int index = 0; 
      for(int i =0;i<units.length;i++){
        if(units[i]!=null){
            UnitsCount++;
        }
     }
      int[] tempUNITS = new int[UnitsCount];
      for(int i=0;i<units.length;i++){
        if(units[i]!=null){
            tempUNITS[index]=i+1;
            index++;
        }
      }
      return tempUNITS;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
       for (int i = 0; i < unitsCount; i++) {
            if (units[i] != null && units[i].getId() == unitId) {
                return units[i].toString();
            }
       }
        throw new IDNotRecognisedException("unit doesnt exist");
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        if (type == null) {
            throw new InvalidLocationException("Incident type can't be nothing");
        }
        if (severity < 1 || severity > 5) {
            throw new InvalidSeverityException("Severity must be graded between 1 and 5");
        }
        if (!Map.isInBounds(x, y)) {
            throw new InvalidLocationException("Location out of bounds");
        }
        if (Map.isBlocked(x, y)) {
            throw new InvalidLocationException("Location is blocked");
        }
        if (incidentCount >= 200) {
            throw new CapacityExceededException("Maximum incidents reached");
        }

        // create the incident
        Incident incident = new Incident(nextIncidentId, type, severity, x, y);
        incidents[incidentCount] = incident;
        incidentCount++;
        nextIncidentId++;
        return incident.getIncidentId();
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        // find incident
        Incident incident = null;
        for (int i = 0; i < incidentCount; i++) {
            if (incidents[i].getIncidentId() == incidentId) {
                incident = incidents[i];
                break;
            }
        }
        if (incident == null) {
            throw new IDNotRecognisedException("Incident ID not found: " + incidentId);
        }
        if (incident.getStatus() != IncidentStatus.REPORTED &&
            incident.getStatus() != IncidentStatus.DISPATCHED) {
            throw new IllegalStateException("Can only cancel REPORTED or DISPATCHED incidents");
        }

        // returns assigned unit back to idle if dispatched
        if (incident.getStatus() == IncidentStatus.DISPATCHED) {
            int unitId = incident.getAssignedUnitId();
            for (int i = 0; i < TotalUnits; i++) {
                if (units[i].getId() == unitId) {
                    units[i].setStatus(UnitStatus.IDLE);
                    units[i].setAssignedIncidentId(-1);
                    break;
                }
            }
        }
        //update incident
        incident.setStatus(IncidentStatus.CANCELLED);
        incident.setAssignedUnitId(-1);
            
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        // find incident
        Incident incident = null;
        for (int i = 0; i < incidentCount; i++) {
            if (incidents[i].getIncidentId() == incidentId) {
                incident = incidents[i];
                break;
            }
        }

        if (incident == null) {
            throw new IDNotRecognisedException("Incident ID not found: " + incidentId);
        }
        if (newSeverity < 1 || newSeverity > 5) {
            throw new InvalidSeverityException("Severity must be between 1 and 5");
        }
        if (incident.getStatus() == IncidentStatus.RESOLVED ||
            incident.getStatus() == IncidentStatus.CANCELLED) {
            throw new IllegalStateException("Can't escalate a RESOLVED or CANCELLED incident");
            }
        //update the severity
        incident.setSeverity(newSeverity);
    }

    @Override
    public int[] getIncidentIds() {
        int[] ids = new int[incidentCount];
        for (int i = 0; i < incidentCount; i++) {
            ids[i] = incidents[i].getIncidentId();
        }
        return ids;
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        for (int i = 0; i < incidentCount; i++) {
            if (incidents[i].getIncidentId() == incidentId) {
                return incidents[i].toString();
            }
        }
        throw new IDNotRecognisedException("Incident ID not found: " + incidentId);
    }

    @Override
    public void dispatch() {
        for (int i = 0; i < incidentCount; i++) {
            Incident incident = incidents[i];

            //only process the reported incidents
            if (incident.getStatus() != IncidentStatus.REPORTED) {
                continue;
            }

            //use tie breakers to find best unit
            Unit bestUnit = null;
            int bestDistance = Integer.MAX_VALUE;

            for (int j = 0; j <= TotalUnits; j++) {
                Unit unit = units[j];

                if (unit == null) continue; 

                // must be idle to handle inciddent type
        
                if (unit.getStatus() != UnitStatus.IDLE) {
                    continue;
                }
                if (unit.getStatus() == UnitStatus.OUT_OF_SERVICE) {
                    continue;
                }
                if (!unit.canHandle(incident.getType())) {
                    continue;
                }

                int distance = unit.manhattanDistance(incident.getX(), incident.getY());

                //applying tie breakers
                if (bestUnit == null) {
                    bestUnit = unit;
                    bestDistance = distance;
                    // TB 1 - shortest distance wins
                } else if (distance < bestDistance) {
                    bestUnit = unit;
                    bestDistance = distance;
                    // TB 2 - lowest Unit ID wins
                } else if (distance == bestDistance && unit.getId() < bestUnit.getId()) {
                    bestUnit = unit;
                    // TB 3 - lowest Home Station ID wins
                } else if (distance == bestDistance &&
                            unit.getId() == bestUnit.getId() &&
                            unit.getStationId() < bestUnit.getStationId()) {
                        bestUnit = unit;
                }
            }
            // if unit is found, assign it
            if (bestUnit != null) {
                bestUnit.setStatus(UnitStatus.EN_ROUTE);
                bestUnit.setAssignedIncidentId(incident.getIncidentId());
                incident.setStatus(IncidentStatus.DISPATCHED);
                incident.setAssignedUnitId(bestUnit.getId());
            }

            }
        }
    

    @Override
    public void tick() {
        tick++; //incremental tick counter

        //move en_route units in ascending unit ID order
        for (int i=0; i < TotalUnits; i++) {
            Unit unit = units[i];

            if (unit.getStatus() != UnitStatus.EN_ROUTE) {
                continue;
            }

            // find target incident location
            Incident target = null;
            for (int j = 0; j < incidentCount; j++) {
                if (incidents[j].getIncidentId() == unit.getAssignedIncidentId()) {
                    target = incidents[j];
                    break;
                }
            }

            if (target == null) continue;

            int targetX = target.getX();
            int targetY = target.getY();

            //moves in order N, E, S, W
            int[] dx = {0, 1, 0, -1};
            int[] dy = {-1, 0, 1, 0};

            int currentDist = unit.manhattanDistance(targetX, targetY);

            // find move that reduces manhattan distance
            boolean moved = false;
            for (int d = 0; d < 4; d++) {
                int newX = unit.getX() + dx[d];
                int newY = unit.getY() + dy[d];

                // check if in bounds and not blocked
                if (!Map.isInBounds(newX, newY)) continue;
                if (Map.isBlocked(newX, newY)) continue;

                int newDist = Math.abs(newX - targetX) + Math.abs(newY - targetY);
                if (newDist < currentDist) {
                    unit.setX(newX);
                    unit.setY(newY);
                    moved = true;
                    break;
                }
            }
            // take first legal move is distance can't be reduced
            if (!moved) {
                for (int d = 0; d < 4; d++) {
                    int newX = unit.getX() + dx[d];
                    int newY = unit.getY() + dy[d];

                    if (!Map.isInBounds(newX, newY)) continue;
                    if (!Map.isBlocked(newX, newY)) continue;

                    unit.setX(newX);
                    unit.setY(newY);
                    moved = true;
                    break;
                }
            }
        }
        
        // check if en route units have reached destination
        for (int i = 0; i < unitsCount; i++) {
            Unit unit = units[i];

            if (unit.getStatus() != UnitStatus.EN_ROUTE) continue;

            Incident target = null;
            for (int j = 0; j < incidentCount; j++) {
                if (incidents[j].getIncidentId() == unit.getAssignedIncidentId()) {
                    target = incidents[j];
                    break;
                }
            }

            if (target == null) continue;

            // check if unit has arrived
            if (unit.getX() == target.getX() && unit.getY() == target.getY()) {
                unit.setStatus(UnitStatus.AT_SCENE);
                target.setStatus(IncidentStatus.IN_PROGRESS);
                unit.setTicksRemaining(unit.getTicksToResolve(target.getSeverity()));
            }
        }

        // reduce ticks for all units at scene
        for (int i = 0; i < TotalUnits; i++) {
            Unit unit = units[i];

            if (unit.getStatus() != UnitStatus.AT_SCENE) continue;

            unit.setTicksRemaining(unit.getTicksRemaining() - 1);
        }

        // resolve the completed incidents
        for (int i = 0; i < incidentCount; i++) {
            Incident incident = incidents[i];

            if (incident.getStatus() != IncidentStatus.IN_PROGRESS) continue;

            // find the assigned unit
            Unit assignedUnit = null;
            for (int j = 0; j <= TotalUnits; j++) {
                if (units[j].getId() == incident.getAssignedUnitId()) {
                    assignedUnit = units[j];
                    break;
                }
            }
            
            if (assignedUnit == null) continue;

            // resolve if ticks have run out
            if (assignedUnit.getTicksRemaining() <= 0) {
                incident.setStatus(IncidentStatus.RESOLVED);
                assignedUnit.setStatus(UnitStatus.IDLE);
                assignedUnit.setAssignedIncidentId(-1);
            }
        }
    }

    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        // line 1
        sb.append("TICK=").append(tick).append("\n");
        // line 2
        sb.append("STATIONS=").append(stationCount)
          .append(" UNITS=").append(TotalUnits)
          .append(" INCIDENTS=").append(incidentCount)
          .append(" OBSTACLES=").append(Map.countObstacles())
          .append("\n");
        
        //incidents sction
        sb.append("INCIDENTS\n");
        for (int i = 0; i < incidentCount; i++) {
            sb.append(incidents[i].toString()).append("\n");
        }

        //unit section
        sb.append("UNITS\n");
        for (int i = 0; i < TotalUnits; i++) {
            sb.append(units[i].toString()).append("\n");
        }

        return sb.toString().trim();
        
    }
}

