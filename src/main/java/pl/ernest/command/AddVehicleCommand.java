package pl.ernest.command;

import pl.ernest.basicLights.Lights;
import pl.ernest.model.Road;
import pl.ernest.model.Vehicle;

public class AddVehicleCommand implements ICommand{

    private final Vehicle Vehicle;

    private final Lights lights;

    private final Road startRoad;

    public AddVehicleCommand(Vehicle vehicle, Lights lights, Road startRoad) {
        Vehicle = vehicle;
        this.lights = lights;
        this.startRoad = startRoad;
    }

    @Override
    public void execute() {
        System.out.println("Executing Add");
        this.lights.addVehicle(startRoad, Vehicle);
    }
}
