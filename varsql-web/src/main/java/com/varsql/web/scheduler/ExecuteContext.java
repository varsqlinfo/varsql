package com.varsql.web.scheduler;

import com.varsql.core.common.constants.ExecuteType;

public class ExecuteContext {

    private final ExecuteType executeType;
    private final String jobName;

    public ExecuteContext(ExecuteType executeType, String jobName) {
        this.executeType = executeType;
        this.jobName = jobName;
    }

    public ExecuteType getExecuteType() {
        return executeType;
    }

    public String getJobName() {
        return jobName;
    }
}