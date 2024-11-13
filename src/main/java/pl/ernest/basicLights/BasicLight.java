package pl.ernest.basicLights;

import pl.ernest.model.ILight;
import pl.ernest.model.TrafficCycle;
import pl.ernest.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

//basic light - 1 queue, for every turn
public class BasicLight implements ILight {

    private final Queue<Vehicle> carsQueue;

    private TrafficCycle trafficCycle;

    public BasicLight(Queue<Vehicle> carsQueue, TrafficCycle trafficCycle) {
        this.carsQueue = carsQueue;
        this.trafficCycle = trafficCycle;
    }

    @Override
    public void nextCycle(){
        this.trafficCycle = trafficCycle.next();
    }

    @Override
    public List<Vehicle> greenCycle() {
        ArrayList<Vehicle> result = new ArrayList<>();
        if (!carsQueue.isEmpty() && trafficCycle == TrafficCycle.Green){
            result.add(carsQueue.remove());
        }
        return result;
    }

    @Override
    public int getSumPriority(){
        int sum = 0;
        for (Vehicle vehicle : carsQueue) {
            sum += vehicle.priority();
        }
        return sum;
    }

    @Override
    public int getGreenPriority(){
        if(trafficCycle == TrafficCycle.Green){
            return getSumPriority();
        }
        return 0;
    }

    @Override
    public void addVehicle(Vehicle vehicle){
        carsQueue.add(vehicle);
    }

    public Queue<Vehicle> getCarsQueue() {
        return carsQueue;
    }

    public TrafficCycle getTrafficCycle() {
        return trafficCycle;
    }
}
