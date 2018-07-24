package com.adaptavist.tm4j.junit.result;

import com.adaptavist.tm4j.junit.JUnitTestMethodID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecutionResults {

    private List<ExecutionResult> executions = new ArrayList();

    public List<ExecutionResult> getExecutions() {
        return executions;
    }

    public void setExecutions(List<ExecutionResult> executions) {
        this.executions = executions;
    }

    public void addResult(ExecutionResult executionResult) {
        executions.add(executionResult);
    }

    public Optional<ExecutionResult> getRegisteredResultFor(JUnitTestMethodID testMethodId) {
        return executions
                .stream()
                .filter(r -> r.getSource().equals(testMethodId.getDescription()))
                .findFirst();
    }
}
