package com.example;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestNGGenerator {
    public static void main(String[] args) throws IOException {
        // Read the TechAPI.json file
        String json = new String(Files.readAllBytes(Paths.get("C:\\Github Project\\TechAPI.json")));

        // Parse the JSON and extract the request information
        JSONObject collection = new JSONObject(json);
        JSONArray items = collection.getJSONArray("item");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String name = item.getString("name");
            JSONObject request = item.getJSONObject("request");
            String method = request.getString("method");
            String url = request.getJSONObject("url").getString("raw");
            JSONArray events = item.getJSONArray("event");
            String script = null;
            for (int j = 0; j < events.length(); j++) {
                JSONObject event = events.getJSONObject(j);
                if (event.getString("listen").equals("test")) {
                    script = event.getJSONObject("script").get("exec").toString();
                    break;
                }
            }

            // Create a new TestNG class with a unique name based on the request name
            String className = name.replaceAll("\\W+", "") + "Test";
            String packageName = "com.testcases";
            StringBuilder sb = new StringBuilder();
            sb.append("package " + packageName + ";\n\n");
            sb.append("import org.testng.annotations.Test;\n");
            sb.append("import io.restassured.RestAssured;\n");
            sb.append("import io.restassured.response.Response;\n\n");
            sb.append("public class " + className + " {\n");
            sb.append("    @Test\n");
            sb.append("    public void test() {\n");
            sb.append("        Response response = RestAssured." + method.toLowerCase() + "(\"" + url + "\");\n");
            sb.append(script + "\n");
            sb.append("    }\n");
            sb.append("}\n");

            // Save the TestNG class to a file with the same name as the class
            String fileName = "src\\test\\java\\com\\testcases\\" + className + ".java";
            Files.write(Paths.get(fileName), sb.toString().getBytes());
        }
    }
}