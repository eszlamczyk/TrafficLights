package pl.ernest.basicLights;

import pl.ernest.model.ILight;
import pl.ernest.model.Road;
import pl.ernest.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//4 lights no pedestrians no arrows
//some assumptions:
// - cars require the same time to move though the intersection
// - there are no cool down phases where every light is red (everyone can instantly leave the crossing so no need to)
public class Lights {

    private final ILight northLight;
    private final ILight eastLight;
    private final ILight southLight;
    private final ILight westLight;

    private Optional<Vehicle> northVehicle = Optional.empty();
    private Optional<Vehicle> eastVehicle = Optional.empty();
    private Optional<Vehicle> southVehicle = Optional.empty();
    private Optional<Vehicle> westVehicle = Optional.empty();

    public Lights(ILight northLight, ILight eastLight, ILight southLight, ILight westLight) {
        this.northLight = northLight;
        this.eastLight = eastLight;
        this.southLight = southLight;
        this.westLight = westLight;
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
        switch (road){
            case north -> {
                if (this.northVehicle.isEmpty()){
                    return false;
                }else{
                    if (this.northVehicle.get().getEndRoad() == Road.north.getRightTurn() ||
                            this.northVehicle.get().getEndRoad() == Road.north.getStraight()){
                        return true;
                    }
                }
            }
        }
    }


    public List<Vehicle> stepSimulation(){
        int numberOfTurns = calculateTurns();
        ArrayList<Vehicle> leftVehicles = new ArrayList<>();

        for (int i = 0; i < numberOfTurns; i++){
            //get new cars
            if (northVehicle.isEmpty()){
                northVehicle = this.northLight.GreenCycle();
            }
            if (eastVehicle.isEmpty()){
                eastVehicle = this.eastLight.GreenCycle();
            }
            if (southVehicle.isEmpty()){
                southVehicle = this.southLight.GreenCycle();
            }
            if (westVehicle.isEmpty()){
                westVehicle = this.westLight.GreenCycle();
            }



        }
    }




}
