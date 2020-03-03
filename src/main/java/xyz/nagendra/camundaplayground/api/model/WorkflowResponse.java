package xyz.nagendra.camundaplayground.api.model;

import java.util.Map;

public class WorkflowResponse {
    private String workflowKey;
    private String businessKey;
    private boolean active;

    public WorkflowResponse(String workflowKey, String businessKey, boolean active) {
        this.workflowKey = workflowKey;
        this.businessKey = businessKey;
        this.active = active;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public boolean isActive() {
        return active;
    }
}
