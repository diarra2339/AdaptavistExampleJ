package com.adaptavist.tm4j.junit;

import com.adaptavist.tm4j.junit.result.Tm4jExecutionResult;
import com.adaptavist.tm4j.junit.result.Tm4jJUnitResults;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tm4jJUnitResultsBuilder {

    private Tm4jJUnitResults tm4jJUnitResults = new Tm4jJUnitResults();
    private List<TM4JJUnitTestMethodID> failedTests = new ArrayList();

    public void registerFailure(TM4JJUnitTestMethodID testMethodId) {
        failedTests.add(testMethodId);
    }

    public void registerResult(String testCaseKey, TM4JJUnitTestMethodID testMethodId) {
        Optional<Tm4jExecutionResult> tm4jExecutionResultForMethodIdentifier = tm4jJUnitResults.getResults()
                .stream()
                .filter(r -> r.getSource().equals(testMethodId.getDescription()))
                .findFirst();

        if (tm4jExecutionResultForMethodIdentifier.isPresent()) {
            Tm4jExecutionResult executionResult = tm4jExecutionResultForMethodIdentifier.get();
            executionResult.setResult(getResultFor(testMethodId));
        } else {
            registerNewResult(testCaseKey, testMethodId);
        }
    }

    private void registerNewResult(String testCaseKey, TM4JJUnitTestMethodID testMethodId) {
        Tm4jExecutionResult executionResult = new Tm4jExecutionResult();

        executionResult.setSource(testMethodId.getDescription());
        executionResult.setTestCaseKey(testCaseKey);
        executionResult.setResult(getResultFor(testMethodId));

        tm4jJUnitResults.addResult(executionResult);
    }

    private String getResultFor(TM4JJUnitTestMethodID testMethodId) {
        return failedTests.contains(testMethodId) ?
                "Failed" : "Passed";
    }

    public void generateResultsFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("tm4j_result.json"), tm4jJUnitResults);
    }
}
