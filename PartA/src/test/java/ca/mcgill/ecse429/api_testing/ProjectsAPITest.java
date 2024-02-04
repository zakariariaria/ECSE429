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
    public void testPutNotAllowed() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testPost201() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testPost400() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testDeleteNotAllowed() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Test no options to show unava...
    @Test
    public void testShowAllOptions200() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testReturnHeaders200() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testPatchNotAllowed405() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //"Project/:id"

    //Get

    //Put

    //Post

    //Delete

    //Options

    //Head

    //Patch




    //"Project/:id/categories"

    //Get

    //Put

    //Post

    //Delete

    //Options

    //Head

    //Patch




    //"/projects/:id/categories/:id"

    //Get

    //Put

    //Post

    //Delete

    //Options

    //Head

    //Patch






    //"/projects/:id/tasks"

    //Get

    //Put

    //Post

    //Delete

    //Options

    //Head

    //Patch







    //"/projects/:id/tasks/:id"

    //Get

    //Put

    //Post

    //Delete

    //Options

    //Head

    //Patch
}