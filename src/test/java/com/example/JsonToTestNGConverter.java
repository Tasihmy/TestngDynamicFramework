package com.example;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonToTestNGConverter {
    public static void main(String[] args) throws IOException {
        // Read the contents of the JSON file into a string variable.
        String json = readFileToString("C:\\Github Project\\TechAPI.json");

        // Parse the JSON string into a JSON object using Jackson.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // Extract the necessary information from the JSON object and use it to generate the TestNG class.
        String className = "TechAPITest";
        String packageName = "com.example.tests";
        String testMethodName = "testGetTechnologies";
        String testUrl = "http://localhost:8080/tech";

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import org.testng.annotations.Test;\n\n");
        sb.append("public class ").append(className).append(" {\n\n");
        sb.append("    @Test\n");
        sb.append("    public void ").append(testMethodName).append("() {\n");
        sb.append("        // Test code goes here.\n");
        sb.append("    }\n\n");
        sb.append("}");

        // Write the generated TestNG class to a file.
        String outputFilePath = "src\\test\\java\\com\\testcases\\TechAPITest.java";
        writeStringToFile(outputFilePath, sb.toString());
    }

    private static String readFileToString(String filePath) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(new File(filePath).toPath()));
    }

    private static void writeStringToFile(String filePath, String content) throws IOException {
        java.nio.file.Files.write(new File(filePath).toPath(), content.getBytes());
    }
}