package pl.ernest.fancyLights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.IndicatorLight;
import pl.ernest.model.Road;
import pl.ernest.model.fancyLights.FancyLight;
import pl.ernest.model.fancyLights.IncorrectLaneArrangementException;
import pl.ernest.model.fancyLights.Lane;
import pl.ernest.model.fancyLights.LaneTurn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class FancyLightTest {

    private Queue<IndicatorLight> basicLightCycle;
    private Lane uTurnLane;
    private Lane LeftLane;
    private Lane LeftStraightLane;
    private Lane LeftStraightRightLane;
    private Lane StraightLane;
    private Lane StraightRightLane;
    private Lane RightLane;

    @BeforeEach
    void setUp(){
        basicLightCycle = new LinkedList<>();
        basicLightCycle.add(IndicatorLight.Green);
        basicLightCycle.add(IndicatorLight.Yellow);
        basicLightCycle.add(IndicatorLight.Red);
        basicLightCycle.add(IndicatorLight.YellowRed);

        uTurnLane = new Lane(LaneTurn.UTurn, new LinkedList<>(basicLightCycle),new LinkedList<>());
        LeftLane = new Lane(LaneTurn.Left, new LinkedList<>(basicLightCycle),new LinkedList<>());
        LeftStraightLane = new Lane(LaneTurn.LeftStraight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        LeftStraightRightLane = new Lane(LaneTurn.LeftStraightRight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        StraightLane = new Lane(LaneTurn.Straight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        StraightRightLane = new Lane(LaneTurn.StraightRight, new LinkedList<>(basicLightCycle),new LinkedList<>());
        RightLane = new Lane(LaneTurn.Right, new LinkedList<>(basicLightCycle),new LinkedList<>());
    }

    @Test
    void checkSufficientLanesTest(){

        String message = "Every single direction is not covered";
        testLanesWithMessage(new Lane[]{uTurnLane}, message);
        testLanesWithMessage(new Lane[]{uTurnLane,LeftLane}, message);
        testLanesWithMessage(new Lane[]{uTurnLane,LeftLane,StraightLane}, message);
        testLanesWithMessage(new Lane[]{uTurnLane,LeftStraightLane}, message);
        testLanesWithMessage(new Lane[]{LeftLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightLane}, message);
        testLanesWithMessage(new Lane[]{StraightLane}, message);
        testLanesWithMessage(new Lane[]{StraightRightLane}, message);
        testLanesWithMessage(new Lane[]{StraightLane,RightLane}, message);
        testLanesWithMessage(new Lane[]{RightLane}, message);
    }

    @Test
    void checkLanesProperOrder(){
        String message = "Conflicting lane arrangement";
        //left
        testLanesWithMessage(new Lane[]{LeftLane, uTurnLane ,StraightRightLane}, message);

        //leftStraight
        testLanesWithMessage(new Lane[]{LeftStraightLane, uTurnLane, RightLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightLane, LeftLane ,RightLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightLane, LeftLane, RightLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightLane, LeftStraightRightLane}, message);

        //leftStraightRight
        testLanesWithMessage(new Lane[]{LeftStraightRightLane, uTurnLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightRightLane, LeftLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightRightLane, LeftStraightLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightRightLane, StraightLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightRightLane, StraightRightLane}, message);
        testLanesWithMessage(new Lane[]{LeftStraightRightLane, LeftStraightRightLane}, message);

        //straight
        testLanesWithMessage(new Lane[]{StraightLane, uTurnLane, LeftLane, RightLane}, message);
        testLanesWithMessage(new Lane[]{StraightLane, LeftLane, RightLane}, message);
        testLanesWithMessage(new Lane[]{StraightLane, LeftStraightLane, RightLane}, message);
        testLanesWithMessage(new Lane[]{StraightLane, LeftStraightRightLane}, message);

        //straightRight
        testLanesWithMessage(new Lane[]{StraightRightLane, uTurnLane, LeftLane}, message);
        testLanesWithMessage(new Lane[]{StraightRightLane, LeftLane}, message);
        testLanesWithMessage(new Lane[]{StraightRightLane, LeftStraightLane}, message);
        testLanesWithMessage(new Lane[]{StraightRightLane, LeftStraightRightLane}, message);
        testLanesWithMessage(new Lane[]{StraightRightLane, StraightLane, LeftLane}, message);

        //Right
        testLanesWithMessage(new Lane[]{RightLane, uTurnLane, LeftStraightLane}, message);
        testLanesWithMessage(new Lane[]{RightLane, LeftLane, StraightLane}, message);
        testLanesWithMessage(new Lane[]{RightLane, LeftStraightLane}, message);
        testLanesWithMessage(new Lane[]{RightLane, LeftStraightRightLane}, message);
        testLanesWithMessage(new Lane[]{RightLane, StraightLane, LeftLane}, message);
        testLanesWithMessage(new Lane[]{RightLane, StraightRightLane, LeftLane}, message);
    }

    void testLanesWithMessage(Lane[] lanes, String message){
        IncorrectLaneArrangementException exception =
                assertThrows(IncorrectLaneArrangementException.class,
                        () -> new FancyLight(Arrays.stream(lanes).toList(),new ArrayList<>(),1, Road.north));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void correctRoadPlacementTest(){
        assertDoesNotThrow(() -> new FancyLight(Arrays.stream(new Lane[]{LeftStraightRightLane}).toList(),
                new ArrayList<>(),1, Road.north));
        assertDoesNotThrow(() -> new FancyLight(Arrays.stream(new Lane[]{LeftLane, StraightRightLane}).toList(),
                new ArrayList<>(),1, Road.north));
        assertDoesNotThrow(() -> new FancyLight(Arrays.stream(new Lane[]{uTurnLane, LeftStraightRightLane}).toList(),
                new ArrayList<>(),1, Road.north));
        assertDoesNotThrow(() -> new FancyLight(Arrays.stream(new Lane[]{uTurnLane, LeftLane, StraightLane, RightLane}).toList(),
                new ArrayList<>(),1, Road.north));
        assertDoesNotThrow(() -> new FancyLight(Arrays.stream(new Lane[]{LeftLane, StraightLane, RightLane}).toList(),
                new ArrayList<>(),1, Road.north));
    }
}
