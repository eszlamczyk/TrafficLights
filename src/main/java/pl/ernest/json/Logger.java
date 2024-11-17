package pl.ernest.json;

import pl.ernest.model.ILight;
import pl.ernest.model.Pedestrian;
import pl.ernest.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    protected static Logger logger;

    private final List<IStepStatus> stepStatuses;


    private boolean fancyLoggerMode = false;

    private FancyStepStatus currentFancyStep;

    private BasicStepStatus currentBasicStep;

    private Logger() {
        this.stepStatuses = new ArrayList<>();
        currentFancyStep = new FancyStepStatus();
        currentBasicStep = new BasicStepStatus();
    }

    public void startFancyLogging(){
        fancyLoggerMode = true;
    }

    public void startBasicLogging(){
        fancyLoggerMode = false;
    }

    public static Logger getInstance(){
        if(logger == null){
            logger = new Logger();
        }
        return logger;
    }

    public void logLeftCar(Vehicle vehicle){
        currentBasicStep.leftVehicles.add(vehicle.toString());
        currentFancyStep.leftVehicles.add(vehicle.toString());
    }

    public void logNewCycle(ILight light){
        currentFancyStep.newLightCycle.add(light.toString());
    }

    public void logPedestrians(List<Pedestrian> pedestrians){
        currentFancyStep.leftPedestrians.addAll(pedestrians.stream().map(Pedestrian::toString).toList());
    }

    public void addStep(){
        if(fancyLoggerMode){
            stepStatuses.add(currentFancyStep);

        }else{
            stepStatuses.add(currentBasicStep);
        }

        currentFancyStep = new FancyStepStatus();
        currentBasicStep = new BasicStepStatus();
    }

    public List<IStepStatus> getStepStatuses(){
        return stepStatuses;
    }

    public void clearLogs(){
        stepStatuses.clear();
    }

    public FancyStepStatus getCurrentFancyStep() {
        return currentFancyStep;
    }

    public BasicStepStatus getCurrentBasicStep() {
        return currentBasicStep;
    }
}
