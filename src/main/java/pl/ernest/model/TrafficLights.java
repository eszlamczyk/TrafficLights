package pl.ernest.model;

import java.util.*;

public class TrafficLights {
//    private final ILight northLight;
//    private final ILight eastLight;
//    private final ILight southLight;
//    private final ILight westLight;

    //done just for code tidiness
    private final Map<Road, ILight> lightMap;
    private final Map<Road,List<Optional<Vehicle>>> waitingVehicles;
    private final ArrayList<ArrayList<Vehicle>> stepStatuses;
    private int turnsInCycleLeft;
    private final int priorityConstant;

    public TrafficLights(ILight northLight, ILight eastLight, ILight southLight, ILight westLight, int priorityConstant) {
        lightMap = new HashMap<>();
        lightMap.put(Road.north, northLight);
        lightMap.put(Road.east, eastLight);
        lightMap.put(Road.south, southLight);
        lightMap.put(Road.west, westLight);

        this.priorityConstant = priorityConstant;

        this.stepStatuses = new ArrayList<>();
        waitingVehicles = new HashMap<>();
        this.turnsInCycleLeft = 1;
    }

    //basic approach - greenPriority * some constant / allPriority
    private int calculateTurns(){

        int greenPriority = 0;
        for (ILight light : lightMap.values()){
            greenPriority += light.getGreenPriority();
        }

        int allPriority = 0;

        for (ILight light : lightMap.values()){
            allPriority += light.getSumPriority();
        }

        if (allPriority == 0) return 0;

        return greenPriority * this.priorityConstant / allPriority;
    }

    private List<Boolean> canExit(Road road){
        List<Boolean> canExitList = new ArrayList<>();
        if (!waitingVehicles.containsKey(road)){
            return canExitList;
        }
        for (Optional<Vehicle> optionalVehicle : waitingVehicles.get(road)){
            //no vehicle at this lane
            if (optionalVehicle.isEmpty()){
                canExitList.add(false);
            }else {
                Vehicle vehicle = optionalVehicle.get();
                //pedestrians check
                if(lightMap.get(vehicle.endRoad()).isBlocked()){
                    canExitList.add(false);
                    continue;
                }
                //right and straight - can go there no matter what
                if (vehicle.endRoad() == road.getRoadOnTheRight() ||
                        vehicle.endRoad() == road.getOppositeRoad()) {
                    canExitList.add(true);
                    continue;
                }
                //left turn - check if ANY of opposite side are colliding
                boolean exitPossible = true;
                for (Optional<Vehicle> optionalOppositeVehicle : waitingVehicles.get(road.getOppositeRoad())){
                    if (optionalOppositeVehicle.isPresent() &&
                            (optionalVehicle.get().endRoad() == road ||
                                    optionalOppositeVehicle.get().endRoad() == road.getRoadOnTheLeft())){
                        exitPossible = false;
                    }

                canExitList.add(exitPossible);
            }
            }
        }
        return canExitList;
    }


    public void stepSimulation(){
        if (turnsInCycleLeft <= 0){
            lightMap.forEach((road, iLight) -> iLight.nextCycle());
            this.turnsInCycleLeft = calculateTurns();
        }

        ArrayList<Vehicle> leftVehicles = new ArrayList<>();

        lightMap.forEach((road, iLight) -> {
            if(!waitingVehicles.containsKey(road)){
                List<Optional<Vehicle>> potentialVehicles = iLight.greenCars();
                waitingVehicles.put(road, potentialVehicles);
            }else{
                for (int i = 0; i < iLight.getAmountOfLanes(); i++) {
                    if (waitingVehicles.get(road).get(i).isEmpty()){
                        waitingVehicles.get(road).set(i,iLight.greenCarFromIndex(i));
                    }
                }
            }
        });
        //I could do here the same forEach method, but I need to take into consideration everything at the time
        //that's why I used 4 flags
        List<Boolean> canExitNorth = canExit(Road.north);
        List<Boolean> canExitEast = canExit(Road.east);
        List<Boolean> canExitSouth = canExit(Road.south);
        List<Boolean> canExitWest = canExit(Road.west);

        tryExiting(Road.north, canExitNorth, leftVehicles);
        tryExiting(Road.east, canExitEast, leftVehicles);
        tryExiting(Road.south, canExitSouth, leftVehicles);
        tryExiting(Road.west, canExitWest, leftVehicles);

        this.turnsInCycleLeft--;

        stepStatuses.add(leftVehicles);
    }

    private void tryExiting(Road road, List<Boolean> canExitList, ArrayList<Vehicle> leftVehicles){
        for (int i = 0; i < lightMap.get(road).getAmountOfLanes(); i++) {
            if (canExitList.get(i)){
                leftVehicles.add(waitingVehicles.get(road).get(i).get());
                waitingVehicles.get(road).set(i,Optional.empty());
            }
        }
    }

    public ArrayList<ArrayList<Vehicle>> getStepStatuses(){
        return this.stepStatuses;
    }

    public void addVehicle(Road startRoad, Vehicle vehicle){
        this.lightMap.get(startRoad).addVehicle(vehicle);
    }
}
