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

    //"Project/:id"
    //Get
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

    //Put
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

    //Post
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

    //Post
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

    //Delete
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

    //Head
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

    //Patch
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
    @Test
    public void testSample1() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Put
    @Test
    public void testSample2() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Post
    @Test
    public void testSample3() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Delete
    @Test
    public void testSample4() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Options
    @Test
    public void testSample5() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Head
    @Test
    public void testSample6() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Patch
    @Test
    public void testSample7() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }




    //"Project/:id/categories"
    //Get
    @Test
    public void testSample8() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Put
    @Test
    public void testSample9() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Post
    @Test
    public void testSampleq() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Delete
    @Test
    public void testSamplew() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Options
    @Test
    public void testSamplee() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Head
    @Test
    public void testSampler() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Patch
    @Test
    public void testSamplet() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }




    //"/projects/:id/categories/:id"
    //Get
    @Test
    public void testSampley() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Put
    @Test
    public void testSampleu() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Post
    @Test
    public void testSamplei() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Delete
    @Test
    public void testSampleo() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Options
    @Test
    public void testSamplep() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Head
    @Test
    public void testSamplea() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Patch
    @Test
    public void testSamples() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }






    //"/projects/:id/tasks"
    //Get
    @Test
    public void testSampled() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Put
    @Test
    public void testSamplef() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Post
    @Test
    public void testSampleg() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Delete
    @Test
    public void testSampleh() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Options
    @Test
    public void testSamplej() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Head
    @Test
    public void testSamplek() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Patch
    @Test
    public void testSamplel() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }







    //"/projects/:id/tasks/:id"
    //Get
    @Test
    public void testSamplez() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Put
    @Test
    public void testSamplex() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Post
    @Test
    public void testSamplec() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Delete
    @Test
    public void testSamplev() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Options
    @Test
    public void testSampleb() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Head
    @Test
    public void testSamplen() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }

    //Patch
    @Test
    public void testSamplem() {

        RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            get("/projects").
        then().
            assertThat().
            statusCode(200);
    }
}