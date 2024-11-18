package pl.ernest.command;

import pl.ernest.model.IVehicle;
import pl.ernest.model.TrafficLights;
import pl.ernest.model.Road;

public class AddVehicleCommand implements ICommand{

    private final IVehicle vehicle;

    private final TrafficLights trafficLights;

    private final Road startRoad;

    public AddVehicleCommand(IVehicle vehicle, TrafficLights trafficLights, Road startRoad) {
        this.vehicle = vehicle;
        this.trafficLights = trafficLights;
        this.startRoad = startRoad;
    }

    @Override
    public void execute() {
        //System.out.println("Executing Add");
        this.trafficLights.addVehicle(startRoad, vehicle);
    }
}
