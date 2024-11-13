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

    public Lights(ILight northLight, ILight eastLight, ILight southLight, ILight westLight) {
        this.northLight = northLight;
        this.eastLight = eastLight;
        this.southLight = southLight;
        this.westLight = westLight;
        waitingVehicles = new HashMap<>();
    }

    //baisic approach - greenPriority * some constant / allPriority
    private int calculateTurns(){
        int greenPriority = northLight.getGreenPriority() + eastLight.getGreenPriority() +
                southLight.getGreenPriority() + westLight.getGreenPriority();
        int allPriority =  northLight.getSumPriority() + eastLight.getSumPriority() +
                southLight.getSumPriority() + westLight.getSumPriority();
        return greenPriority * 10 / allPriority;
    }

    private boolean canExit(Road road){
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


    public List<Vehicle> stepSimulation(){
        int numberOfTurns = calculateTurns();
        ArrayList<Vehicle> leftVehicles = new ArrayList<>();

        for (int i = 0; i < numberOfTurns; i++){
            //get new cars
            if (waitingVehicles.containsKey(Road.north)){
                waitingVehicles.put(Road.north, this.northLight.greenCycle());
            }
            if (waitingVehicles.containsKey(Road.east)){
                waitingVehicles.put(Road.east, this.eastLight.greenCycle());
            }
            if (waitingVehicles.containsKey(Road.south)){
                waitingVehicles.put(Road.south, this.southLight.greenCycle());
            }
            if (waitingVehicles.containsKey(Road.west)){
                waitingVehicles.put(Road.west, this.westLight.greenCycle());
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
        }
        return leftVehicles;
    }




}
