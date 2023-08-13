import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TechAPITest {
    private static String baseUrl = "http://localhost:8080";
    private static String techEndpoint = "/tech";
    private static String techIdEndpoint = "/tech/5";
    private static String techNotFoundEndpoint = "/tech/6";

    @Test
    public void testGetTechnologies() {
        RequestSpecification request = RestAssured.given();
        Response response = request.get(baseUrl + techEndpoint);
        response.then().statusCode(200);
        response.then().assertThat().contentType("application/json");
        response.then().assertThat().body("length", equalTo(4));
    }

    @Test(dependsOnMethods = { "testGetTechnologies" })
    public void testCreateTechnology() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body("{\"id\":5,\"title\":\"test\",\"link\":\"http://test\"}");
        Response response = request.post(baseUrl + techEndpoint);
        response.then().statusCode(200);
    }

    @Test(dependsOnMethods = { "testCreateTechnology" })
    public void testGetTechnology() {
        RequestSpecification request = RestAssured.given();
        Response response = request.get(baseUrl + techIdEndpoint);
        response.then().statusCode(200);
        response.then().assertThat().contentType("application/json");
        response.then().assertThat().body("id", equalTo(5));
        response.then().assertThat().body("link", equalTo("http://test"));
    }

    @Test(dependsOnMethods = { "testGetTechnology" })
    public void testUpdateTechnology() {
        RequestSpecification request = RestAssured.given();
        request.body("{\"title\":\"test\",\"link\":\"http://test5\"}");
        Response response = request.put(baseUrl + techIdEndpoint);
        response.then().statusCode(200);
    }

    @Test(dependsOnMethods = { "testUpdateTechnology" })
    public void testGetTechnology2() {
        RequestSpecification request = RestAssured.given();
        Response response = request.get(baseUrl + techIdEndpoint);
        response.then().statusCode(200);
        response.then().assertThat().contentType("application/json");
        response.then().assertThat().body("id", equalTo(5));
        response.then().assertThat().body("link", equalTo("http://test5"));
    }

    @Test(dependsOnMethods = { "testGetTechnology2" })
    public void testDeleteTechnology() {
        RequestSpecification request = RestAssured.given();
        Response response = request.delete(baseUrl + techIdEndpoint);
        response.then().statusCode(204);
    }

    @Test(dependsOnMethods = { "testDeleteTechnology" })
    public void testGetTechnology3() {
        RequestSpecification request = RestAssured.given();
        Response response = request.get(baseUrl + techNotFoundEndpoint);
        response.then().statusCode(404);
        response.then().assertThat().body(equalTo("Technology not found id"));
    }
}