package pl.ernest.model;

public record Pedestrian(String id, Road road) {

    @Override
    public String toString() {
        return id;
    }
}
