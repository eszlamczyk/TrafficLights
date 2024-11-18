package pl.ernest;

import pl.ernest.command.ICommand;
import pl.ernest.json.JSONParser;
import pl.ernest.json.Logger;
import pl.ernest.model.*;
import pl.ernest.model.fancyLights.FancyLight;
import pl.ernest.model.fancyLights.IncorrectLaneArrangementException;
import pl.ernest.model.fancyLights.LaneTurn;
import pl.ernest.strategy.BasicTurnsStrategy;

import java.io.IOException;
import java.util.*;

public class FancySimulation {

    public static void main(String[] args) throws IOException {

        if (args.length < 2){
            System.out.println("please provide input and output json file path");
            return;
        }

        TrafficLights trafficLights;
        try {
            trafficLights = getTrafficLights();
        } catch (IncorrectLaneArrangementException e) {
            throw new RuntimeException(e);
        }

        Logger.getInstance().startFancyLogging();

        List<ICommand> commandList = JSONParser.parseInput(args[0], trafficLights);

        for (ICommand command : commandList){
            command.execute();
        }

        JSONParser.createOutput(args[1]);
    }

    private static TrafficLights getTrafficLights() throws IncorrectLaneArrangementException {
        Queue<IndicatorLight> qLefts = new LinkedList<>();
        qLefts.add(IndicatorLight.Red);
        qLefts.add(IndicatorLight.YellowRed);
        qLefts.add(IndicatorLight.Green);
        qLefts.add(IndicatorLight.Yellow);
        qLefts.add(IndicatorLight.Red);
        qLefts.add(IndicatorLight.Red);
        qLefts.add(IndicatorLight.Red);
        qLefts.add(IndicatorLight.Red);

        Queue<IndicatorLight> qMain = new LinkedList<>();
        qMain.add(IndicatorLight.Green);
        qMain.add(IndicatorLight.Yellow);
        qMain.add(IndicatorLight.Red);
        qMain.add(IndicatorLight.Red);
        qMain.add(IndicatorLight.Red);
        qMain.add(IndicatorLight.Red);
        qMain.add(IndicatorLight.Red);
        qMain.add(IndicatorLight.YellowRed);

        Queue<IndicatorLight> qEast = new LinkedList<>();
        qEast.add(IndicatorLight.Red);
        qEast.add(IndicatorLight.Red);
        qEast.add(IndicatorLight.Red);
        qEast.add(IndicatorLight.YellowRed);
        qEast.add(IndicatorLight.Green);
        qEast.add(IndicatorLight.Yellow);
        qEast.add(IndicatorLight.Red);
        qEast.add(IndicatorLight.Red);

        Queue<IndicatorLight> qWest = new LinkedList<>();
        qWest.add(IndicatorLight.Red);
        qWest.add(IndicatorLight.Red);
        qWest.add(IndicatorLight.Red);
        qWest.add(IndicatorLight.Red);
        qWest.add(IndicatorLight.Red);
        qWest.add(IndicatorLight.YellowRed);
        qWest.add(IndicatorLight.Green);
        qWest.add(IndicatorLight.Yellow);

        ILight northLight = new FancyLight(Arrays.asList(
                new Lane(LaneTurn.Left,new LinkedList<>(qLefts),new LinkedList<>()),
                new Lane(LaneTurn.Straight,new LinkedList<>(qMain),new LinkedList<>()),
                new Lane(LaneTurn.StraightRight,new LinkedList<>(qMain),new LinkedList<>()),
                new Lane(LaneTurn.Right,new LinkedList<>(qMain),new LinkedList<>(), false,true)),
                    new ArrayList<>(),2,Road.north);

        ILight southLight = new FancyLight(Arrays.asList(
                new Lane(LaneTurn.Left,new LinkedList<>(qLefts),new LinkedList<>()),
                new Lane(LaneTurn.Straight,new LinkedList<>(qMain),new LinkedList<>()),
                new Lane(LaneTurn.StraightRight,new LinkedList<>(qMain),new LinkedList<>()),
                new Lane(LaneTurn.Right,new LinkedList<>(qMain),new LinkedList<>(), false,true)),
                    new ArrayList<>(),2,Road.south);

        ILight eastLight = new FancyLight(Arrays.asList(
                new Lane(LaneTurn.LeftStraight, new LinkedList<>(qEast),new LinkedList<>(), true, false),
                new Lane(LaneTurn.StraightRight, new LinkedList<>(qEast),new LinkedList<>())
        ),Road.east);

        ILight westLight = new FancyLight(Arrays.asList(
                new Lane(LaneTurn.LeftStraight, new LinkedList<>(qWest),new LinkedList<>(), true, false),
                new Lane(LaneTurn.StraightRight, new LinkedList<>(qWest),new LinkedList<>())
        ),Road.west);


        int priorityConstant = 5;

        try {
            return new TrafficLights(northLight,eastLight,southLight,westLight, new BasicTurnsStrategy(priorityConstant));
        } catch (CollidingLightConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
