package pl.ernest.json;

import pl.ernest.model.ILight;
import pl.ernest.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    protected static Logger logger;

    private final List<StepStatus> stepStatuses;

    private StepStatus currentStep;

    private Logger() {
        this.stepStatuses = new ArrayList<>();
        currentStep = new StepStatus();
    }

    public static Logger getInstance(){
        if(logger == null){
            logger = new Logger();
        }
        return logger;
    }

    public void logLeftCar(Vehicle vehicle){
        currentStep.leftVehicles.add(vehicle.toString());
    }

    public void logNewCycle(ILight light){
        currentStep.newLightCycle.add(light.toString());
    }

    public void addStep(){
        stepStatuses.add(currentStep);
        currentStep = new StepStatus();
    }

    public List<StepStatus> getStepStatuses(){
        return stepStatuses;
    }

}
