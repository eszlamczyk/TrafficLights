package pl.ernest.command;

import pl.ernest.model.TrafficLights;

public class StepCommand implements ICommand{

    private final TrafficLights trafficLights;

    public StepCommand(TrafficLights trafficLights) {
        this.trafficLights = trafficLights;
    }

    @Override
    public void execute() {
        //System.out.println("executing Step");
        trafficLights.stepSimulation();
    }
}
