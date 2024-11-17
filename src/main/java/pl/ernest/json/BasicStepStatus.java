package pl.ernest.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BasicStepStatus implements IStepStatus{

    @JsonProperty("leftVehicles")
    public List<String> leftVehicles = new ArrayList<>();
}
