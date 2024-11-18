package pl.ernest.model;

import pl.ernest.json.Logger;
import pl.ernest.model.fancyLights.LaneTurn;
import pl.ernest.strategy.AbstractTurnsStrategy;

import java.util.*;

public class TrafficLights {
    private final Logger logger = Logger.getInstance();
    private final AbstractTurnsStrategy turnsStrategy;
    private final Map<Road, ILight> lightMap;
    private final Map<Road,List<Optional<IVehicle>>> waitingVehicles;
    private int turnsInCycleLeft;

    public TrafficLights(ILight northLight, ILight eastLight, ILight southLight, ILight westLight,
                         AbstractTurnsStrategy turnsStrategy)
            throws CollidingLightConfigurationException {

        lightMap = new HashMap<>();
        lightMap.put(Road.north, northLight);
        lightMap.put(Road.east, eastLight);
        lightMap.put(Road.south, southLight);
        lightMap.put(Road.west, westLight);

        this.turnsStrategy = turnsStrategy;

        this.waitingVehicles = new HashMap<>();
        this.turnsInCycleLeft = turnsStrategy.calculateTurns(lightMap.values());
        checkPossibleCollision();
    }

    private void checkPossibleCollision() throws CollidingLightConfigurationException {
        // Ensure every lane is checked during each light cycle iteration.
        for (int i = 0; i < lightMap.get(Road.north).getLightCycleSize() - 1; i++) {
            for (var entry : lightMap.entrySet()) {
                processRoad(entry.getKey(), entry.getValue());
            }
            lightMap.forEach((road, iLight) -> iLight.nextCycle());
        }
    }
    private void processRoad(Road roadDirection, ILight light) throws CollidingLightConfigurationException {
        for (Lane lane : light.getLanesList()) {
            if(lane.getLight() == IndicatorLight.Green) {
                handleCollision(lane, roadDirection);
            }
        }
    }

    private static final Set<LaneTurn> EMPTY_COLLIDERS = Set.of();
    private static final Set<LaneTurn> STRAIGHT_COLLIDERS = Set.of(
            LaneTurn.Straight, LaneTurn.StraightRight, LaneTurn.LeftStraight, LaneTurn.LeftStraightRight
    );
    private static final Set<LaneTurn> STRAIGHT_LEFT_COLLIDERS = Set.of(
            LaneTurn.Straight, LaneTurn.StraightRight, LaneTurn.LeftStraight, LaneTurn.LeftStraightRight, LaneTurn.Left
    );
    private static final Set<LaneTurn> RIGHT_COLLIDERS = Set.of(
            LaneTurn.LeftStraight, LaneTurn.Straight, LaneTurn.StraightRight, LaneTurn.LeftStraightRight
    );
    private static final Set<LaneTurn> LEFT_COLLIDERS = Set.of(
            LaneTurn.Left, LaneTurn.LeftStraight, LaneTurn.LeftStraightRight, LaneTurn.UTurn, LaneTurn.Straight,
            LaneTurn.StraightRight
    );
    private static final Set<LaneTurn> FULL_COLLIDERS = Set.of(
            LaneTurn.UTurn, LaneTurn.Left, LaneTurn.LeftStraight, LaneTurn.LeftStraightRight, LaneTurn.Straight,
            LaneTurn.StraightRight, LaneTurn.Right
    );


    private void handleCollision(Lane lane, Road roadDirection) throws CollidingLightConfigurationException {
        switch (lane.getTurn()) {
            case Straight, StraightRight ->
                    checkPossibleCollision(roadDirection, STRAIGHT_LEFT_COLLIDERS, FULL_COLLIDERS);
            case Right ->
                    checkPossibleCollision(roadDirection,STRAIGHT_COLLIDERS, EMPTY_COLLIDERS);
            case Left, UTurn ->
                    checkPossibleCollision(roadDirection, lane.isTrafficArrow());
            case LeftStraightRight -> {
                checkPossibleCollision(roadDirection, RIGHT_COLLIDERS);
                checkPossibleCollision(roadDirection, LEFT_COLLIDERS);
                checkPossibleCollision(roadDirection, lane.isTrafficArrow());
            }
            case LeftStraight -> {
                checkPossibleCollision(roadDirection, lane.isTrafficArrow());
                checkPossibleCollision(roadDirection, STRAIGHT_LEFT_COLLIDERS);
            }
        }
    }

