package com.adaptavist.tm4j.junit;

import org.junit.runner.Description;

public class JUnitTestMethodID {

    public static final String PARAMETERIZED_TEST_NAME_PATTERN = "\\[[0-9]+\\]$";
    private Description description;

    public JUnitTestMethodID(Description description) {
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
        if (obj instanceof JUnitTestMethodID) {
            JUnitTestMethodID testMethodID = (JUnitTestMethodID) obj;
            return this.getDescription().equals(testMethodID.getDescription());
        }

        return false;
    }
}
