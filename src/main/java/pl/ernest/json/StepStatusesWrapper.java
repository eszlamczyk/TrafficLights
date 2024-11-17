package pl.ernest.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StepStatusesWrapper {
    @JsonProperty("stepStatuses")
    private final List<IStepStatus> stepStatuses;

    public StepStatusesWrapper(List<IStepStatus> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }
}

