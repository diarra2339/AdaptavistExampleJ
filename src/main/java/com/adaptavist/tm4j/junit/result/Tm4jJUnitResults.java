package com.adaptavist.tm4j.junit.result;

import java.util.ArrayList;
import java.util.List;

public class Tm4jJUnitResults {

    private List<Tm4jExecutionResult> results = new ArrayList<Tm4jExecutionResult>();

    public List<Tm4jExecutionResult> getResults() {
        return results;
    }

    public void setResults(List<Tm4jExecutionResult> results) {
        this.results = results;
    }

    public void addResult(Tm4jExecutionResult executionResult) {
        results.add(executionResult);
    }
}
