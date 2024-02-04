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
    public void testPostProjectBoolString() {
        given()
            .headers("Content-Type", "application/json")
            //.body(projectCreateBoolAsString)
        .when()
            .post(urlProject + "/1")
        .then()
            .statusCode(400)
            .body(containsString("Failed Validation: completed should be BOOLEAN"));
    }

    @Test
    public void testPostProjectNoBodyFieldJson() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .post(urlProject + "/1")
        .then()
            .statusCode(200)
            .body(containsString("Office Work"));
    }


    @Test
    public void testPutProjectNoBodyFieldJson() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .put(urlProject + "/1")
        .then()
            .statusCode(200)
            .body("title", equalTo(""))
            .body("description", equalTo(""));
    }


    @Test
    public void testDeleteProjectExistId() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/1")
        .then()
            .statusCode(200);
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
    public void testDeleteProjectTaskWrongIds() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100/tasks/100")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetProjectCategoriesWrongId() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/100/categories")
        .then()
            .statusCode(200)
            .body("categories", equalTo(null));
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
    public void testHeadProjectId() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/1")
        .then()
            .statusCode(200)
            .body(emptyOrNullString());
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
    public void testPostProjectId() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"new title\",\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .post(urlProject + "/1")
        .then()
            .statusCode(200)
            .body("title", equalTo("new title"));
    }

    @Test
    public void testPostProjectIdNotExist() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .post(urlProject + "/100")
        .then()
            .statusCode(404);
    }

    @Test
    public void testPostProjectNoBodyFieldJsonJSON() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .post(urlProject + "/1")
        .then()
            .statusCode(200)
            .body(containsString("Office Work"));
    }

    @Test
    public void testPostProjectNoBodyFieldJsonNullEntry() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":null,\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .post(urlProject + "/1")
        .then()
            .statusCode(200)
            .body(containsString("Office Work"))
            .body(containsString("some description"));
    }

    @Test
    public void testPutProjectId() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"new title\",\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .put(urlProject + "/1")
        .then()
            .statusCode(200)
            .body("title", equalTo("new title"));
    }

    @Test
    public void testPutProjectIdNotExist() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .put(urlProject + "/100")
        .then()
            .statusCode(404);
    }

    @Test
    public void testPutProjectNoBodyFieldJsonJSON() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .put(urlProject + "/1")
        .then()
            .statusCode(200)
            .body("title", equalTo(""))
            .body("description", equalTo(""));
    }

    @Test
    public void testPutProjectNoBodyFieldNullEntry() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":null,\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .put(urlProject + "/1")
        .then()
            .statusCode(200)
            .body("title", equalTo(""))
            .body("description", equalTo("some description"));
    }

    @Test
    public void testDeleteProjectId() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/1")
        .then()
            .statusCode(200);
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
    public void testGetProjectTask() {
        given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/1/tasks")
        .then()
            .statusCode(200)
            .body("todos", hasSize(2));
    }

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

    @Test
    public void testPostProjectTask() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"id\":\"1\" }")
        .when()
            .post(urlProject + "/1/tasks")
        .then()
            .statusCode(201);
    }


    //XML Payload generations
    @Test
    public void testGetProjectsXML() {
        given()
            .headers("Content-Type", "application/xml")
        .when()
            .get(urlProject)
        .then()
            .statusCode(200)
            .body("project", notNullValue());
    }

    @Test
    public void testPostProjectIdNotExistXML() {
        given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>t enim ad minim veni</description>\n  <id>100</id>\n  <completed>true</completed>\n  <title>incididunt ut labora</title>\n</project>")
        .when()
            .post(urlProject + "/100")
        .then()
            .statusCode(404);
    }

    @Test
    public void testPostProjectNoBodyFieldXml() {
        given()
            .headers("Content-Type", "application/xml")
        .when()
            .post(urlProject + "/1")
        .then()
            .statusCode(200);
    }

    @Test
    public void testPutProject() {
        given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>t enim ad minim veni</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>incididunt ut labora</title>\n</project>")
        .when()
            .put(urlProject + "/1")
        .then()
            .statusCode(400);
    }

    @Test
    public void testPostProjectTaskXML() {
        given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>t enim ad minim veni</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>incididunt ut labora</title>\n</project>")
        .when()
            .post(urlProject + "/1/tasks")
        .then()
            .statusCode(400);
    }

    @Test
    public void testPostProjectCategoriesXML() {
        given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>t enim ad minim veni</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>incididunt ut labora</title>\n</project>")
        .when()
            .post(urlProject + "/1/categories")
        .then()
            .statusCode(400);
    }
}