package com.adaptavist.tm4j.junit.builder;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.adaptavist.tm4j.junit.customformat.CustomFormatTestCase;
import org.junit.runner.Description;

public class CustomFormatTestCaseBuilder {
    private CustomFormatTestCase testCase;

    public CustomFormatTestCaseBuilder build(Description description) {
        TestCase testCaseAnnotation = description.getAnnotation(TestCase.class);
        String testCaseKey = testCaseAnnotation != null ? testCaseAnnotation.key() : null;
        String testCaseName = testCaseAnnotation != null ? testCaseAnnotation.name(): null;

        if(testCaseKey != null || testCaseName != null) {
            testCase = new CustomFormatTestCase();
            testCase.setKey(testCaseKey);
            testCase.setName(testCaseName);
        }

        return this;
    }

    public CustomFormatTestCase getTestCase() {
        return testCase;
    }
}
