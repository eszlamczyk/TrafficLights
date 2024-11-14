package pl.ernest.basicLights;

import pl.ernest.model.ILight;
import pl.ernest.model.Road;
import pl.ernest.model.Vehicle;

import java.util.*;


//4 lights no pedestrians no arrows
//some assumptions:
// - cars require the same time to move though the intersection
// - there are no cool down phases where every light is red (everyone can instantly leave the crossing so no need to)
public class Lights {
    private final ILight northLight;
    private final ILight eastLight;
    private final ILight southLight;
    private final ILight westLight;

    private final Map<Road,List<Vehicle>> waitingVehicles;
    private final ArrayList<ArrayList<Vehicle>> stepStatuses;

    private int turnsInCycleLeft;

    public Lights(ILight northLight, ILight eastLight, ILight southLight, ILight westLight) {
        this.northLight = northLight;
        this.eastLight = eastLight;
        this.southLight = southLight;
        this.westLight = westLight;
        this.stepStatuses = new ArrayList<>();
        waitingVehicles = new HashMap<>();
        this.turnsInCycleLeft = 1;
    }

    //baisic approach - greenPriority * some constant / allPriority
    private int calculateTurns(){
        int greenPriority = northLight.getGreenPriority() + eastLight.getGreenPriority() +
                southLight.getGreenPriority() + westLight.getGreenPriority();
        int allPriority =  northLight.getSumPriority() + eastLight.getSumPriority() +
                southLight.getSumPriority() + westLight.getSumPriority();

        if (allPriority == 0) return 0;

        return greenPriority * 10 / allPriority;
    }

    private boolean canExit(Road road){
        if (!waitingVehicles.containsKey(road)){
            return false;
        }
        //right and straight - can go no matter what
        if (waitingVehicles.get(road).getFirst().endRoad() == road.getRightTurn() ||
                waitingVehicles.get(road).getFirst().endRoad() == road.getStraight()){
            return true;
        } else {
            //left turn - check if opposite side is colliding
            return waitingVehicles.get(road.getStraight()).isEmpty() ||
                    waitingVehicles.get(road.getStraight()).getFirst().endRoad() == road.getRightTurn();
        }
    }


    public void stepSimulation(){
        if (turnsInCycleLeft <= 0){
            northLight.nextCycle();
            eastLight.nextCycle();
            southLight.nextCycle();
            westLight.nextCycle();
            this.turnsInCycleLeft = calculateTurns();
        }

        ArrayList<Vehicle> leftVehicles = new ArrayList<>();

        if (!waitingVehicles.containsKey(Road.north)) {
            List<Vehicle> northVehicles = this.northLight.greenCycle();
            if (!northVehicles.isEmpty()) {
                waitingVehicles.put(Road.north, northVehicles);
            }
        }

        if (!waitingVehicles.containsKey(Road.east)) {
            List<Vehicle> eastVehicles = this.eastLight.greenCycle();
            if (!eastVehicles.isEmpty()) {
                waitingVehicles.put(Road.east, eastVehicles);
            }
        }

        if (!waitingVehicles.containsKey(Road.south)) {
            List<Vehicle> southVehicles = this.southLight.greenCycle();
            if (!southVehicles.isEmpty()) {
                waitingVehicles.put(Road.south, southVehicles);
            }
        }

        if (!waitingVehicles.containsKey(Road.west)) {
            List<Vehicle> westVehicles = this.westLight.greenCycle();
            if (!westVehicles.isEmpty()) {
                waitingVehicles.put(Road.west, westVehicles);
            }
        }

        //try exiting
        if (canExit(Road.north)){
            leftVehicles.add(waitingVehicles.get(Road.north).getFirst());
            waitingVehicles.remove(Road.north);
        }
        if (canExit(Road.east)){
            leftVehicles.add(waitingVehicles.get(Road.east).getFirst());
            waitingVehicles.remove(Road.east);
        }
        if (canExit(Road.south)){
            leftVehicles.add(waitingVehicles.get(Road.south).getFirst());
            waitingVehicles.remove(Road.south);
        }
        if (canExit(Road.west)){
            leftVehicles.add(waitingVehicles.get(Road.west).getFirst());
            waitingVehicles.remove(Road.west);
        }

        this.turnsInCycleLeft--;

        stepStatuses.add(leftVehicles);
    }

    public ArrayList<ArrayList<Vehicle>> getStepStatuses(){
        return this.stepStatuses;
    }

    public void addVehicle(Road startRoad, Vehicle vehicle){
        switch (startRoad){
            case north -> northLight.addVehicle(vehicle);
            case east -> eastLight.addVehicle(vehicle);
            case south -> southLight.addVehicle(vehicle);
            case west -> westLight.addVehicle(vehicle);
        }
    }
}
