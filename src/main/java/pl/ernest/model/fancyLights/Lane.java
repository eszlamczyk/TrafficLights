package pl.ernest.model.fancyLights;

import pl.ernest.model.IndicatorLight;
import pl.ernest.model.Vehicle;

import java.util.Queue;

public class Lane {

    private final LaneTurn turn;
    private final Queue<IndicatorLight> nextLights;
    private IndicatorLight light;
    private final Queue<Vehicle> laneQueue;

    public Lane(LaneTurn turn, Queue<IndicatorLight> nextLights, Queue<Vehicle> laneQueue) {
        this.turn = turn;
        this.nextLights = nextLights;
        this.light = nextLights.remove();
        this.laneQueue = laneQueue;
    }

    public boolean vehiclesInQueue(){
        return !laneQueue.isEmpty();
    }
    public void addVehicle(Vehicle vehicle){
        laneQueue.add(vehicle);
    }

    public Vehicle getVehicle(){
        return laneQueue.remove();
    }
    public LaneTurn getTurn(){
        return turn;
    }
    public int getLaneQueueSize(){
        return laneQueue.size();
    }

    public IndicatorLight getLight() {
        return light;
    }

    public Queue<IndicatorLight> getNextLights() {
        return nextLights;
    }

    public void addToLightQueue(IndicatorLight newLight){
        nextLights.add(newLight);
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
