package com.adaptavist.tm4j.junit;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.adaptavist.tm4j.junit.builder.CustomFormatContainerBuilder;
import com.adaptavist.tm4j.junit.decorator.TestDescriptionDecorator;
import com.adaptavist.tm4j.junit.customformat.CustomFormatExecution;
import com.adaptavist.tm4j.junit.customformat.CustomFormatContainer;
import com.adaptavist.tm4j.junit.file.CustomFormatFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;

import static com.adaptavist.tm4j.junit.customformat.CustomFormatConstants.FAILED;
import static com.adaptavist.tm4j.junit.customformat.CustomFormatConstants.PASSED;
import static com.adaptavist.tm4j.junit.file.CustomFormatFile.generateCustomFormatFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExecutionListenerTest {

    @Test
    public void shouldCreateFileWithEmptyResult() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);
        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();
        assertTrue(customFormatContainer.getExecutions().isEmpty());
    }

    @Test
    public void shouldNotSetNameWhenItIsNotSetInTestCaseAnnotation() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        TestCase testCaseKey = testCaseAnnotationWithKey("JQA-T1");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldPassWithoutATestCaseKey", testCaseKey);
        executionListener.testFinished(descriptionJQAT1);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();

        assertEquals(1, customFormatContainer.getExecutions().size());
        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertNull(jqat1Result.getTestCase().getName());
    }

    @Test
    public void shouldNotSetKeyWhenItIsNotSetInTestCaseAnnotation() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        TestCase testCaseName = testCaseAnnotationWithName("Successful login");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldPassWithoutATestCaseKey", testCaseName);
        executionListener.testFinished(descriptionJQAT1);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();

        assertEquals(1, customFormatContainer.getExecutions().size());
        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertNull(jqat1Result.getTestCase().getKey());
    }

    @Test
    public void shouldHaveOnePassedAndOneFailResultForEachNotMappedToTestCaseKeyMethod() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldPassWithoutATestCaseKey");
        executionListener.testFinished(descriptionJQAT1);

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldFailWithoutATestCaseKey");
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = customFormatContainer.getExecutions().get(1);
        assertEquals(FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveOnePassedResultForEachTestCaseKeyWhenThereWasNoFail() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        TestCase testCaseKey1 = testCaseAnnotationWithKey("JQA-T1");
        TestCase testCaseKey2 = testCaseAnnotationWithKey("JQA-T2");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey", testCaseKey1);
        executionListener.testFinished(descriptionJQAT1);

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult", testCaseKey2);
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());
        customFormatContainer.getExecutions().forEach(result -> assertEquals(PASSED, result.getResult()));
    }

    @Test
    public void shouldHaveOnePassedResultForEachTestCaseNameWhenThereWasNoFail() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        TestCase testCase1 = testCaseAnnotation("JQA-T1", "Have one Result For each Test Case Name");
        TestCase testCase2 = testCaseAnnotation("JQA-T2", "Create File With Empty Result");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey", testCase1);
        executionListener.testFinished(descriptionJQAT1);

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult", testCase2);
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = getResultByTestCaseName(customFormatContainer, "Have one Result For each Test Case Name");
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = getResultByTestCaseName(customFormatContainer, "Create File With Empty Result");
        assertEquals(FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveOnePassedAndOneFailedResultsWhenThereWasSomeFail() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        TestCase testCaseKey1 = testCaseAnnotationWithKey("JQA-T1");
        TestCase testCaseKey2 = testCaseAnnotationWithKey("JQA-T2");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey", testCaseKey1);
        executionListener.testFinished(descriptionJQAT1);

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult", testCaseKey2);
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = getResultByTestCaseKey(customFormatContainer, "JQA-T1");
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = getResultByTestCaseKey(customFormatContainer, "JQA-T2");
        assertEquals(FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveMultipleResultsForTheSameTestCaseKeyWhenTheyAreAnnotatedInDifferentTestMethods() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        TestCase testCaseKey1 = testCaseAnnotationWithKey("JQA-T1");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey", testCaseKey1);
        executionListener.testFinished(descriptionJQAT1);

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult", testCaseKey1);
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = getExecutionByDescription(customFormatContainer, descriptionJQAT1);
        assertEquals("JQA-T1", jqat1Result.getTestCase().getKey());
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = getExecutionByDescription(customFormatContainer, descriptionJQAT2);
        assertEquals("JQA-T1", jqat2Result.getTestCase().getKey());
        assertEquals(FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveOnlyOneResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTest() throws IOException {
        CustomFormatContainerBuilder customFormatContainerBuilder = new CustomFormatContainerBuilder();

        TestCase testCaseKey1 = testCaseAnnotationWithKey("JQA-T1");
        TestCase testCaseKey2 = testCaseAnnotationWithKey("JQA-T2");

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[0]", testCaseKey1);
        customFormatContainerBuilder.registerFinished(new TestDescriptionDecorator(descriptionParam1));

        Description descriptionParam2 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[1]", testCaseKey2);
        customFormatContainerBuilder.registerFinished(new TestDescriptionDecorator(descriptionParam2));

        generateCustomFormatFile(customFormatContainerBuilder.getCustomFormatContainer());

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();
        assertEquals(1, customFormatContainer.getExecutions().size());

        assertEquals(PASSED, customFormatContainer.getExecutions().get(0).getResult());
    }

    @Test
    public void shouldHaveFailedResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTestAndOneOfThemHaveFailed() throws IOException {
        CustomFormatContainerBuilder customFormatContainerBuilder = new CustomFormatContainerBuilder();

        TestCase testCaseKey1 = testCaseAnnotationWithKey("JQA-T1");

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[0]", testCaseKey1);
        TestDescriptionDecorator failedTestMethodId = new TestDescriptionDecorator(descriptionParam1);
        customFormatContainerBuilder.registerFailure(failedTestMethodId);
        customFormatContainerBuilder.registerFinished(failedTestMethodId);

        Description descriptionParam2 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[1]", testCaseKey1);
        customFormatContainerBuilder.registerFinished(new TestDescriptionDecorator(descriptionParam2));

        generateCustomFormatFile(customFormatContainerBuilder.getCustomFormatContainer());

        CustomFormatContainer customFormatContainer = getTm4jJUnitResults();
        assertEquals(1, customFormatContainer.getExecutions().size());

        assertEquals(FAILED, customFormatContainer.getExecutions().get(0).getResult());
    }

    private CustomFormatExecution getResultByTestCaseKey(CustomFormatContainer customFormatContainer, String testCaseKey) {
        return customFormatContainer.getExecutions()
                .stream()
                .filter(r -> r.getTestCase().getKey().equals(testCaseKey))
                .findFirst()
                .get();
    }

    private CustomFormatExecution getResultByTestCaseName(CustomFormatContainer customFormatContainer, String testCaseName) {
        return customFormatContainer.getExecutions()
                .stream()
                .filter(r -> r.getTestCase().getName().equals(testCaseName))
                .findFirst()
                .get();
    }

    private CustomFormatExecution getExecutionByDescription(CustomFormatContainer customFormatContainer, Description testMethodID) {
        return customFormatContainer.getExecutions()
                .stream()
                .filter(r -> r.getSource().equals(new TestDescriptionDecorator(testMethodID).getSource()))
                .findFirst()
                .get();
    }

    private CustomFormatContainer getTm4jJUnitResults() throws IOException {
        File generatedResultFile = new File(CustomFormatFile.DEFAULT_TM4J_RESULT_FILE_NAME);
        return new ObjectMapper().readValue(generatedResultFile, CustomFormatContainer.class);
    }

    private TestCase testCaseAnnotationWithName(String name) {
        return testCaseAnnotation(null, name);
    }

    private TestCase testCaseAnnotationWithKey(String key) {
        return testCaseAnnotation(key, null);
    }

    private TestCase testCaseAnnotation(String key, String name) {
        return new TestCase()
        {
            @Override
            public String key() {
                return key;
            }

            @Override
            public String name()
            {
                return name;
            }

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return TestCase.class;
            }
        };
    }
}
