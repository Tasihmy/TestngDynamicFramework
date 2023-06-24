package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MyApiTest1 {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test(groups = {"testGet"})
    public void testGet() {
        RestAssured.baseURI = BASE_URL;

        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .response();

        response.then()
                .body("[0].userId", equalTo(1))
                .body("[0].id", equalTo(1))
                .body("[0].title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
                .body("[0].body", equalTo("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"));
    }
    @Test(dependsOnMethods = {"testGet"},groups = {"testGet"})
    public void testGetValid() {
        System.out.println("testGetValid");
    }
    @Test(groups = {"testPost"})
    public void testPost() {
        RestAssured.baseURI = BASE_URL;

        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .response();

        response.then()
                .body("[0].userId", equalTo(1))
                .body("[0].id", equalTo(1))
                .body("[0].title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
                .body("[0].body", equalTo("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"));
    }
    @Test(dependsOnMethods = {"testPost"},groups = {"testPost"})
    public void testPostValid() {
        System.out.println("testPostValid");
    }
}