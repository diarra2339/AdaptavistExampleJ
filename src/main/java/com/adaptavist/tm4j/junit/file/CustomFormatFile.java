package com.adaptavist.tm4j.junit.file;

import com.adaptavist.tm4j.junit.customformat.CustomFormatContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CustomFormatFile {

    public static final String DEFAULT_TM4J_RESULT_FILE_NAME = "tm4j_result.json";

    public static void generateCustomFormatFile(CustomFormatContainer customFormatContainer) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DEFAULT_TM4J_RESULT_FILE_NAME), customFormatContainer);
    }
}
