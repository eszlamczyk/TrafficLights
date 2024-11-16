package pl.ernest.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class StepStatus {
    @JsonProperty("leftVehicles")
    public List<String> leftVehicles = new ArrayList<>();

    @JsonProperty("newLightCycle")
    public List<String> newLightCycle = new ArrayList<>();

}
