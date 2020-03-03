package xyz.nagendra.camundaplayground.api.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ApprovalRequest {
    private String workflowKey;
    private String businessKey;
    private String approvedBy;

    public ApprovalRequest(String workflowKey, String businessKey, String approvedBy) {
        this.workflowKey = workflowKey;
        this.businessKey = businessKey;
        this.approvedBy = approvedBy;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
