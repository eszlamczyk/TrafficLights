package pl.ernest.model;

import java.util.List;

public interface ILight {

    void nextCycle();

    List<Vehicle> greenCycle();

    int getSumPriority();

    int getGreenPriority();

    void addVehicle(Vehicle vehicle);

}
