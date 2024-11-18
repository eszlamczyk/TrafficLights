package pl.ernest.model;

public record Bus(String id, Road endRoad, int priority) implements IVehicle{

    public Bus(String id, Road endRoad) {
        this(id, endRoad, 1);
    }

    @Override
    public String toString() {
        return id;
    }

}
