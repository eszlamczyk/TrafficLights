package pl.ernest.basicLights;

import pl.ernest.model.ILight;
import pl.ernest.model.IndicatorLight;
import pl.ernest.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

//basic light - 1 queue, for every turn
public class BasicLight implements ILight {

    private final Queue<Vehicle> carsQueue;

    private IndicatorLight indicatorLight;

    private final int lightPriority;

    public BasicLight(Queue<Vehicle> carsQueue, IndicatorLight indicatorLight, int lightPriority) {
        this.carsQueue = carsQueue;
        this.indicatorLight = indicatorLight;
        this.lightPriority = lightPriority;
    }

    public BasicLight(Queue<Vehicle> carsQueue, IndicatorLight indicatorLight) {
        this(carsQueue, indicatorLight,1);
    }

    @Override
    public void nextCycle(){
        this.indicatorLight = indicatorLight.next();
    }

    @Override
    public List<Optional<Vehicle>> greenCars() {
        ArrayList<Optional<Vehicle>> result = new ArrayList<>();
        if (!carsQueue.isEmpty() && indicatorLight == IndicatorLight.Green){
            result.add(Optional.of(carsQueue.remove()));
        }else {
            result.add(Optional.empty());
        }
        return result;
    }

    public Optional<Vehicle> greenCarFromIndex(int index){
        if (!carsQueue.isEmpty() && indicatorLight == IndicatorLight.Green && index == 0) {
            return Optional.of(carsQueue.remove());
        }
        return Optional.empty();
    }


    @Override
    public int getSumPriority(){
        int sum = 0;
        for (Vehicle vehicle : carsQueue) {
            sum += vehicle.priority();
        }
        return sum * lightPriority;
    }

    @Override
    public int getGreenPriority(){
        if(indicatorLight == IndicatorLight.Green){
            return getSumPriority();
        }
        return 0;
    }

    @Override
    public void addVehicle(Vehicle vehicle){
        carsQueue.add(vehicle);
    }

    @Override
    public int getAmountOfLanes(){
        return 1;
    }

    @Override
    public boolean isBlocked() {
        //no pedestrians - no blockage
        return false;
    }

    public Queue<Vehicle> getCarsQueue() {
        return carsQueue;
    }

    public IndicatorLight getTrafficCycle() {
        return indicatorLight;
    }
}
