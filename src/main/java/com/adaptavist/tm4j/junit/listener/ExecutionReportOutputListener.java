package com.adaptavist.tm4j.junit.listener;

import com.adaptavist.tm4j.junit.JUnitTestMethodID;
import com.adaptavist.tm4j.junit.ExecutionReportOutputFileBuilder;
import com.adaptavist.tm4j.junit.annotation.TestCaseKey;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class ExecutionReportOutputListener extends RunListener {

    private ExecutionReportOutputFileBuilder executionReportOutputFileBuilder;

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        executionReportOutputFileBuilder = new ExecutionReportOutputFileBuilder();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        TestCaseKey annotation = failure.getDescription().getAnnotation(TestCaseKey.class);

        if (annotation != null) {
            JUnitTestMethodID testMethodId = new JUnitTestMethodID(failure.getDescription());
            executionReportOutputFileBuilder.registerFailure(testMethodId);
        }
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);

        TestCaseKey annotation = description.getAnnotation(TestCaseKey.class);

        String testCaseKey = annotation != null ? annotation.value() : null;

        JUnitTestMethodID testMethodId = new JUnitTestMethodID(description);
        executionReportOutputFileBuilder.registerResult(testCaseKey, testMethodId);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        executionReportOutputFileBuilder.generateResultsFile();
    }
}
