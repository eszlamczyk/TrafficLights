package pl.ernest.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StepStatusesWrapper {
    @JsonProperty("stepStatuses")
    private final List<StepStatus> stepStatuses;

    public StepStatusesWrapper(List<StepStatus> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }
}

