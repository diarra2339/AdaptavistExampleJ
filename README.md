 [ ![Download](https://api.bintray.com/packages/avst/TM4J/tm4j-junit-integration/images/download.svg) ](https://bintray.com/avst/TM4J/tm4j-junit-integration/_latestVersion)

# TM4J Junit Integration

This project is a TM4J JUnit Integration which aims to generate a file describing the test execution result for each Test Case.

In order to achieve that, you need to annotate the JUnit methods with ```@TestCase(key = "JQA-T2")``` or ```@TestCase(name = "")```.

JUnit methods which are not annotated with ```@TestCase``` will also be added to the JSON file, but without the Test Case Key property.

JUnit methods which are not annotated with ```@TestCase(name = "")``` will also be added to the JSON file, but without the Test Case Name property.


## Usage

You can have a look at this [TM4J JUnit Integration Example](https://bitbucket.org/Adaptavist/tm4j-junit-integration-example/) repository.

You need to add the dependency to your pom file.

```
<dependencies>
    <dependency>
        <groupId>com.adaptavist</groupId>
        <artifactId>tm4j-junit-integration</artifactId>
        <version>1.0.0</version>
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
                        <value>com.adaptavist.tm4j.junit.ExecutionListener</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```


The next step is to annotate your JUnit tests with ```@TestCase``` or don't annotate at all, if the Test Case doesn't exist yet.

```
public class CalculatorSumTest {

    @Test
    @TestCase(key = "JQA-T1")
    public void sumTwoNumbersAndPass() {
        Calculator calculator = new Calculator();
        assertEquals(1, calculator.sum(1, 2));
    }

    @Test
    @TestCase(key = "JQA-T2")
    public void sumTwoNumbersAndFail() {
        Calculator calculator = new Calculator();
        assertNotEquals(2, calculator.sum(1, 2));
    }
    
    @Test
    public void notMappedToTestCaseAndPass() {
        Calculator calculator = new Calculator();
        assertEquals(1, calculator.sum(1, 2));
    }
    
    @Test
    @TestCase(name = "Mapped to Test Case Name and Pass")
    public void mappedToTestCaseNameAndPass() {
        Calculator calculator = new Calculator();
        assertEquals(1, calculator.sum(1, 2));
    }    

}

```


Now, you can run your tests with ```mvn test``` and the TM4J test execution result file will be generated in the same execution folder.

### tm4j_result.json

```
{
   "executions":[
      {
         "source":"CalculatorSumTest.sumTwoNumbersAndPass",
         "result":"Passed",
         "testCase": {
            "key": "JQA-T1"
         }
      },
      {
        "source":"CalculatorSumTest.sumTwoNumbersAndFail",
        "result":"Failed",
         "testCase": {
            "key": "JQA-T2"
         }
      },
      {
        "source":"CalculatorSumTest.notMappedToTestCaseAndPass",
        "result":"Passed"
      },
      {
        "source":"CalculatorSumTest.mappedToTestCaseNameAndPass",
        "result":"Passed",
         "testCase": {
            "name": "Mapped to Test Case Name and Pass"
         }
      }                  
   ]
}
```

## Support

For any issues or enquires please get in touch with the Test Management for Jira team at Adaptavist using the [support portal](https://productsupport.adaptavist.com/servicedesk/customer/portal/27).