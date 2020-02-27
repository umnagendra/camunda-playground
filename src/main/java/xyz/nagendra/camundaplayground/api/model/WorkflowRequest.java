package xyz.nagendra.camundaplayground.api.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class WorkflowRequest {
    private String workflowKey;
    private String startedBy;

    public WorkflowRequest(String workflowKey, String startedBy) {
        this.workflowKey = workflowKey;
        this.startedBy = startedBy;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public String getStartedBy() {
        return startedBy;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
