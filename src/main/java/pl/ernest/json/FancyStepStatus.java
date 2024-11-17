package pl.ernest.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class FancyStepStatus implements IStepStatus{
    @JsonProperty("leftVehicles")
    public List<String> leftVehicles = new ArrayList<>();

    @JsonProperty("leftPedestrians")
    public List<String> leftPedestrians = new ArrayList<>();

    @JsonProperty("newLightCycle")
    public List<String> newLightCycle = new ArrayList<>();

}
