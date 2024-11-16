package pl.ernest.model.fancyLights;

import pl.ernest.json.Logger;
import pl.ernest.model.*;

import java.util.*;

public class FancyLight implements ILight {

    private final Logger logger = Logger.getInstance();
    private final List<Lane> lanes;
    private final List<Pedestrian> pedestrians;
    private IndicatorLight pedestrianLight;
    private final int lightPriority;
    private final Road startDirection;

    public FancyLight(List<Lane> lanes, List<Pedestrian> pedestrians, int lightPriority, Road startDirection) throws IncorrectLaneArrangementException {
        this.lanes = lanes;
        this.pedestrians = pedestrians;
        this.lightPriority = lightPriority;
        this.startDirection = startDirection;
        checkLanePlacement();
        setPedestrianLight();
    }

    public FancyLight(List<Lane> lanes, Road startDirection) throws IncorrectLaneArrangementException {
        this(lanes,new ArrayList<>(),1,startDirection);
    }

    private void checkLanePlacement() throws IncorrectLaneArrangementException{
        Set<LaneTurn> necessaryLaneTurns = new HashSet<>();
        for (Lane lane : lanes){
            switch (lane.getTurn()){
                case UTurn -> necessaryLaneTurns.add(LaneTurn.UTurn);
                case Left -> {
                    necessaryLaneTurns.add(LaneTurn.UTurn);
                    necessaryLaneTurns.add(LaneTurn.Left);
                }
                case LeftStraight -> {
                    necessaryLaneTurns.add(LaneTurn.UTurn);
                    necessaryLaneTurns.add(LaneTurn.Left);
                    necessaryLaneTurns.add(LaneTurn.Straight);
                }
                case LeftStraightRight -> {
                    necessaryLaneTurns.add(LaneTurn.UTurn);
                    necessaryLaneTurns.add(LaneTurn.Left);
                    necessaryLaneTurns.add(LaneTurn.Straight);
                    necessaryLaneTurns.add(LaneTurn.Right);
                }
                case Straight -> necessaryLaneTurns.add(LaneTurn.Straight);
                case StraightRight -> {
                    necessaryLaneTurns.add(LaneTurn.Straight);
                    necessaryLaneTurns.add(LaneTurn.Right);
                }
                case Right -> necessaryLaneTurns.add(LaneTurn.Right);
            }
        }

        if (necessaryLaneTurns.size() < 4){
            throw new IncorrectLaneArrangementException("Every single direction is not covered");
        }

        // Check lane order and crashing paths
        // Perhaps you can do it in O(N) time, but it's would become very
        // unreadable because of LaneTurn.LeftStraightRight
        for (int i = 0; i < lanes.size(); i++) {
            for (int j = i + 1; j < lanes.size(); j++) {
                Lane lane1 = lanes.get(i);
                Lane lane2 = lanes.get(j);

                if (isConflicting(lane1.getTurn(), lane2.getTurn())) {
                    throw new IncorrectLaneArrangementException("Conflicting lane arrangement");
                }
            }
        }
    }

    private boolean isConflicting(LaneTurn turn1, LaneTurn turn2) {
        return switch (turn1) {
            case UTurn -> false;
            case Left -> (turn2 == LaneTurn.UTurn);
            case LeftStraight, Straight -> (turn2 == LaneTurn.UTurn || turn2 == LaneTurn.Left ||
                    turn2 == LaneTurn.LeftStraight || turn2 == LaneTurn.LeftStraightRight);
            case LeftStraightRight, StraightRight, Right -> (turn2 != LaneTurn.Right);
        };
    }

    @Override
    public void nextCycle() {
        for(Lane lane : lanes){
            lane.nextLight();
        }
        setPedestrianLight();
        this.logger.logNewCycle(this);
    }

    private void setPedestrianLight(){
        pedestrianLight = IndicatorLight.Green;
        for(Lane lane : lanes){
            if (lane.getLight() != IndicatorLight.Red) {
                pedestrianLight = IndicatorLight.Red;
                break;
            }
        }
    }

    @Override
    public List<Optional<Vehicle>> moveCarsIntoIntersection() {
        ArrayList<Optional<Vehicle>> returnArray = new ArrayList<>();

        for (Lane lane : lanes){
            if(lane.getLight() == IndicatorLight.Green && lane.vehiclesInQueue()){
                returnArray.add(Optional.of(lane.getNextVehicle()));
            } else returnArray.add(Optional.empty());
        }

        return returnArray;
    }

