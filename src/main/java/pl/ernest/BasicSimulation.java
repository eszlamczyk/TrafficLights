package pl.ernest;

import pl.ernest.model.*;
import pl.ernest.model.basicLights.BasicLight;
import pl.ernest.command.ICommand;
import pl.ernest.json.JSONParser;
import pl.ernest.strategy.BasicTurnsStrategy;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BasicSimulation {

    public static void main(String[] args) throws IOException {

        if (args.length < 2){
            System.out.println("please provide input and output json file path");
            return;
        }

        TrafficLights trafficLights = getTrafficLights();

        List<ICommand> commandList = JSONParser.parseInput(args[0], trafficLights);

        //Logger.getInstance().startFancyLogging();

        for (ICommand command : commandList){
            command.execute();
        }

        JSONParser.createOutput(args[1]);
    }

    private static TrafficLights getTrafficLights() {
        ILight northLight = new BasicLight(new LinkedList<>(), IndicatorLight.YellowRed , Road.north);
        ILight southLight = new BasicLight(new LinkedList<>(), IndicatorLight.YellowRed, Road.south);
        ILight eastLight = new BasicLight(new LinkedList<>(), IndicatorLight.Yellow, Road.east);
        ILight westLight = new BasicLight(new LinkedList<>(), IndicatorLight.Yellow, Road.west);

        int priorityConstant = 5;

        try {
            return new TrafficLights(northLight,eastLight,southLight,westLight, new BasicTurnsStrategy(priorityConstant));
        } catch (CollidingLightConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}