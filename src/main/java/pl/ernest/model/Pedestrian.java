package pl.ernest.model;

public record Pedestrian(String id, Road endRoad) {

    @Override
    public String toString() {
        return id;
    }
}
