package pl.ernest.model;

import java.util.List;
import java.util.Optional;

public interface ILight {

    void nextCycle();

    List<Optional<IVehicle>> moveCarsIntoIntersection();

    Optional<IVehicle> moveCarIntoIntersectionFromLane(int laneNumber);

    int getSumPriority();

    int getGreenPriority();

    void addVehicle(IVehicle vehicle);

    int getAmountOfLanes();

    void addPedestrian(Pedestrian pedestrian);

    List<Pedestrian> pedestriansCrossing();

    boolean isBlockedByPedestrians();

    List<Lane> getLanesList();

    int getLightCycleSize();

    void forcePedestrianLightRed();

}
