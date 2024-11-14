package pl.ernest.command;

import pl.ernest.basicLights.Lights;

public class StepCommand implements ICommand{

    private final Lights lights;

    public StepCommand(Lights lights) {
        this.lights = lights;
    }

    @Override
    public void execute() {
        System.out.println("executing Step");
        lights.stepSimulation();
    }
}
