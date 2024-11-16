package pl.ernest.model.fancyLights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.IndicatorLight;
import pl.ernest.model.Pedestrian;
import pl.ernest.model.Road;
import pl.ernest.model.Vehicle;
import pl.ernest.model.Lane;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FancyLightTest {

    private final Road uTurn = Road.north;
    private final Road left = Road.north.getRoadOnTheLeft();
    private final Road straight = Road.north.getOppositeRoad();
    private final Road right = Road.north.getRoadOnTheRight();
    private Queue<IndicatorLight> basicLightCycle;
    private Lane uTurnLane;
    private Lane leftLane;
    private Lane leftStraightLane;
    private Lane leftStraightRightLane;
    private Lane straightLane;
    private Lane straightRightLane;
    private Lane rightLane;

    @BeforeEach
    void setUp(){
        basicLightCycle = new LinkedList<>();
        basicLightCycle.add(IndicatorLight.Green);
        basicLightCycle.add(IndicatorLight.Yellow);
        basicLightCycle.add(IndicatorLight.Red);
        basicLightCycle.add(IndicatorLight.YellowRed);

        uTurnLane = new Lane(LaneTurn.UTurn, new LinkedList<>(basicLightCycle),new LinkedList<>());
        leftLane = new Lane(LaneTurn.Left, new LinkedList<>(basicLightCycle),new LinkedList<>());
        leftStraightLane = new Lane(LaneTurn.LeftStraight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        leftStraightRightLane = new Lane(LaneTurn.LeftStraightRight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        straightLane = new Lane(LaneTurn.Straight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        straightRightLane = new Lane(LaneTurn.StraightRight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        rightLane = new Lane(LaneTurn.Right, new LinkedList<>(basicLightCycle),new LinkedList<>());
    }

    private FancyLight helperFancyLightConstructor(Lane[] lanes){
        try {
            return new FancyLight(Arrays.stream(lanes).toList(), uTurn);
        }catch (IncorrectLaneArrangementException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    void checkSufficientLanesTest(){

        String message = "Every single direction is not covered";
        testLanesWithMessage(new Lane[]{uTurnLane}, message);
        testLanesWithMessage(new Lane[]{uTurnLane, leftLane}, message);
        testLanesWithMessage(new Lane[]{uTurnLane, leftLane, straightLane}, message);
        testLanesWithMessage(new Lane[]{uTurnLane, leftStraightLane}, message);
        testLanesWithMessage(new Lane[]{leftLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightLane}, message);
        testLanesWithMessage(new Lane[]{straightLane}, message);
        testLanesWithMessage(new Lane[]{straightRightLane}, message);
        testLanesWithMessage(new Lane[]{straightLane, rightLane}, message);
        testLanesWithMessage(new Lane[]{rightLane}, message);
    }

    @Test
    void checkLanesProperOrder(){
        String message = "Conflicting lane arrangement";
        //left
        testLanesWithMessage(new Lane[]{leftLane, uTurnLane , straightRightLane}, message);

        //leftStraight
        testLanesWithMessage(new Lane[]{leftStraightLane, uTurnLane, rightLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightLane, leftLane, rightLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightLane, leftLane, rightLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightLane, leftStraightRightLane}, message);

        //leftStraightRight
        testLanesWithMessage(new Lane[]{leftStraightRightLane, uTurnLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightRightLane, leftLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightRightLane, leftStraightLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightRightLane, straightLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightRightLane, straightRightLane}, message);
        testLanesWithMessage(new Lane[]{leftStraightRightLane, leftStraightRightLane}, message);

        //straight
        testLanesWithMessage(new Lane[]{straightLane, uTurnLane, leftLane, rightLane}, message);
        testLanesWithMessage(new Lane[]{straightLane, leftLane, rightLane}, message);
        testLanesWithMessage(new Lane[]{straightLane, leftStraightLane, rightLane}, message);
        testLanesWithMessage(new Lane[]{straightLane, leftStraightRightLane}, message);

        //straightRight
        testLanesWithMessage(new Lane[]{straightRightLane, uTurnLane, leftLane}, message);
        testLanesWithMessage(new Lane[]{straightRightLane, leftLane}, message);
        testLanesWithMessage(new Lane[]{straightRightLane, leftStraightLane}, message);
        testLanesWithMessage(new Lane[]{straightRightLane, leftStraightRightLane}, message);
        testLanesWithMessage(new Lane[]{straightRightLane, straightLane, leftLane}, message);

        //Right
        testLanesWithMessage(new Lane[]{rightLane, uTurnLane, leftStraightLane}, message);
        testLanesWithMessage(new Lane[]{rightLane, leftLane, straightLane}, message);
        testLanesWithMessage(new Lane[]{rightLane, leftStraightLane}, message);
        testLanesWithMessage(new Lane[]{rightLane, leftStraightRightLane}, message);
        testLanesWithMessage(new Lane[]{rightLane, straightLane, leftLane}, message);
        testLanesWithMessage(new Lane[]{rightLane, straightRightLane, leftLane}, message);
    }

    void testLanesWithMessage(Lane[] lanes, String message){
        IncorrectLaneArrangementException exception =
                assertThrows(IncorrectLaneArrangementException.class,
                        () -> new FancyLight(Arrays.stream(lanes).toList(), uTurn));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void correctRoadPlacementTest(){
        testCorrectLanes(new Lane[]{leftStraightRightLane});
        testCorrectLanes(new Lane[]{uTurnLane, leftStraightRightLane});
        testCorrectLanes(new Lane[]{uTurnLane, uTurnLane, uTurnLane, leftStraightRightLane});
        testCorrectLanes(new Lane[]{uTurnLane, leftLane, leftStraightLane, straightRightLane});
        testCorrectLanes(new Lane[]{uTurnLane, leftLane, leftStraightRightLane, rightLane});
        testCorrectLanes(new Lane[]{leftLane, leftLane, leftStraightRightLane});
        testCorrectLanes(new Lane[]{leftLane, leftStraightRightLane, rightLane});
        testCorrectLanes(new Lane[]{uTurnLane, leftLane, straightLane, rightLane});
        testCorrectLanes(new Lane[]{leftStraightLane, straightRightLane});
        testCorrectLanes(new Lane[]{leftLane, straightLane, rightLane});
        testCorrectLanes(new Lane[]{leftLane, straightLane, straightLane, straightLane, straightRightLane});
        testCorrectLanes(new Lane[]{leftStraightLane, straightLane, rightLane});
    }
    void testCorrectLanes(Lane[] lanes){
        assertDoesNotThrow(() -> new FancyLight(Arrays.stream(lanes).toList(), uTurn));
    }

    @Test
    void nextCycleTest(){
        Lane[] lanes = new Lane[]{leftLane, straightLane, rightLane};
        FancyLight fancyLight = helperFancyLightConstructor(lanes);

        fancyLight.nextCycle();
        sameLightsAssert(lanes, IndicatorLight.Yellow);

        fancyLight.nextCycle();
        sameLightsAssert(lanes, IndicatorLight.Red);

        fancyLight.nextCycle();
        sameLightsAssert(lanes, IndicatorLight.YellowRed);

        fancyLight.nextCycle();
        sameLightsAssert(lanes, IndicatorLight.Green);
    }

    private void sameLightsAssert(Lane[] lanes, IndicatorLight indicatorLight){
        for(Lane lane : lanes){
            assertEquals(indicatorLight,lane.getLight());
        }
    }

    @Test
    void asymmetricLightsTest(){
        //Green/Green/Green
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        //Yellow/Green/Red
        leftLane.nextLight();
        rightLane.nextLight();
        rightLane.nextLight();


        //Red/Yellow/YellowRed
        fancyLight.nextCycle();

        assertEquals(IndicatorLight.Red, leftLane.getLight());
        assertEquals(IndicatorLight.Yellow, straightLane.getLight());
        assertEquals(IndicatorLight.YellowRed, rightLane.getLight());

        //YellowRed/Red/Green
        fancyLight.nextCycle();

        assertEquals(IndicatorLight.YellowRed, leftLane.getLight());
        assertEquals(IndicatorLight.Red, straightLane.getLight());
        assertEquals(IndicatorLight.Green, rightLane.getLight());

        //Green/YellowRed/Yellow
        fancyLight.nextCycle();

        assertEquals(IndicatorLight.Green, leftLane.getLight());
        assertEquals(IndicatorLight.YellowRed, straightLane.getLight());
        assertEquals(IndicatorLight.Yellow, rightLane.getLight());

        //Yellow/Green/Red
        fancyLight.nextCycle();

        assertEquals(IndicatorLight.Yellow, leftLane.getLight());
        assertEquals(IndicatorLight.Green, straightLane.getLight());
        assertEquals(IndicatorLight.Red, rightLane.getLight());
    }

    @Test
    void differentLengthsLightCycleTest(){
        Queue<IndicatorLight> longerLightCycle = new LinkedList<>(basicLightCycle);
        //new cycle -> Green/Yellow/Red/YellowRed/YellowRed
        longerLightCycle.add(IndicatorLight.YellowRed);
        Lane longerLightCycleLane = new Lane(LaneTurn.Right, longerLightCycle, new LinkedList<>());

        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane,longerLightCycleLane});

        fancyLight.nextCycle();
        fancyLight.nextCycle();
        fancyLight.nextCycle();
        fancyLight.nextCycle();

        assertEquals(IndicatorLight.Green, leftLane.getLight());
        assertEquals(IndicatorLight.Green, straightLane.getLight());
        assertEquals(IndicatorLight.YellowRed, longerLightCycleLane.getLight());

    }

    @Test
    void moveCarsIntoIntersectionTest(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        Vehicle leftCar2 = new Vehicle("leftCar2", left);

        leftLane.addVehicle(new Vehicle("leftCar", left));
        leftLane.addVehicle(leftCar2);
        rightLane.addVehicle(new Vehicle("rightCar", right));

        List<Optional<Vehicle>> movedVehicles = fancyLight.moveCarsIntoIntersection();

        assertTrue(movedVehicles.getFirst().isPresent());
        assertTrue(movedVehicles.get(1).isEmpty());
        assertTrue(movedVehicles.getLast().isPresent());

        assertEquals(1, leftLane.getAmountVehiclesInQueue());
        assertEquals(0, straightLane.getAmountVehiclesInQueue());
        assertEquals(0, rightLane.getAmountVehiclesInQueue());

        assertEquals(leftCar2, leftLane.getNextVehicle());
    }

    @Test
    void notGreenMoveCarsIntoIntersectionTest(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        leftLane.addVehicle(new Vehicle("leftCar", left));
        leftLane.addVehicle(new Vehicle("leftCar2", left));
        rightLane.addVehicle(new Vehicle("rightCar", right));

        //Yellow
        fancyLight.nextCycle();
        checkEverythingEmpty(fancyLight);

        //Red
        fancyLight.nextCycle();
        checkEverythingEmpty(fancyLight);

        //YellowRed
        fancyLight.nextCycle();
        checkEverythingEmpty(fancyLight);

    }

    private void checkEverythingEmpty(FancyLight fancyLight){
        List<Optional<Vehicle>> optionalList = fancyLight.moveCarsIntoIntersection();
        for (Optional<Vehicle> optional : optionalList){
            assertTrue(optional.isEmpty());
        }
    }


    @Test
    void moveCarIntoIntersectionTest(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        Vehicle leftCar = new Vehicle("leftCar", left);

        leftLane.addVehicle(leftCar);
        leftLane.addVehicle(new Vehicle("leftCar2", left));
        rightLane.addVehicle(new Vehicle("rightCar", right));

        Optional<Vehicle> leftVehicle = fancyLight.moveCarIntoIntersectionFromLane(0);
        Optional<Vehicle> straightVehicle = fancyLight.moveCarIntoIntersectionFromLane(1);

        assertTrue(leftVehicle.isPresent());
        assertEquals(leftCar, leftVehicle.get());
        assertFalse(straightVehicle.isPresent());

        assertEquals(1, leftLane.getAmountVehiclesInQueue());
        assertEquals(0, straightLane.getAmountVehiclesInQueue());
        assertEquals(1, rightLane.getAmountVehiclesInQueue());
    }

    @Test
    void notGreenMoveCarIntoIntersectionTest(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        leftLane.addVehicle(new Vehicle("leftCar", left));
        leftLane.addVehicle(new Vehicle("leftCar2", left));
        rightLane.addVehicle(new Vehicle("rightCar", right));

        for (int i = 0; i < 3; i++) {
            fancyLight.nextCycle();
            Optional<Vehicle> leftVehicle = fancyLight.moveCarIntoIntersectionFromLane(0);
            Optional<Vehicle> straightVehicle = fancyLight.moveCarIntoIntersectionFromLane(1);

            assertTrue(leftVehicle.isEmpty());
            assertTrue(straightVehicle.isEmpty());

            assertEquals(2, leftLane.getAmountVehiclesInQueue());
            assertEquals(0, straightLane.getAmountVehiclesInQueue());
            assertEquals(1, rightLane.getAmountVehiclesInQueue());
        }
    }


    @Test
    void getSumPriority(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        leftLane.addVehicle(new Vehicle("leftCar", left));
        leftLane.addVehicle(new Vehicle("leftCar2", left));
        rightLane.addVehicle(new Vehicle("rightCar", right));
        rightLane.addVehicle(new Vehicle("rightBus", right, 3));

        fancyLight.addPedestrian(new Pedestrian("ped1", Road.east));
        fancyLight.addPedestrian(new Pedestrian("ped2", Road.east));
        fancyLight.addPedestrian(new Pedestrian("ped3", Road.east));

        int sumPriority = fancyLight.getSumPriority();

        assertEquals(9,sumPriority);
    }

    @Test
    void getGreenPriority(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        leftLane.addVehicle(new Vehicle("leftCar", left));
        leftLane.addVehicle(new Vehicle("leftCar2", left));
        rightLane.addVehicle(new Vehicle("rightCar", right));
        rightLane.addVehicle(new Vehicle("rightBus", right, 3));

        fancyLight.addPedestrian(new Pedestrian("ped1", Road.east));
        fancyLight.addPedestrian(new Pedestrian("ped2", Road.east));
        fancyLight.addPedestrian(new Pedestrian("ped3", Road.east));

        FancyLight fancyNegativePriorityLight =
                helperFancyLightConstructor(new Lane[]{uTurnLane, leftStraightRightLane});

        fancyNegativePriorityLight.addVehicle(new Vehicle("car", uTurn));
        fancyNegativePriorityLight.addVehicle(new Vehicle("car2", straight));

        fancyNegativePriorityLight.addPedestrian(new Pedestrian("ped1", Road.south));
        fancyNegativePriorityLight.addPedestrian(new Pedestrian("ped1", Road.south));
        fancyNegativePriorityLight.addPedestrian(new Pedestrian("ped1", Road.south));
        fancyNegativePriorityLight.addPedestrian(new Pedestrian("ped1", Road.south));
        fancyNegativePriorityLight.addPedestrian(new Pedestrian("ped1", Road.south));
        fancyNegativePriorityLight.addPedestrian(new Pedestrian("ped1", Road.south));


        int greenPriority = fancyLight.getGreenPriority();
        int negativeGreenPriority = fancyNegativePriorityLight.getGreenPriority();


        assertEquals(3,greenPriority);
        assertEquals(0,negativeGreenPriority);
    }


    @Test
    void addCarBaseTest(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        Vehicle carUTurn1 = new Vehicle("carUTurn1", uTurn);
        Vehicle carLeft1 = new Vehicle("carLeft1", left);
        Vehicle carStraight1 = new Vehicle("carStraight1", straight);
        Vehicle carRight1 = new Vehicle("carRight1", right);
        Vehicle carRight2 = new Vehicle("carRight2", right);
        Vehicle carStraight2 = new Vehicle("carStraight2", straight);
        Vehicle carLeft2 = new Vehicle("carLeft2", left);
        Vehicle carUTurn2 = new Vehicle("carUTurn2", uTurn);


        fancyLight.addVehicle(carUTurn1);
        fancyLight.addVehicle(carLeft1);
        fancyLight.addVehicle(carStraight1);
        fancyLight.addVehicle(carRight1);
        fancyLight.addVehicle(carRight2);
        fancyLight.addVehicle(carStraight2);
        fancyLight.addVehicle(carLeft2);
        fancyLight.addVehicle(carUTurn2);


        assertEquals(carUTurn1, leftLane.getNextVehicle());
        assertEquals(carLeft1, leftLane.getNextVehicle());
        assertEquals(carLeft2, leftLane.getNextVehicle());
        assertEquals(carUTurn2, leftLane.getNextVehicle());
        assertEquals(carStraight1, straightLane.getNextVehicle());
        assertEquals(carStraight2, straightLane.getNextVehicle());
        assertEquals(carRight1, rightLane.getNextVehicle());
        assertEquals(carRight2, rightLane.getNextVehicle());

        // Ensure no vehicles remain in queues
        assertFalse(leftLane.vehiclesInQueue());
        assertFalse(straightLane.vehiclesInQueue());
        assertFalse(rightLane.vehiclesInQueue());
    }

    @Test
    void addCarMultipleLanesTest(){
        Lane additionalRightLane = new Lane(LaneTurn.Right, new LinkedList<>(basicLightCycle), new LinkedList<>());
        FancyLight fancyLight =
                helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane, additionalRightLane});

        Vehicle carRight1 = new Vehicle("carRight1", right);
        Vehicle carRight2 = new Vehicle("carRight2", right);
        Vehicle carRight3 = new Vehicle("carRight3", right);
        Vehicle carRight4 = new Vehicle("carRight4", right);


        fancyLight.addVehicle(carRight1);
        fancyLight.addVehicle(carRight2);
        fancyLight.addVehicle(carRight3);
        fancyLight.addVehicle(carRight4);


        assertEquals(2, additionalRightLane.getAmountVehiclesInQueue());
        assertEquals(2, rightLane.getAmountVehiclesInQueue());

        assertEquals(carRight1, rightLane.getNextVehicle());
        assertEquals(carRight3, rightLane.getNextVehicle());
        assertEquals(carRight2, additionalRightLane.getNextVehicle());
        assertEquals(carRight4, additionalRightLane.getNextVehicle());
    }

    @Test
    void addCarMultipleLanesLeftAndUTurn(){
        Lane additionalLeftLane = new Lane(LaneTurn.Left, new LinkedList<>(basicLightCycle), new LinkedList<>());
        FancyLight fancyLight =
                helperFancyLightConstructor(new Lane[]{leftLane, additionalLeftLane, straightLane, rightLane});

        Vehicle carLeft1 = new Vehicle("carLeft1", left);
        Vehicle carUTurn1 = new Vehicle("carUTurn1", uTurn);
        Vehicle carUTurn2 = new Vehicle("carUTurn2", uTurn);
        Vehicle carLeft2 = new Vehicle("carLeft2", left);
        Vehicle carUTurn3 = new Vehicle("carUTurn3", uTurn);


        fancyLight.addVehicle(carLeft1);
        fancyLight.addVehicle(carUTurn1);
        fancyLight.addVehicle(carUTurn2);
        fancyLight.addVehicle(carLeft2);
        fancyLight.addVehicle(carUTurn3);


        assertEquals(4, leftLane.getAmountVehiclesInQueue());
        assertEquals(1, additionalLeftLane.getAmountVehiclesInQueue());

        assertEquals(carLeft1, leftLane.getNextVehicle());
        assertEquals(carUTurn1, leftLane.getNextVehicle());
        assertEquals(carUTurn2, leftLane.getNextVehicle());
        assertEquals(carUTurn3, leftLane.getNextVehicle());

        assertEquals(carLeft2, additionalLeftLane.getNextVehicle());
    }

    @Test
    void addCarMultipleLanesDoubleLanesLeft(){
        FancyLight fancyLight =
                helperFancyLightConstructor(new Lane[]{leftLane, leftStraightLane, straightLane, rightLane});

        Vehicle carLeft1 = new Vehicle("carLeft1", left);
        Vehicle carLeft2 = new Vehicle("carLeft2", left);
        Vehicle carLeft3 = new Vehicle("carLeft3", left);
        Vehicle carStraight1 = new Vehicle("carStraight1", straight);
        Vehicle carStraight2 = new Vehicle("carStraight2", straight);
        Vehicle carStraight3 = new Vehicle("carStraight3", straight);


        fancyLight.addVehicle(carLeft1);
        fancyLight.addVehicle(carStraight1);
        fancyLight.addVehicle(carLeft2);
        fancyLight.addVehicle(carLeft3);
        fancyLight.addVehicle(carStraight2);
        fancyLight.addVehicle(carStraight3);


        assertEquals(2, leftLane.getAmountVehiclesInQueue());
        assertEquals(2, leftStraightLane.getAmountVehiclesInQueue());
        assertEquals(2, straightLane.getAmountVehiclesInQueue());

        assertEquals(carLeft1, leftLane.getNextVehicle());
        assertEquals(carLeft2, leftLane.getNextVehicle());

        assertEquals(carStraight1, leftStraightLane.getNextVehicle());
        assertEquals(carLeft3, leftStraightLane.getNextVehicle());

        assertEquals(carStraight2, straightLane.getNextVehicle());
        assertEquals(carStraight3, straightLane.getNextVehicle());
    }

    @Test
    void addCarMultipleLanesDoubleLanesRightTest(){
        FancyLight fancyLight =
                helperFancyLightConstructor(new Lane[]{leftLane, straightLane, straightRightLane, rightLane});

        Vehicle carStraight1 = new Vehicle("carStraight1", straight);
        Vehicle carStraight2 = new Vehicle("carStraight2", straight);
        Vehicle carStraight3 = new Vehicle("carStraight3", straight);
        Vehicle carRight1 = new Vehicle("carRight1", right);
        Vehicle carRight2 = new Vehicle("carRight2", right);
        Vehicle carRight3 = new Vehicle("carRight3", right);

        fancyLight.addVehicle(carRight1);
        fancyLight.addVehicle(carStraight1);
        fancyLight.addVehicle(carStraight2);
        fancyLight.addVehicle(carStraight3);
        fancyLight.addVehicle(carRight2);
        fancyLight.addVehicle(carRight3);

        assertEquals(2, straightLane.getAmountVehiclesInQueue());
        assertEquals(2, straightRightLane.getAmountVehiclesInQueue());
        assertEquals(2, rightLane.getAmountVehiclesInQueue());

        assertEquals(carStraight1, straightLane.getNextVehicle());
        assertEquals(carStraight2, straightLane.getNextVehicle());

        assertEquals(carRight1, straightRightLane.getNextVehicle());
        assertEquals(carStraight3, straightRightLane.getNextVehicle());

        assertEquals(carRight2, rightLane.getNextVehicle());
        assertEquals(carRight3, rightLane.getNextVehicle());
    }

    @Test
    void pedestriansTest(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftStraightRightLane});

        Pedestrian ped1 = new Pedestrian("ped1", left);
        Pedestrian ped2 = new Pedestrian("ped2", left);
        Pedestrian ped3 = new Pedestrian("ped3", left);
        Pedestrian ped4 = new Pedestrian("ped4", right);
        Pedestrian ped5 = new Pedestrian("ped5", right);
        Pedestrian ped6 = new Pedestrian("ped6", right);
        Pedestrian ped7 = new Pedestrian("ped7", straight);
        Pedestrian ped8 = new Pedestrian("ped8", straight);
        Pedestrian ped9 = new Pedestrian("ped9", straight);

        List<Pedestrian> expectedList = new ArrayList<>();
        expectedList.add(ped1);
        expectedList.add(ped2);
        expectedList.add(ped3);
        expectedList.add(ped4);
        expectedList.add(ped5);
        expectedList.add(ped6);
        expectedList.add(ped7);
        expectedList.add(ped8);
        expectedList.add(ped9);


        fancyLight.addPedestrian(ped1);
        fancyLight.addPedestrian(ped2);
        fancyLight.addPedestrian(ped3);
        fancyLight.addPedestrian(ped4);
        fancyLight.addPedestrian(ped5);
        fancyLight.addPedestrian(ped6);
        fancyLight.addPedestrian(ped7);
        fancyLight.addPedestrian(ped8);
        fancyLight.addPedestrian(ped9);

        List<Pedestrian> returnList = fancyLight.pedestriansCrossing();
        List<Pedestrian> returnListEmpty = fancyLight.pedestriansCrossing();

        assertEquals(9,returnList.size());
        assertTrue(returnListEmpty.isEmpty());

        for (int i = 0; i < 9; i++) {
            assertEquals(expectedList.get(i), returnList.get(i));
        }
    }

    @Test
    void pedestrianLightTest(){
        FancyLight fancyLight = helperFancyLightConstructor(new Lane[]{leftLane, straightLane, rightLane});

        assertFalse(fancyLight.isBlockedByPedestrians());

        fancyLight.nextCycle();

        assertFalse(fancyLight.isBlockedByPedestrians());

        fancyLight.nextCycle();

        assertTrue(fancyLight.isBlockedByPedestrians());

        fancyLight.nextCycle();

        assertFalse(fancyLight.isBlockedByPedestrians());

        //manually change 1 light so now its asymmetric:  Yellow/Green/Green
        leftLane.nextLight();

        //Red/Yellow/Yellow
        fancyLight.nextCycle();
        assertFalse(fancyLight.isBlockedByPedestrians());

        //YellowRed/Red/Red
        fancyLight.nextCycle();
        assertFalse(fancyLight.isBlockedByPedestrians());

        //Green/YellowRed/YellowRed
        fancyLight.nextCycle();
        assertFalse(fancyLight.isBlockedByPedestrians());
    }
}