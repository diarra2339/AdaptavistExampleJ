package com.adaptavist.tm4j.junit;

import com.adaptavist.tm4j.junit.result.Tm4jExecutionResult;
import com.adaptavist.tm4j.junit.result.Tm4jJUnitResults;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;

import java.io.File;
import java.io.IOException;

public class Tm4JJUnitResultsFileBuilderTest {

    @Test
    public void shouldCreateFileWithEmptyResult() throws IOException {
        Tm4jJUnitResultsFileBuilder fileBuilder = new Tm4jJUnitResultsFileBuilder();
        fileBuilder.generateResultsFile();

        Tm4jJUnitResults tm4jJUnitResults = getTm4jJUnitResults();
        Assert.assertTrue(tm4jJUnitResults.getResults().isEmpty());
    }

    @Test
    public void shouldHaveOnePassedResultForEachTestCaseKeyWhenThereWasNoFail() throws IOException {
        Tm4jJUnitResultsFileBuilder fileBuilder = new Tm4jJUnitResultsFileBuilder();

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey");
        fileBuilder.registerResult("JQA-T1", new TM4JJUnitTestMethodID(descriptionJQAT1));

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult");
        fileBuilder.registerResult("JQA-T2", new TM4JJUnitTestMethodID(descriptionJQAT2));

        fileBuilder.generateResultsFile();

        Tm4jJUnitResults tm4jJUnitResults = getTm4jJUnitResults();

        Assert.assertEquals(2, tm4jJUnitResults.getResults().size());
        tm4jJUnitResults.getResults().forEach(result -> Assert.assertEquals(Tm4jJUnitResultsFileBuilder.PASSED, result.getResult()));
    }

    @Test
    public void shouldHaveOnePassedAndOneFailedResultsWhenThereWasSomeFail() throws IOException {
        Tm4jJUnitResultsFileBuilder fileBuilder = new Tm4jJUnitResultsFileBuilder();

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey");
        fileBuilder.registerResult("JQA-T1", new TM4JJUnitTestMethodID(descriptionJQAT1));

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult");
        TM4JJUnitTestMethodID failedTestMethodId = new TM4JJUnitTestMethodID(descriptionJQAT2);
        fileBuilder.registerFailure(failedTestMethodId);
        fileBuilder.registerResult("JQA-T2", failedTestMethodId);

        fileBuilder.generateResultsFile();

        Tm4jJUnitResults tm4jJUnitResults = getTm4jJUnitResults();

        Assert.assertEquals(2, tm4jJUnitResults.getResults().size());

        Tm4jExecutionResult jqat1Result = getResult(tm4jJUnitResults, "JQA-T1");
        Assert.assertEquals(Tm4jJUnitResultsFileBuilder.PASSED, jqat1Result.getResult());

        Tm4jExecutionResult jqat2Result = getResult(tm4jJUnitResults, "JQA-T2");
        Assert.assertEquals(Tm4jJUnitResultsFileBuilder.FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveMultipleResultsForTheSameTestCaseKeyWhenTheyAreAnnotatedInDifferentTestMethods() throws IOException {
        Tm4jJUnitResultsFileBuilder fileBuilder = new Tm4jJUnitResultsFileBuilder();

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey");
        TM4JJUnitTestMethodID passedTestMethodId = new TM4JJUnitTestMethodID(descriptionJQAT1);
        fileBuilder.registerResult("JQA-T1", passedTestMethodId);

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), "shouldCreateFileWithEmptyResult");
        TM4JJUnitTestMethodID failedTestMethodId = new TM4JJUnitTestMethodID(descriptionJQAT2);
        fileBuilder.registerFailure(failedTestMethodId);
        fileBuilder.registerResult("JQA-T1", failedTestMethodId);

        fileBuilder.generateResultsFile();

        Tm4jJUnitResults tm4jJUnitResults = getTm4jJUnitResults();

        Assert.assertEquals(2, tm4jJUnitResults.getResults().size());


        Tm4jExecutionResult jqat1Result = getResult(tm4jJUnitResults, passedTestMethodId);
        Assert.assertEquals("JQA-T1", jqat1Result.getTestCaseKey());
        Assert.assertEquals(Tm4jJUnitResultsFileBuilder.PASSED, jqat1Result.getResult());

        Tm4jExecutionResult jqat2Result = getResult(tm4jJUnitResults, failedTestMethodId);
        Assert.assertEquals("JQA-T1", jqat2Result.getTestCaseKey());
        Assert.assertEquals(Tm4jJUnitResultsFileBuilder.FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveOnlyOneResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTest() throws IOException {
        Tm4jJUnitResultsFileBuilder fileBuilder = new Tm4jJUnitResultsFileBuilder();

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[0]");
        fileBuilder.registerResult("JQA-T1", new TM4JJUnitTestMethodID(descriptionParam1));

        Description descriptionParam2 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[1]");
        fileBuilder.registerResult("JQA-T1", new TM4JJUnitTestMethodID(descriptionParam2));

        fileBuilder.generateResultsFile();

        Tm4jJUnitResults tm4jJUnitResults = getTm4jJUnitResults();
        Assert.assertEquals(1, tm4jJUnitResults.getResults().size());

        Assert.assertEquals(Tm4jJUnitResultsFileBuilder.PASSED, tm4jJUnitResults.getResults().get(0).getResult());
    }

    @Test
    public void shouldHaveFailedResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTestAndOneOfThemHaveFailed() throws IOException {
        Tm4jJUnitResultsFileBuilder fileBuilder = new Tm4jJUnitResultsFileBuilder();

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[0]");
        TM4JJUnitTestMethodID failedTestMethodId = new TM4JJUnitTestMethodID(descriptionParam1);
        fileBuilder.registerFailure(failedTestMethodId);
        fileBuilder.registerResult("JQA-T1", failedTestMethodId);

        Description descriptionParam2 = Description.createTestDescription(this.getClass(), "shouldHaveOneResultForEachTestCaseKey[1]");
        fileBuilder.registerResult("JQA-T1", new TM4JJUnitTestMethodID(descriptionParam2));

        fileBuilder.generateResultsFile();

        Tm4jJUnitResults tm4jJUnitResults = getTm4jJUnitResults();
        Assert.assertEquals(1, tm4jJUnitResults.getResults().size());

        Assert.assertEquals(Tm4jJUnitResultsFileBuilder.FAILED, tm4jJUnitResults.getResults().get(0).getResult());
    }

    private Tm4jExecutionResult getResult(Tm4jJUnitResults tm4jJUnitResults, String testCaseKey) {
        return tm4jJUnitResults.getResults()
                .stream()
                .filter(r -> r.getTestCaseKey().equals(testCaseKey))
                .findFirst()
                .get();
    }

    private Tm4jExecutionResult getResult(Tm4jJUnitResults tm4jJUnitResults, TM4JJUnitTestMethodID testMethodID) {
        return tm4jJUnitResults.getResults()
                .stream()
                .filter(r -> r.getSource().equals(testMethodID.getDescription()))
                .findFirst()
                .get();
    }

    private Tm4jJUnitResults getTm4jJUnitResults() throws IOException {
        File generatedResultFile = new File(Tm4jJUnitResultsFileBuilder.DEFAULT_TM4J_RESULT_FILE_NAME);
        return new ObjectMapper().readValue(generatedResultFile, Tm4jJUnitResults.class);
    }
}
