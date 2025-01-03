package pl.ernest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.ernest.command.AddPedestrianCommand;
import pl.ernest.model.*;
import pl.ernest.command.AddVehicleCommand;
import pl.ernest.command.ICommand;
import pl.ernest.command.StepCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<ICommand> parseInput(String jsonPath, TrafficLights trafficLights) throws IOException {
        List<ICommand> commands = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(jsonPath));

        for (JsonNode commandNode : rootNode.get("commands")) {
            String type = commandNode.get("type").asText();

            switch (type) {
                case "addVehicle":
                    String vehicleId = commandNode.get("vehicleId").asText();
                    Road startRoad = Road.valueOf(commandNode.get("startRoad").asText());
                    Road endRoad = Road.valueOf(commandNode.get("endRoad").asText());

                    boolean isBus = "bus".equalsIgnoreCase(commandNode.path("vehicleType").asText(null));
                    int priority = commandNode.path("priority").asInt(1);

                    IVehicle newVehicle = isBus
                            ? new Bus(vehicleId, endRoad, priority)
                            : new Car(vehicleId, endRoad, priority);

                    commands.add(new AddVehicleCommand(newVehicle, trafficLights, startRoad));
                    break;

                case "addPedestrian":
                    String pedId = commandNode.get("pedestrianId").asText();
                    Road road = Road.valueOf(commandNode.get("road").asText());
                    Pedestrian ped = new Pedestrian(pedId,road);
                    commands.add(new AddPedestrianCommand(ped,trafficLights,road));
                case "step":
                    commands.add(new StepCommand(trafficLights));
                    break;

                default:
                    throw new IllegalArgumentException("Unknown command type: " + type);
            }
        }
        return commands;
    }

    public static void createOutput(String outputPath){

        Logger logger = Logger.getInstance();
        List<IStepStatus> stepStatusList = logger.getStepStatuses();

        try {
            ObjectMapper mapper = new ObjectMapper();
            File outputFile = new File(outputPath);

            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, new StepStatusesWrapper(stepStatusList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}