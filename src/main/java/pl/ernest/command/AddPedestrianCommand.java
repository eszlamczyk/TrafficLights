package pl.ernest.command;

import pl.ernest.model.Pedestrian;
import pl.ernest.model.Road;
import pl.ernest.model.TrafficLights;
import pl.ernest.model.Vehicle;

public class AddPedestrianCommand implements ICommand{
    private final Pedestrian pedestrian;

    private final TrafficLights trafficLights;

    private final Road startRoad;

    public AddPedestrianCommand(Pedestrian pedestrian, TrafficLights trafficLights, Road startRoad) {
        this.pedestrian = pedestrian;
        this.trafficLights = trafficLights;
        this.startRoad = startRoad;
    }

    @Override
    public void execute() {
        this.trafficLights.addPedestrian(startRoad, pedestrian);
    }
}
