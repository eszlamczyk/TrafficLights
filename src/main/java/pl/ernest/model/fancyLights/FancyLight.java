package pl.ernest.model.fancyLights;

import pl.ernest.model.*;

import java.util.*;

public class FancyLight implements ILight {

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
        pedestrianLight = IndicatorLight.Green;
        for(Lane lane : lanes){
            lane.nextLight();
            if (lane.getLight() != IndicatorLight.Red){
                pedestrianLight = IndicatorLight.Red;
            }
        }
    }

    @Override
    public List<Optional<Vehicle>> greenCars() {
        ArrayList<Optional<Vehicle>> returnArray = new ArrayList<>();

        for (Lane lane : lanes){
            if(lane.getLight() == IndicatorLight.Green && lane.vehiclesInQueue()){
                returnArray.add(Optional.of(lane.getVehicle()));
            } else returnArray.add(Optional.empty());
        }

        return returnArray;
    }

    @Override
    public Optional<Vehicle> greenCarFromIndex(int index) {

        if (lanes.get(index).getLight() == IndicatorLight.Green && lanes.get(index).vehiclesInQueue()){
            return Optional.of(lanes.get(index).getVehicle());
        }
        return Optional.empty();
    }

    @Override
    public int getSumPriority() {
        int sum = 0;
        for (Lane lane : lanes){
            sum += lane.getSumPriority();
        }
        return sum * this.lightPriority - pedestrians.size();
    }

    @Override
    public int getGreenPriority() {
        int sum = 0;
        for (Lane lane : lanes){
            if (lane.getLight() == IndicatorLight.Green){
                sum += lane.getSumPriority();
            }
        }
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
        } else {
            possibleLaneTurns.add(LaneTurn.UTurn);
            possibleLaneTurns.add(LaneTurn.Left);
            possibleLaneTurns.add(LaneTurn.LeftStraight);
            possibleLaneTurns.add(LaneTurn.LeftStraightRight);
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
            if (correctLane.getLaneQueueSize() < shortestQueueAmount){
                chosenLane = correctLane;
                shortestQueueAmount = correctLane.getLaneQueueSize();
            }
        }
        chosenLane.addVehicle(vehicle);
    }

    @Override
    public int getAmountOfLanes() {
        return lanes.size();
    }

    @Override
    public boolean isBlocked() {
        return pedestrianLight == IndicatorLight.Green;
    }
}
