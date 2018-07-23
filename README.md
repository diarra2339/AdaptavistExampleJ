# TM4J Junit Integration

This project is a TM4J JUnit Integration which aims to generate a file describing the test execution result for each Test Case.

In order to achieve that, you need to annotate the JUnit methods with ```@TestCaseKey```.


## Usage

You can have a look at this [TM4J JUnit Integration Example](https://stash.adaptavist.com/projects/ATM/repos/tm4j-junit-integration-example/browse) repository.

You need to add the dependency to your pom file.

```
<dependencies>
    <dependency>
        <groupId>com.adaptavist</groupId>
        <artifactId>tm4j-junit-integration</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Also, you'll need to register the TM4J JUnit Listener.

```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.22.0</version>
            <configuration>
                <properties>
                    <property>
                        <name>listener</name>
                        <value>com.adaptavist.tm4j.junit.listener.Tm4jJUnitListener</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```


The next step is to annotate your JUnit tests with ```@TestCaseKey```

```
public class CalculatorSumTest {

    @Test
    @TestCaseKey("JQA-T1")
    public void sumTwoNumbersAndPass() {
        Calculator calculator = new Calculator();
        assertEquals(1, calculator.sum(1, 2));
    }

    @Test
    @TestCaseKey("JQA-T2")
    public void sumTwoNumbersAndFail() {
        Calculator calculator = new Calculator();
        assertNotEquals(2, calculator.sum(1, 2));
    }

}

```


Now, you can run your tests with ```mvn test``` and the TM4J test execution result file will be generated in the same execution folder.

### tm4j_result.json

```
{
   "results":[
      {
         "source":"CalculatorSumTest.sumTwoNumbersAndPass",
         "testCaseKey":"JQA-T1",
         "result":"Passed"
      },
      {
        "source":"CalculatorSumTest.sumTwoNumbersAndFail",
        "testCaseKey":"JQA-T2",
        "result":"Failed"
      }
   ]
}
```