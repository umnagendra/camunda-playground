package xyz.nagendra.camundaplayground.api.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class WorkflowRequest {
    private String workflowKey;
    private String businessKey;
    private String startedBy;

    public WorkflowRequest(String workflowKey, String businessKey, String startedBy) {
        this.workflowKey = workflowKey;
        this.businessKey = businessKey;
        this.startedBy = startedBy;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
