package pl.ernest.model;

import java.util.List;
import java.util.Optional;

public interface ILight {

    void nextCycle();

    List<Optional<Vehicle>> greenCars();

    Optional<Vehicle> greenCarFromIndex(int index);

    int getSumPriority();

    int getGreenPriority();

    void addVehicle(Vehicle vehicle);

    int getAmountOfLanes();

    boolean isBlocked();

}
