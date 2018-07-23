package com.adaptavist.tm4j.junit.listener;

import com.adaptavist.tm4j.junit.TM4JJUnitTestMethodID;
import com.adaptavist.tm4j.junit.Tm4jJUnitResultsFileBuilder;
import com.adaptavist.tm4j.junit.annotation.TestCaseKey;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class Tm4jJUnitListener extends RunListener {

    private Tm4jJUnitResultsFileBuilder tm4JJUnitResultsFileBuilder;

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        tm4JJUnitResultsFileBuilder = new Tm4jJUnitResultsFileBuilder();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        TestCaseKey annotation = failure.getDescription().getAnnotation(TestCaseKey.class);

        if (annotation != null) {
            TM4JJUnitTestMethodID testMethodId = new TM4JJUnitTestMethodID(failure.getDescription());
            tm4JJUnitResultsFileBuilder.registerFailure(testMethodId);
        }
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);

        TestCaseKey annotation = description.getAnnotation(TestCaseKey.class);

        if (annotation != null) {
            String testCaseKey = annotation.value();
            TM4JJUnitTestMethodID testMethodId = new TM4JJUnitTestMethodID(description);
            tm4JJUnitResultsFileBuilder.registerResult(testCaseKey, testMethodId);
        }
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        tm4JJUnitResultsFileBuilder.generateResultsFile();
    }
}
