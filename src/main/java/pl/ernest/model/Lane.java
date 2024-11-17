package pl.ernest.model;

import pl.ernest.model.fancyLights.LaneTurn;

import java.util.Queue;

public class Lane {

    private final LaneTurn turn;
    private final Queue<IndicatorLight> nextLights;
    private IndicatorLight light;
    private final Queue<Vehicle> laneQueue;
    private final boolean isTrafficArrow;

    public Lane(LaneTurn turn, Queue<IndicatorLight> nextLights, Queue<Vehicle> laneQueue, boolean isTrafficArrow) {
        this.turn = turn;
        this.nextLights = nextLights;
        this.light = nextLights.remove();
        this.laneQueue = laneQueue;
        this.isTrafficArrow = isTrafficArrow;
    }

    public Lane(LaneTurn turn, Queue<IndicatorLight> nextLights, Queue<Vehicle> laneQueue) {
        this.turn = turn;
        this.nextLights = nextLights;
        this.light = nextLights.remove();
        this.laneQueue = laneQueue;
        this.isTrafficArrow = false;
    }

    public boolean vehiclesInQueue(){
        return !laneQueue.isEmpty();
    }

    public boolean isTrafficArrow(){
        return isTrafficArrow;
    }

    public int getAmountVehiclesInQueue(){
        return laneQueue.size();
    }

    public void addVehicle(Vehicle vehicle){
        laneQueue.add(vehicle);
    }

    public Vehicle getNextVehicle(){
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
        for (Vehicle vehicle : laneQueue) {
            sum += vehicle.priority();
        }
        return sum;
    }
}
