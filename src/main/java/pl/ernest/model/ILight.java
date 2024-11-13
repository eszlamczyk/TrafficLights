package pl.ernest.model;

import java.util.Optional;

public interface ILight {

    void nextCycle();

    Optional<Vehicle> GreenCycle();

    int getSumPriority();

    int getGreenPriority();

    void addVehicle(Vehicle vehicle);

}
