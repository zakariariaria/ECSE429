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

    
   @Test
    public void testGetAllProjects() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testCreateProject() {

        RestAssured.baseURI = "http://localhost:4567";

        // Send a POST request to /projects with appropriate payload
        String requestBody = "{ \"name\": \"Test Project\", \"description\": \"Test Description\" }";
        //Response response = given().contentType("application/json").body(requestBody).post("/projects");

        // Validate the response code and content
        //assertEquals(201, response.getStatusCode());
        //assertTrue(response.getBody().asString().contains("Created a project"));
    }

}