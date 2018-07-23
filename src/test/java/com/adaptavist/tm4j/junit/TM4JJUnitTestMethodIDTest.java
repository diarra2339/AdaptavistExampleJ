package com.adaptavist.tm4j.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;

public class TM4JJUnitTestMethodIDTest {

    @Test
    public void shouldGetDescriptionForRegularJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        TM4JJUnitTestMethodID testMethodID = new TM4JJUnitTestMethodID(description);

        Assert.assertTrue(testMethodID.getDescription().endsWith("TM4JJUnitTestMethodIDTest.shouldGetDescriptionForRegularJUnitTest"));
    }

    @Test
    public void shouldGetDescriptionForParameterizedJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[1]");
        TM4JJUnitTestMethodID testMethodID = new TM4JJUnitTestMethodID(description);

        Assert.assertTrue(testMethodID.getDescription().endsWith("TM4JJUnitTestMethodIDTest.shouldGetDescriptionForParameterizedJUnitTest"));
    }

    @Test
    public void shouldBeEqualForRegularJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        TM4JJUnitTestMethodID testMethodID1 = new TM4JJUnitTestMethodID(description);
        TM4JJUnitTestMethodID testMethodID2 = new TM4JJUnitTestMethodID(description);

        Assert.assertTrue(testMethodID1.equals(testMethodID2));
    }

    @Test
    public void shouldNotBeEqualForRegularJUnitTest() {
        Description description1 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        TM4JJUnitTestMethodID testMethodID1 = new TM4JJUnitTestMethodID(description1);
        Description description2 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest");
        TM4JJUnitTestMethodID testMethodID2 = new TM4JJUnitTestMethodID(description2);

        Assert.assertFalse(testMethodID1.equals(testMethodID2));
    }

    @Test
    public void shouldBeEqualForParameterizedJUnitTest() {
        Description description1 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[0]");
        TM4JJUnitTestMethodID testMethodID1 = new TM4JJUnitTestMethodID(description1);
        Description description2 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[1]");
        TM4JJUnitTestMethodID testMethodID2 = new TM4JJUnitTestMethodID(description2);

        Assert.assertTrue(testMethodID1.equals(testMethodID2));
    }
}
