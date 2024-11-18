package pl.ernest.model;

import pl.ernest.model.fancyLights.LaneTurn;

import java.util.Queue;

public class Lane {

    private final LaneTurn turn;
    private final Queue<IndicatorLight> nextLights;
    private IndicatorLight light;
    private final Queue<IVehicle> laneQueue;
    private final boolean isTrafficArrow;

    private final boolean isBusLane;

    public Lane(LaneTurn turn, Queue<IndicatorLight> nextLights, Queue<IVehicle> laneQueue, boolean isTrafficArrow, boolean isBusLane) {
        this.turn = turn;
        this.nextLights = nextLights;
        this.light = nextLights.remove();
        this.laneQueue = laneQueue;
        this.isTrafficArrow = isTrafficArrow;
        this.isBusLane = isBusLane;
    }

    public Lane(LaneTurn turn, Queue<IndicatorLight> nextLights, Queue<IVehicle> laneQueue) {
        this.turn = turn;
        this.nextLights = nextLights;
        this.light = nextLights.remove();
        this.laneQueue = laneQueue;
        this.isTrafficArrow = false;
        this.isBusLane = false;
    }

    public boolean vehiclesInQueue(){
        return !laneQueue.isEmpty();
    }

    public boolean isTrafficArrow(){
        return isTrafficArrow;
    }

    public boolean isBusLane(){
        return isBusLane;
    }

    public int getAmountVehiclesInQueue(){
        return laneQueue.size();
    }

    public void addVehicle(IVehicle vehicle){
        laneQueue.add(vehicle);
    }

    public IVehicle getNextVehicle(){
        return laneQueue.remove();
    }

    public LaneTurn getTurn(){
        return turn;
    }

    public IndicatorLight getLight() {
        return light;
    }

    public Queue<IndicatorLight> getNextLights() {
        return nextLights;
    }

    public void nextLight(){
        nextLights.add(this.light);
        this.light = nextLights.remove();
    }

    public int getSumPriority(){
        int sum = 0;
        for (IVehicle vehicle : laneQueue) {
            sum += vehicle.priority();
        }
        return sum;
    }
}
