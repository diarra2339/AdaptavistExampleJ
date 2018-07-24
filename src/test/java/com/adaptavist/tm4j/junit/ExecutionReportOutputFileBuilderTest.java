package com.adaptavist.tm4j.junit;

import com.adaptavist.tm4j.junit.result.ExecutionResult;
import com.adaptavist.tm4j.junit.result.ExecutionResults;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;

import java.io.File;
import java.io.IOException;

public class ExecutionReportOutputFileBuilderTest {

    @Test
    public void shouldCreateFileWithEmptyResult() throws IOException {
        ExecutionReportOutputFileBuilder fileBuilder = new ExecutionReportOutputFileBuilder();
        fileBuilder.generateResultsFile();

        ExecutionResults executionResults = getTm4jJUnitResults();
        Assert.assertTrue(executionResults.getExecutions().isEmpty());
    }

    @Test
    public void shouldHaveOnePassedResultForEachTestCaseKeyWhenThereWasNoFail() throws IOException {
        ExecutionReportOutputFileBuilder fileBuilder = new ExecutionReportOutputFileBuilder();

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey");
        fileBuilder.registerResult("JQA-T1", new JUnitTestMethodID(descriptionJQAT1));

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult");
        fileBuilder.registerResult("JQA-T2", new JUnitTestMethodID(descriptionJQAT2));

        fileBuilder.generateResultsFile();

        ExecutionResults executionResults = getTm4jJUnitResults();

        Assert.assertEquals(2, executionResults.getExecutions().size());
        executionResults.getExecutions().forEach(result -> Assert.assertEquals(ExecutionReportOutputFileBuilder.PASSED, result.getResult()));
    }

    @Test
    public void shouldHaveOnePassedAndOneFailedResultsWhenThereWasSomeFail() throws IOException {
        ExecutionReportOutputFileBuilder fileBuilder = new ExecutionReportOutputFileBuilder();

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey");
        fileBuilder.registerResult("JQA-T1", new JUnitTestMethodID(descriptionJQAT1));

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult");
        JUnitTestMethodID failedTestMethodId = new JUnitTestMethodID(descriptionJQAT2);
        fileBuilder.registerFailure(failedTestMethodId);
        fileBuilder.registerResult("JQA-T2", failedTestMethodId);

        fileBuilder.generateResultsFile();

        ExecutionResults executionResults = getTm4jJUnitResults();

        Assert.assertEquals(2, executionResults.getExecutions().size());

        ExecutionResult jqat1Result = getResult(executionResults, "JQA-T1");
        Assert.assertEquals(ExecutionReportOutputFileBuilder.PASSED, jqat1Result.getResult());

        ExecutionResult jqat2Result = getResult(executionResults, "JQA-T2");
        Assert.assertEquals(ExecutionReportOutputFileBuilder.FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveMultipleResultsForTheSameTestCaseKeyWhenTheyAreAnnotatedInDifferentTestMethods() throws IOException {
        ExecutionReportOutputFileBuilder fileBuilder = new ExecutionReportOutputFileBuilder();

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey");
        JUnitTestMethodID passedTestMethodId = new JUnitTestMethodID(descriptionJQAT1);
        fileBuilder.registerResult("JQA-T1", passedTestMethodId);

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult");
        JUnitTestMethodID failedTestMethodId = new JUnitTestMethodID(descriptionJQAT2);
        fileBuilder.registerFailure(failedTestMethodId);
        fileBuilder.registerResult("JQA-T1", failedTestMethodId);

        fileBuilder.generateResultsFile();

        ExecutionResults executionResults = getTm4jJUnitResults();

        Assert.assertEquals(2, executionResults.getExecutions().size());


        ExecutionResult jqat1Result = getResult(executionResults, passedTestMethodId);
        Assert.assertEquals("JQA-T1", jqat1Result.getTestCaseKey());
        Assert.assertEquals(ExecutionReportOutputFileBuilder.PASSED, jqat1Result.getResult());

        ExecutionResult jqat2Result = getResult(executionResults, failedTestMethodId);
        Assert.assertEquals("JQA-T1", jqat2Result.getTestCaseKey());
        Assert.assertEquals(ExecutionReportOutputFileBuilder.FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveOnlyOneResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTest() throws IOException {
        ExecutionReportOutputFileBuilder fileBuilder = new ExecutionReportOutputFileBuilder();

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[0]");
        fileBuilder.registerResult("JQA-T1", new JUnitTestMethodID(descriptionParam1));

        Description descriptionParam2 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[1]");
        fileBuilder.registerResult("JQA-T1", new JUnitTestMethodID(descriptionParam2));

        fileBuilder.generateResultsFile();

        ExecutionResults executionResults = getTm4jJUnitResults();
        Assert.assertEquals(1, executionResults.getExecutions().size());

        Assert.assertEquals(ExecutionReportOutputFileBuilder.PASSED, executionResults.getExecutions().get(0).getResult());
    }

    @Test
    public void shouldHaveFailedResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTestAndOneOfThemHaveFailed() throws IOException {
        ExecutionReportOutputFileBuilder fileBuilder = new ExecutionReportOutputFileBuilder();

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[0]");
        JUnitTestMethodID failedTestMethodId = new JUnitTestMethodID(descriptionParam1);
        fileBuilder.registerFailure(failedTestMethodId);
        fileBuilder.registerResult("JQA-T1", failedTestMethodId);

        Description descriptionParam2 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[1]");
        fileBuilder.registerResult("JQA-T1", new JUnitTestMethodID(descriptionParam2));

        fileBuilder.generateResultsFile();

        ExecutionResults executionResults = getTm4jJUnitResults();
        Assert.assertEquals(1, executionResults.getExecutions().size());

        Assert.assertEquals(ExecutionReportOutputFileBuilder.FAILED, executionResults.getExecutions().get(0).getResult());
    }

    private ExecutionResult getResult(ExecutionResults executionResults, String testCaseKey) {
        return executionResults.getExecutions()
                .stream()
                .filter(r -> r.getTestCaseKey().equals(testCaseKey))
                .findFirst()
                .get();
    }

    private ExecutionResult getResult(ExecutionResults executionResults, JUnitTestMethodID testMethodID) {
        return executionResults.getExecutions()
                .stream()
                .filter(r -> r.getSource().equals(testMethodID.getDescription()))
                .findFirst()
                .get();
    }

    private ExecutionResults getTm4jJUnitResults() throws IOException {
        File generatedResultFile = new File(ExecutionReportOutputFileBuilder.DEFAULT_TM4J_RESULT_FILE_NAME);
        return new ObjectMapper().readValue(generatedResultFile, ExecutionResults.class);
    }
}
