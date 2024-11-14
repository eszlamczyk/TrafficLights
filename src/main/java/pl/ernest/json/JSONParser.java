package pl.ernest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.ernest.basicLights.Lights;
import pl.ernest.command.AddVehicleCommand;
import pl.ernest.command.ICommand;
import pl.ernest.command.StepCommand;
import pl.ernest.model.Road;
import pl.ernest.model.Vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<ICommand> parseInput(String jsonString, Lights lights) throws IOException {
        List<ICommand> commands = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        for (JsonNode commandNode : rootNode.get("commands")) {
            String type = commandNode.get("type").asText();

            switch (type) {
                case "addVehicle":
                    String vehicleId = commandNode.get("vehicleId").asText();
                    Road startRoad = Road.valueOf(commandNode.get("startRoad").asText());
                    Road endRoad = Road.valueOf(commandNode.get("endRoad").asText());
                    Vehicle newVehicle;
                    if (commandNode.has("priority")){
                        int priority = commandNode.get("priority").asInt();
                        newVehicle = new Vehicle(vehicleId,endRoad,priority);
                    }else {
                        newVehicle = new Vehicle(vehicleId,endRoad);
                    }
                    commands.add(new AddVehicleCommand(newVehicle,lights,startRoad));
                    break;

                case "step":
                    commands.add(new StepCommand(lights));
                    break;

                default:
                    throw new IllegalArgumentException("Unknown command type: " + type);
            }
        }
        return commands;
    }
}