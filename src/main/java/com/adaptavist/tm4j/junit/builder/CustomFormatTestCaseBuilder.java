package com.adaptavist.tm4j.junit.builder;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.adaptavist.tm4j.junit.customformat.CustomFormatTestCase;
import org.junit.runner.Description;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class CustomFormatTestCaseBuilder {
    private CustomFormatTestCase testCase;

    public CustomFormatTestCaseBuilder build(Description description) {
        TestCase testCaseAnnotation = description.getAnnotation(TestCase.class);
        String testCaseKey = testCaseAnnotation != null ? testCaseAnnotation.key() : null;
        String testCaseName = testCaseAnnotation != null ? testCaseAnnotation.name(): null;

        if(testCaseKey != null || testCaseName != null) {
            testCase = new CustomFormatTestCase();
            testCase.setKey(isNotEmpty(testCaseKey) ? testCaseKey : null);
            testCase.setName(isNotEmpty(testCaseName) ? testCaseName : null);
        }

        return this;
    }

    public CustomFormatTestCase getTestCase() {
        return testCase;
    }
}