    private void checkPossibleCollision(Road roadDirection,
                                        Set<LaneTurn> turnSet) throws CollidingLightConfigurationException {
        checkCollisionForRoad(roadDirection.getRoadOnTheLeft(), turnSet);
        checkCollisionForRoad(roadDirection.getRoadOnTheRight(), turnSet);
    }

    private void checkPossibleCollision(Road roadDirection, Set<LaneTurn> leftSet,
                                        Set<LaneTurn> rightSet) throws CollidingLightConfigurationException {
        checkCollisionForRoad(roadDirection.getRoadOnTheLeft(), leftSet);
        checkCollisionForRoad(roadDirection.getRoadOnTheRight(), rightSet);
    }

    private void checkPossibleCollision(Road roadDirection,
                                        boolean isArrow) throws CollidingLightConfigurationException {
        checkCollisionForRoad(roadDirection.getRoadOnTheLeft(), TrafficLights.LEFT_COLLIDERS);
        checkCollisionForRoad(roadDirection.getRoadOnTheRight(), TrafficLights.LEFT_COLLIDERS);

        if (isArrow) {
            checkCollisionForRoad(roadDirection.getOppositeRoad(), TrafficLights.FULL_COLLIDERS);
        }
    }

    private void checkCollisionForRoad(Road road, Set<LaneTurn> turnSet) throws CollidingLightConfigurationException {
        for (Lane lane : lightMap.get(road).getLanesList()) {
            if (turnSet.contains(lane.getTurn()) && lane.getLight() == IndicatorLight.Green) {
                throw new CollidingLightConfigurationException("There are colliding light configuration");
            }
        }
    }

    private List<Boolean> canExit(Road road){
        List<Boolean> canExitList = new ArrayList<>();
        if (!waitingVehicles.containsKey(road)){
            return canExitList;
        }
        for (Optional<IVehicle> optionalVehicle : waitingVehicles.get(road)){
            //no vehicle at this lane
            if (optionalVehicle.isEmpty()){
                canExitList.add(false);
            }else {
                IVehicle vehicle = optionalVehicle.get();
                //pedestrians check
                if(lightMap.get(vehicle.endRoad()).isBlockedByPedestrians()){
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
                for (Optional<IVehicle> optionalOppositeVehicle : waitingVehicles.get(road.getOppositeRoad())){
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
            this.turnsInCycleLeft = turnsStrategy.calculateTurns(lightMap.values());
        }

        lightMap.forEach((road, iLight) -> {
            if(!waitingVehicles.containsKey(road)){
                List<Optional<IVehicle>> potentialVehicles = iLight.moveCarsIntoIntersection();
                waitingVehicles.put(road, potentialVehicles);
            }else{
                for (int i = 0; i < iLight.getAmountOfLanes(); i++) {
                    if (waitingVehicles.get(road).get(i).isEmpty()){
                        waitingVehicles.get(road).set(i,iLight.moveCarIntoIntersectionFromLane(i));
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

        tryExiting(Road.north, canExitNorth);
        tryExiting(Road.east, canExitEast);
        tryExiting(Road.south, canExitSouth);
        tryExiting(Road.west, canExitWest);

        //pedestrians
        lightMap.forEach((road, iLight) -> {
            if (iLight.isBlockedByPedestrians()){
                logger.logPedestrians(iLight.pedestriansCrossing());
            }
        });

        this.turnsInCycleLeft--;
        logger.addStep();
    }

    private void tryExiting(Road road, List<Boolean> canExitList){
        for (int i = 0; i < lightMap.get(road).getAmountOfLanes(); i++) {
            if (canExitList.get(i) && waitingVehicles.get(road).get(i).isPresent()){
                logger.logLeftVehicle(waitingVehicles.get(road).get(i).get());
                waitingVehicles.get(road).set(i,Optional.empty());
            }
        }
    }


    public void addVehicle(Road startRoad, IVehicle vehicle){
        this.lightMap.get(startRoad).addVehicle(vehicle);
    }

    public void addPedestrian(Road road, Pedestrian pedestrian){
        this.lightMap.get(road).addPedestrian(pedestrian);
    }
}
