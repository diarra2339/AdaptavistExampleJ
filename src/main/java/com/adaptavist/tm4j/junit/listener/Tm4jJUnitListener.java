package com.adaptavist.tm4j.junit.listener;

import com.adaptavist.tm4j.junit.annotation.TestCaseKey;
import com.adaptavist.tm4j.junit.result.Tm4jExecutionResult;
import com.adaptavist.tm4j.junit.result.Tm4jJUnitResults;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tm4jJUnitListener extends RunListener {

    private Tm4jJUnitResults tm4jJUnitResults;
    private List<String> failedTestMethodNames = new ArrayList<String>();

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        tm4jJUnitResults = new Tm4jJUnitResults();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        failedTestMethodNames.add(failure.getDescription().getMethodName());
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);

        TestCaseKey annotation = description.getAnnotation(TestCaseKey.class);

        if (annotation != null) {
            Tm4jExecutionResult executionResult = new Tm4jExecutionResult();

            executionResult.setSource(description.getTestClass().getName() + "." + description.getMethodName());
            executionResult.setTestCaseKey(annotation.value());
            executionResult.setResult(getResultFor(description));
            tm4jJUnitResults.addResult(executionResult);
        }
    }

    private String getResultFor(Description description) {
        return failedTestMethodNames.contains(description.getMethodName()) ?
                "Failed" : "Passed";
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("tm4j_result.json"), tm4jJUnitResults);
    }
}
