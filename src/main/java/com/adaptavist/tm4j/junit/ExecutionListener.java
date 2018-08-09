package com.adaptavist.tm4j.junit;

import com.adaptavist.tm4j.junit.builder.CustomFormatContainerBuilder;
import com.adaptavist.tm4j.junit.decorator.TestDescriptionDecorator;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import static com.adaptavist.tm4j.junit.file.CustomFormatFile.generateCustomFormatFile;

public class ExecutionListener extends RunListener {

    private CustomFormatContainerBuilder customFormatContainerBuilder;

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);

        customFormatContainerBuilder = new CustomFormatContainerBuilder();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);

        customFormatContainerBuilder.registerFailure(new TestDescriptionDecorator(failure.getDescription()));
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);

        customFormatContainerBuilder.registerFinished(new TestDescriptionDecorator(description));
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);

        generateCustomFormatFile(customFormatContainerBuilder.getCustomFormatContainer());
    }
}
