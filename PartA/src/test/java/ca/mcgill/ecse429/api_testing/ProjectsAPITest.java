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
            .body("{\"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\"}")
        .when()
            .post(urlProject)
        .then()
            .statusCode(201)
            .body("title", equalTo("sometitle"))
            .body("completed", equalTo(true))
            .body("active", equalTo(false))
            .body("description", equalTo("qwertyuiop"));
    }


    @Test
    public void testCreateProjectXml() {
        given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject)
        .then()
            //.statusCode(200)
            .body("title", equalTo("qwertyuiop"))
            .body("completed", equalTo(true))
            .body("active", equalTo(true))
            .body("description", equalTo("qwertyuiop"));
    }

    @Test
    public void testCreateProjectXmlInvalidId() {
        given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>100</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject)
        .then()
            .statusCode(400);
    }

    @Test
    public void testCreateProjectXmlNullId() {
        given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>null</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject)
        .then()
            .statusCode(400);
    }
    
    //Edge Cases
    @Test
    public void testGetProjectIdNotExist() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/100")
        .then()
            .statusCode(404);
    }

    @Test
    public void testHeadProjectIdNotExist() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/100")
        .then()
            .statusCode(404)
            .body(emptyOrNullString());
    }



    @Test
    public void testDeleteProjectNoExistId() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100")
        .then()
            .statusCode(404);
    }



    @Test
    public void testPostProjectCategories() {
        given()
            .headers("Content-Type", "application/json")
            .body("{\"id\":\"100\"}")
        .when()
            .post(urlProject + "/100/categories")
        .then()
            .statusCode(404);
    }

    @Test
    public void testPostProjectCategoriesIdAsInt() {
        given()
            .headers("Content-Type", "application/json")
            .body("{\"id\":100}")
        .when()
            .post(urlProject + "/100/categories")
        .then()
            .statusCode(404);
    }

    @Test
    public void testDeleteProjectCategoriesWrongIds() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100/categories/100")
        .then()
            .statusCode(404);
    }


    //JSON Payload Generations
    @Test
    public void testGetProjects() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject)
        .then()
            .statusCode(200)
            .body("projects", notNullValue());
    }

    @Test
    public void testHeadProjects() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject)
        .then()
            .statusCode(200)
            .body(emptyOrNullString());
    }

    @Test
    public void testPostProject() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .post(urlProject)
        .then()
            .statusCode(201);

        given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject)
        .then()
            .statusCode(200)
            .body("projects", hasSize(2));
    }

    // /projects/:id

    @Test
    public void testGetProjectId() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/1")
        .then()
            .statusCode(200)
            .body("projects", hasSize(1));
    }

    @Test
    public void testGetProjectIdNotExistJSON() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/100")
        .then()
            .statusCode(404);
    }

    

    @Test
    public void testHeadProjectIdNotExistJSON() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/100")
        .then()
            .statusCode(404)
            .body(emptyOrNullString());
    }


    @Test
    public void testPostProjectIdNotExist() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .post(urlProject + "/100")
        .then()
            .statusCode(404);
    }




    @Test
    public void testPutProjectIdNotExist() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .put(urlProject + "/100")
        .then()
            .statusCode(404);
    }



    @Test
    public void testDeleteProjectNoExistIdJSON() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100")
        .then()
            .statusCode(404);
    }

    // /projects/:id/tasks


    @Test
    public void testHeadProjectTask() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/1/tasks")
        .then()
            .statusCode(200)
            .body(emptyOrNullString());
    }



    //XML Payload generations

    @Test
    public void testPostProjectIdNotExistXML() {
        given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>100</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject + "/100")
        .then()
            .statusCode(404);
    }

    

    @Test
    public void testPutProject() {
        given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .put(urlProject + "/1")
        .then()
            .statusCode(400);
    }


}
