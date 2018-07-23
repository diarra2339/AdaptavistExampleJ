package com.adaptavist.tm4j.junit.listener;

import com.adaptavist.tm4j.junit.TM4JJUnitTestMethodID;
import com.adaptavist.tm4j.junit.Tm4jJUnitResultsBuilder;
import com.adaptavist.tm4j.junit.annotation.TestCaseKey;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class Tm4jJUnitListener extends RunListener {

    private Tm4jJUnitResultsBuilder tm4jJUnitResultsBuilder;

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        tm4jJUnitResultsBuilder = new Tm4jJUnitResultsBuilder();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        TestCaseKey annotation = failure.getDescription().getAnnotation(TestCaseKey.class);

        if (annotation != null) {
            TM4JJUnitTestMethodID testMethodId = new TM4JJUnitTestMethodID(failure.getDescription());
            tm4jJUnitResultsBuilder.registerFailure(testMethodId);
        }
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);

        TestCaseKey annotation = description.getAnnotation(TestCaseKey.class);

        if (annotation != null) {
            String testCaseKey = annotation.value();
            TM4JJUnitTestMethodID testMethodId = new TM4JJUnitTestMethodID(description);
            tm4jJUnitResultsBuilder.registerResult(testCaseKey, testMethodId);
        }
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        tm4jJUnitResultsBuilder.generateResultsFile();
    }
}
