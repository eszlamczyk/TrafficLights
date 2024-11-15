package pl.ernest.model;

public record Vehicle(String id, Road endRoad, int priority) {
    public Vehicle(String id, Road endRoad) {
        this(id, endRoad, 1);
    }

    @Override
    public String toString() {
        return id;
    }
}