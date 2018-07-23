package com.adaptavist.tm4j.junit.result;

import com.adaptavist.tm4j.junit.TM4JJUnitTestMethodID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Tm4jExecutionResult> getRegisteredResultFor(TM4JJUnitTestMethodID testMethodId) {
        return results
                .stream()
                .filter(r -> r.getSource().equals(testMethodId.getDescription()))
                .findFirst();
    }
}
