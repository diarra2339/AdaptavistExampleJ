package com.adaptavist.tm4j.junit;

import com.adaptavist.tm4j.junit.result.ExecutionResult;
import com.adaptavist.tm4j.junit.result.ExecutionResults;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecutionReportOutputFileBuilder {

    public static final String FAILED = "Failed";
    public static final String PASSED = "Passed";
    public static final String DEFAULT_TM4J_RESULT_FILE_NAME = "tm4j_result.json";

    private ExecutionResults executionResults = new ExecutionResults();
    private List<JUnitTestMethodID> failedTests = new ArrayList();

    public void registerFailure(JUnitTestMethodID testMethodId) {
        failedTests.add(testMethodId);
    }

    public void registerResult(String testCaseKey, JUnitTestMethodID testMethodId) {
        Optional<ExecutionResult> tm4jExecutionResultForMethodId = executionResults.getRegisteredResultFor(testMethodId);

        if (tm4jExecutionResultForMethodId.isPresent()) {
            ExecutionResult executionResult = tm4jExecutionResultForMethodId.get();
            executionResult.setResult(getResultFor(testMethodId));
        } else {
            registerNewResult(testCaseKey, testMethodId);
        }
    }

    private void registerNewResult(String testCaseKey, JUnitTestMethodID testMethodId) {
        ExecutionResult executionResult = new ExecutionResult();

        executionResult.setSource(testMethodId.getDescription());
        executionResult.setTestCaseKey(testCaseKey);
        executionResult.setResult(getResultFor(testMethodId));

        executionResults.addResult(executionResult);
    }

    private String getResultFor(JUnitTestMethodID testMethodId) {
        return failedTests.contains(testMethodId) ? FAILED : PASSED;
    }

    public void generateResultsFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(DEFAULT_TM4J_RESULT_FILE_NAME), executionResults);
    }
}
