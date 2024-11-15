package pl.ernest.command;

import pl.ernest.model.TrafficLights;
import pl.ernest.model.Road;
import pl.ernest.model.Vehicle;

public class AddVehicleCommand implements ICommand{

    private final Vehicle Vehicle;

    private final TrafficLights trafficLights;

    private final Road startRoad;

    public AddVehicleCommand(Vehicle vehicle, TrafficLights trafficLights, Road startRoad) {
        Vehicle = vehicle;
        this.trafficLights = trafficLights;
        this.startRoad = startRoad;
    }

    @Override
    public void execute() {
        //System.out.println("Executing Add");
        this.trafficLights.addVehicle(startRoad, Vehicle);
    }
}
