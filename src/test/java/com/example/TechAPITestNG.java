package com.example;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TechAPITestNG {

    @Test
    public void testGetTechnologies() {
        given()
                .when()
                .get("http://localhost:8080/tech")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", equalTo(4));
    }

    @Test(dependsOnMethods = {"testGetTechnologies"})
    public void testCreateTechnology() {
        given()
                .header("Content-Type", "application/json")
                .body("{\"id\":5,\"title\":\"test\",\"link\":\"http://test\"}")
                .when()
                .post("http://localhost:8080/tech")
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = {"testCreateTechnology"})
    public void testGetTechnology() {
        given()
                .when()
                .get("http://localhost:8080/tech/5")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(5))
                .body("link", equalTo("http://test"));
    }

    @Test(dependsOnMethods = {"testGetTechnology"})
    public void testUpdateTechnology() {
        given()
                .body("{\"title\":\"test\",\"link\":\"http://test5\"}")
                .when().contentType("application/json")
                .put("http://localhost:8080/tech/5")
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = {"testUpdateTechnology"})
    public void testGetTechnology2() {
        given()
                .when()
                .get("http://localhost:8080/tech/5")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(5))
                .body("link", equalTo("http://test5"));
    }

    @Test(dependsOnMethods = {"testGetTechnology2"})
    public void testDeleteTechnology() {
        given()
                .when()
                .delete("http://localhost:8080/tech/5")
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = {"testDeleteTechnology"})
    public void testGetTechnology3() {
        given()
                .when()
                .get("http://localhost:8080/tech/5")
                .then()
                .statusCode(404)
                .body(containsString("Technology not found id"));
        //verify body contains "Technology not found id"
    }
}
