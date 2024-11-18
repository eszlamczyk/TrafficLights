package pl.ernest.model.basicLights;

import pl.ernest.json.Logger;
import pl.ernest.model.*;
import pl.ernest.model.Lane;
import pl.ernest.model.fancyLights.LaneTurn;

import java.util.*;

public class BasicLight implements ILight {

    private final Logger logger = Logger.getInstance();

    private final Lane lane;

    private final int lightPriority;

    private final Road startDirection;

    public BasicLight(Queue<IVehicle> carsQueue, IndicatorLight indicatorLight, int lightPriority, Road startDirection) {
        Queue<IndicatorLight> indicatorLightQueue = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            indicatorLightQueue.add(indicatorLight);
            indicatorLight = indicatorLight.next();
        }
        this.lane = new Lane(LaneTurn.LeftStraightRight,indicatorLightQueue,carsQueue);
        this.lightPriority = lightPriority;
        this.startDirection = startDirection;
    }

    public BasicLight(Queue<IVehicle> carsQueue, IndicatorLight indicatorLight, Road startDirection) {
        this(carsQueue, indicatorLight,1, startDirection);
    }

    @Override
    public void nextCycle(){
        this.lane.nextLight();
        this.logger.logNewCycle(this);
    }

    @Override
    public List<Optional<IVehicle>> moveCarsIntoIntersection() {
        ArrayList<Optional<IVehicle>> result = new ArrayList<>();
        if (lane.vehiclesInQueue() && lane.getLight() == IndicatorLight.Green){
            result.add(Optional.of(lane.getNextVehicle()));
        }else {
            result.add(Optional.empty());
        }
        return result;
    }

    public Optional<IVehicle> moveCarIntoIntersectionFromLane(int laneNumber){
        if (laneNumber == 0){
            return moveCarsIntoIntersection().getFirst();
        }
        return Optional.empty();
    }


    @Override
    public int getSumPriority(){
        return lane.getSumPriority() * lightPriority;
    }

    @Override
    public int getGreenPriority(){
        if(lane.getLight() == IndicatorLight.Green){
            return getSumPriority();
        }
        return 0;
    }

    @Override
    public void addVehicle(IVehicle vehicle){
        lane.addVehicle(vehicle);
    }

    @Override
    public int getAmountOfLanes(){
        return 1;
    }

    @Override
    public boolean isBlockedByPedestrians() {
        //no pedestrians - no blockage
        return false;
    }

    @Override
    public List<Lane> getLanesList() {
        return List.of(lane);
    }

    @Override
    public int getLightCycleSize() {
        return lane.getNextLights().size() + 1;
    }

    @Override
    public void addPedestrian(Pedestrian pedestrian) {}

    @Override
    public List<Pedestrian> pedestriansCrossing() {
        return new ArrayList<>();
    }

    @Override
    public void forcePedestrianLightRed() {}

    public int getCarsQueueSize() {
        return lane.getAmountVehiclesInQueue();
    }

    public IndicatorLight getCurrentLight() {
        return lane.getLight();
    }

    @Override
    public String toString(){

        return startDirection.toString() + " road; [" + lane.getLight().toString() + "]";
    }
}
