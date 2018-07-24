package com.adaptavist.tm4j.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;

public class JUnitTestMethodIDTest {

    @Test
    public void shouldGetDescriptionForRegularJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        JUnitTestMethodID testMethodID = new JUnitTestMethodID(description);

        Assert.assertTrue(testMethodID.getDescription().endsWith("JUnitTestMethodIDTest.shouldGetDescriptionForRegularJUnitTest"));
    }

    @Test
    public void shouldGetDescriptionForParameterizedJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[1]");
        JUnitTestMethodID testMethodID = new JUnitTestMethodID(description);

        Assert.assertTrue(testMethodID.getDescription().endsWith("JUnitTestMethodIDTest.shouldGetDescriptionForParameterizedJUnitTest"));
    }

    @Test
    public void shouldBeEqualForRegularJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        JUnitTestMethodID testMethodID1 = new JUnitTestMethodID(description);
        JUnitTestMethodID testMethodID2 = new JUnitTestMethodID(description);

        Assert.assertTrue(testMethodID1.equals(testMethodID2));
    }

    @Test
    public void shouldNotBeEqualForRegularJUnitTest() {
        Description description1 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        JUnitTestMethodID testMethodID1 = new JUnitTestMethodID(description1);
        Description description2 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest");
        JUnitTestMethodID testMethodID2 = new JUnitTestMethodID(description2);

        Assert.assertFalse(testMethodID1.equals(testMethodID2));
    }

    @Test
    public void shouldBeEqualForParameterizedJUnitTest() {
        Description description1 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[0]");
        JUnitTestMethodID testMethodID1 = new JUnitTestMethodID(description1);
        Description description2 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[1]");
        JUnitTestMethodID testMethodID2 = new JUnitTestMethodID(description2);

        Assert.assertTrue(testMethodID1.equals(testMethodID2));
    }
}
