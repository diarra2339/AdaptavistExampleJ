package com.adaptavist.tm4j.junit;

import org.junit.runner.Description;

public class TM4JJUnitTestMethodID {

    public static final String PARAMETERIZED_TEST_NAME_PATTERN = "\\[[0-9]+\\]$";
    private Description description;

    public TM4JJUnitTestMethodID(Description description) {
        this.description = description;
    }

    public String getDescription() {
        String testClassName = description.getTestClass().getName();
        String methodName = getMethodName();
        return testClassName + "." + methodName;
    }

    private String getMethodName() {
        return description.getMethodName().replaceFirst(PARAMETERIZED_TEST_NAME_PATTERN, "");
    }

    @Override
    public boolean equals(Object obj) {
        TM4JJUnitTestMethodID testMethodID = (TM4JJUnitTestMethodID) obj;
        return this.getDescription().equals(testMethodID.getDescription());
    }
}
