package ca.mcgill.ecse429.api_testing;

import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.Random.class)

public class ProjectsAPITest {

    private String urlProject = "http://localhost:4567/projects";

    //Projects Data
     @Test
    public void testCreateProjectJson() {
        given()
            .contentType("application/json")
            .body("{\"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"some description\"}")
        .when()
            .post(urlProject)
        .then()
            .statusCode(200)
            .body("title", equalTo("sometitle"))
            .body("completed", equalTo(true))
            .body("active", equalTo(false))
            .body("description", equalTo("some description"));
    }

    @Test
    public void testCreateProjectBoolAsString() {
        given()
            .contentType("application/json")
            .body("{\"title\":\"sometitle\",\"completed\":\"True\",\"active\":\"False\",\"description\":\"some description\"}")
        .when()
            .post(urlProject)
        .then()
            .statusCode(200)
            .body("title", equalTo("sometitle"))
            .body("completed", equalTo(true))
            .body("active", equalTo(false))
            .body("description", equalTo("some description"));
    }

    @Test
    public void testCreateProjectXml() {
        given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>t enim ad minim veni</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>incididunt ut labora</title>\n</project>")
        .when()
            .post(urlProject)
        .then()
            .statusCode(200)
            .body("title", equalTo("incididunt ut labora"))
            .body("completed", equalTo(true))
            .body("active", equalTo(true))
            .body("description", equalTo("t enim ad minim veni"));
    }

    @Test
    public void testCreateProjectXmlInvalidId() {
        given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>t enim ad minim veni</description>\n  <id>100</id>\n  <completed>true</completed>\n  <title>incididunt ut labora</title>\n</project>")
        .when()
            .post(urlProject)
        .then()
            .statusCode(400); // Adjust the expected status code as needed
    }

    @Test
    public void testCreateProjectXmlNullId() {
        given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>t enim ad minim veni</description>\n  <id>null</id>\n  <completed>true</completed>\n  <title>incididunt ut labora</title>\n</project>")
        .when()
            .post(urlProject)
        .then()
            .statusCode(400); // Adjust the expected status code as needed
    }
    
    //Edge Cases
}