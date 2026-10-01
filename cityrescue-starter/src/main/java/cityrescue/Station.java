package cityrescue;
public class Station{
    private int stationId;
    private String name;
    private int x;
    private int y;

    private int capacity = 50;
    private Unit[] units;
    private int unitCount;

    public Station(int stationID, String name, int x, int y){
        this.stationId =stationID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = capacity;

        this.units = new Unit[capacity];
        this.unitCount = 0;
    }
    //getters, nothing else should be able to be changed
    public int getUnitCount() { return unitCount; }
    public Unit[] getUnits() {return units; }
    public int getCapacity(){return capacity;}
    public int getStationx(){return x;}
    public int getStationy(){return y;}
    public int getId() { return stationId; }

    public void setCapacity(int units){
        this.capacity = units;
    }
    public void SetUnits(Unit newUnit){
        units[unitCount]= newUnit;
        unitCount++;
    }
    public void removeUnit(int unitId){
        for(int i=0;i<=unitCount;i++){
            if(units[i]!=null && units[i].getId()==unitId){
                units[i]=null;
                for(int j=i; j<unitCount;j++){
                    units[j]=units[j+1];
                }
                units[unitCount]=null;
                unitCount--;
                return;

            }

        }
    }
 }




