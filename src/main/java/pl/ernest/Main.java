package pl.ernest;

import pl.ernest.model.basicLights.BasicLight;
import pl.ernest.model.TrafficLights;
import pl.ernest.command.ICommand;
import pl.ernest.json.JSONParser;
import pl.ernest.model.ILight;
import pl.ernest.model.IndicatorLight;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length < 2){
            System.out.println("please provide input and output json file path");
            return;
        }

        ILight northLight = new BasicLight(new LinkedList<>(), IndicatorLight.Green);
        ILight southLight = new BasicLight(new LinkedList<>(), IndicatorLight.Green);
        ILight eastLight = new BasicLight(new LinkedList<>(), IndicatorLight.Red);
        ILight westLight = new BasicLight(new LinkedList<>(), IndicatorLight.Red);

        int priorityConstant = 10;

        TrafficLights trafficLights = new TrafficLights(northLight,eastLight,southLight,westLight, priorityConstant);

        List<ICommand> commandList = JSONParser.parseInput(args[0], trafficLights);

        for (ICommand command : commandList){
            command.execute();
        }

        JSONParser.createOutput(args[1], trafficLights);
        //System.out.println(trafficLights.getStepStatuses());
    }
}