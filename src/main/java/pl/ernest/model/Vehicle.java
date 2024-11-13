package pl.ernest.model;


public class Vehicle{

    private final String id;
    private final Road endRoad;
    private final int priority; //for trams buses etc.

    public Vehicle(String id, Road endRoad, int priority){
        this.id = id;
        this.endRoad = endRoad;
        this.priority = priority;
    }
    //default priority
    public Vehicle(String id, Road endRoad){
        this.id = id;
        this.endRoad = endRoad;
        this.priority = 1;
    }

    public String getId() {
        return id;
    }

    public Road getEndRoad() {
        return endRoad;
    }

    public int getPriority() {
        return priority;
    }
}