    @Override
    public Optional<Vehicle> moveCarIntoIntersectionFromLane(int laneNumber) {

        if (lanes.get(laneNumber).getLight() == IndicatorLight.Green && lanes.get(laneNumber).vehiclesInQueue()){
            return Optional.of(lanes.get(laneNumber).getNextVehicle());
        }
        return Optional.empty();
    }

    @Override
    public int getSumPriority() {
        int sum = 0;
        for (Lane lane : lanes){
            sum += lane.getSumPriority();
        }
        return sum * this.lightPriority + pedestrians.size();
    }

    @Override
    public int getGreenPriority() {
        int sum = 0;
        for (Lane lane : lanes){
            if (lane.getLight() == IndicatorLight.Green){
                sum += lane.getSumPriority();
            }
        }
        //peds want the light to be Red
        return Math.max(0, sum * lightPriority - pedestrians.size());
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        Set<LaneTurn> possibleLaneTurns = new HashSet<>();
        if (vehicle.endRoad() == this.startDirection.getOppositeRoad()) {
            possibleLaneTurns.add(LaneTurn.LeftStraight);
            possibleLaneTurns.add(LaneTurn.Straight);
            possibleLaneTurns.add(LaneTurn.LeftStraightRight);
            possibleLaneTurns.add(LaneTurn.StraightRight);
        } else if (vehicle.endRoad() == this.startDirection.getRoadOnTheRight()) {
            possibleLaneTurns.add(LaneTurn.Right);
            possibleLaneTurns.add(LaneTurn.StraightRight);
            possibleLaneTurns.add(LaneTurn.LeftStraightRight);
        } else if (vehicle.endRoad() == this.startDirection.getRoadOnTheLeft()){
            possibleLaneTurns.add(LaneTurn.Left);
            possibleLaneTurns.add(LaneTurn.LeftStraight);
            possibleLaneTurns.add(LaneTurn.LeftStraightRight);
        }
        /*
         * It is possible to make a U-turn from a left-turn lane, but only when it is the lane furthest to the left.
         * Therefore, the algorithm should check for additional U-turn lanes, and if none exist:
         *
         * chosenLane = lanes.getFirst();
         *
         * Due to checkLanePlacement(), the chosen lane must be either:
         * - A dedicated U-turn lane, or
         * - A left-turn lane
         *
         * As a result, making a U-turn from this lane is ALWAYS valid.
         */
        else if (lanes.getFirst().getTurn() != LaneTurn.UTurn){
            Lane chosenLane = lanes.getFirst();
            chosenLane.addVehicle(vehicle);
            return;
        }else {
            possibleLaneTurns.add(LaneTurn.UTurn);
        }

        ArrayList<Lane> correctLanes = new ArrayList<>();
        for (Lane lane : lanes){
            if(possibleLaneTurns.contains(lane.getTurn())){
                correctLanes.add(lane);
            }
        }

        int shortestQueueAmount = Integer.MAX_VALUE;
        Lane chosenLane = correctLanes.getFirst();
        for (Lane correctLane : correctLanes){
            if (correctLane.getAmountVehiclesInQueue() < shortestQueueAmount){
                chosenLane = correctLane;
                shortestQueueAmount = correctLane.getAmountVehiclesInQueue();
            }
        }
        chosenLane.addVehicle(vehicle);
    }

    public void addPedestrian(Pedestrian pedestrian){
        this.pedestrians.add(pedestrian);
    }

    public List<Pedestrian> pedestriansCrossing(){
        List<Pedestrian> pedestrians = this.pedestrians.stream().toList();
        this.pedestrians.clear();
        return pedestrians;
    }


    @Override
    public int getAmountOfLanes() {
        return lanes.size();
    }

    @Override
    public boolean isBlockedByPedestrians() {
        return pedestrianLight == IndicatorLight.Green;
    }

    @Override
    public List<Lane> getLanesList() {
        return lanes;
    }

    @Override
    public String toString(){
        StringBuilder resultString = new StringBuilder(startDirection.toString() + " road; [");

        for (Lane lane : lanes){
            resultString.append(lane.getLight().toString());
        }
        resultString.append("]");
        return resultString.toString();
    }
}
