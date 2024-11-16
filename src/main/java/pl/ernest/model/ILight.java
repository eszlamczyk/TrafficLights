package pl.ernest.model;

import java.util.List;
import java.util.Optional;

public interface ILight {

    void nextCycle();

    List<Optional<Vehicle>> moveCarsIntoIntersection();

    Optional<Vehicle> moveCarIntoIntersectionFromLane(int laneNumber);

    int getSumPriority();

    int getGreenPriority();

    void addVehicle(Vehicle vehicle);

    int getAmountOfLanes();

    void addPedestrian(Pedestrian pedestrian);

    List<Pedestrian> pedestriansCrossing();

    boolean isBlockedByPedestrians();

    List<Lane> getLanesList();

}
