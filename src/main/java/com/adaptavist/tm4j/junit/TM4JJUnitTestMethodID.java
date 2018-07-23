package com.adaptavist.tm4j.junit;

import org.junit.runner.Description;

public class TM4JJUnitTestMethodID {

    private Description description;

    public TM4JJUnitTestMethodID(Description description) {
        this.description = description;
    }

    public String getDescription() {
        String testClassName = description.getTestClass().getName();
        String methodName = getMethodNameFor(description);
        return testClassName + "." + methodName;
    }

    private String getMethodNameFor(Description description) {
        return description.getMethodName().replaceFirst("\\[[0-9]+\\]$", "");
    }

    @Override
    public boolean equals(Object obj) {
        TM4JJUnitTestMethodID testMethodID = (TM4JJUnitTestMethodID) obj;
        return this.getDescription().equals(testMethodID.getDescription());
    }
}
