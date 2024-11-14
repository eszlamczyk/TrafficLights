package pl.ernest;

import pl.ernest.basicLights.BasicLight;
import pl.ernest.basicLights.Lights;
import pl.ernest.command.ICommand;
import pl.ernest.json.JSONParser;
import pl.ernest.model.ILight;
import pl.ernest.model.TrafficCycle;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String jsonString = """
                {

                  "commands": [

                    {

                      "type": "addVehicle",

                      "vehicleId": "vehicle1",

                      "startRoad": "south",

                      "endRoad": "north"

                    },

                    {

                      "type": "addVehicle",

                      "vehicleId": "vehicle2",

                      "startRoad": "north",

                      "endRoad": "south"

                    },

                    {

                      "type": "step"

                    },

                    {

                      "type": "step"

                    },

                    {

                      "type": "addVehicle",

                      "vehicleId": "vehicle3",

                      "startRoad": "west",

                      "endRoad": "south"

                    },

                    {

                      "type": "addVehicle",

                      "vehicleId": "vehicle4",

                      "startRoad": "west",

                      "endRoad": "south"

                    },

                    {

                      "type": "step"

                    },

                    {

                      "type": "step"

                    }

                  ]

                }""";

        ILight northLight = new BasicLight(new LinkedList<>(), TrafficCycle.Green);
        ILight southLight = new BasicLight(new LinkedList<>(), TrafficCycle.Green);
        ILight eastLight = new BasicLight(new LinkedList<>(), TrafficCycle.Red);
        ILight westLight = new BasicLight(new LinkedList<>(), TrafficCycle.Red);

        Lights lights = new Lights(northLight,eastLight,southLight,westLight);

        List<ICommand> commandList = JSONParser.parseInput(jsonString, lights);

        for (ICommand command : commandList){
            command.execute();
        }

        System.out.println(lights.getStepStatuses());

    }
}