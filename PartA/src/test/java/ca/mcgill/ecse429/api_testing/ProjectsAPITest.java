package ca.mcgill.ecse429.api_testing;

import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.response.Response;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;



@TestMethodOrder(MethodOrderer.Random.class)

public class ProjectsAPITest {

    private String urlProject = "http://localhost:4567/projects";

    //Projects Data
    @Test
    public void testCreateProjectJson() {
        // Make the POST request and capture the response
        Response response = given()
            .contentType("application/json")
            .body("{\"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\"}")
        .when()
            .post(urlProject)
        .thenReturn(); // Use returnResponse() to get the response object

        // Assert status code
        assertEquals(200, response.getStatusCode());

        // Extract the body as a map and assert on its values
        Map<String, ?> responseBody = response.getBody().as(Map.class);

        assertEquals("sometitle", responseBody.get("title"));
        assertEquals(true, responseBody.get("completed"));
        assertEquals(false, responseBody.get("active"));
        assertEquals("qwertyuiop", responseBody.get("description"));
    }

    @Test
    public void testCreateProjectBoolAsString() {
        Response response = given()
            .contentType("application/json")
            .body("{\"title\":\"sometitle\",\"completed\":\"True\",\"active\":\"False\",\"description\":\"qwertyuiop\"}")
        .when()
            .post(urlProject)
        .thenReturn(); // Get the response

        assertEquals(200, response.getStatusCode());
        assertEquals("sometitle", response.jsonPath().getString("title"));
        assertEquals(true, response.jsonPath().getBoolean("completed"));
        assertEquals(false, response.jsonPath().getBoolean("active"));
        assertEquals("qwertyuiop", response.jsonPath().getString("description"));
    }

    @Test
    public void testCreateProjectXml() {
        Response response = given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject)
        .thenReturn(); // Get the response

        assertEquals(200, response.getStatusCode());
        assertEquals("qwertyuiop", response.xmlPath().getString("project.title"));
        assertEquals(true, response.xmlPath().getBoolean("project.completed"));
        assertEquals(true, response.xmlPath().getBoolean("project.active"));
        assertEquals("qwertyuiop", response.xmlPath().getString("project.description"));
    }
    @Test
    public void testCreateProjectXmlInvalidId() {
        Response response = given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>100</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject)
        .thenReturn();
        
        assertEquals(400,response.getStatusCode());
    }

    @Test
    public void testCreateProjectXmlNullId() {
        Response response = given()
            .contentType("application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>null</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject)
        .thenReturn();
           
        
        assertEquals(400,response.getStatusCode());
    }
    
    //Edge Cases
    @Test
    public void testGetProjectIdNotExist() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/100")
        .thenReturn();
        
        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testHeadProjectIdNotExist() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/100")
        .thenReturn();

        assertEquals(404, response.getStatusCode());
        assertTrue(response.getBody().asString().isEmpty());
    }



    @Test
    public void testPostProjectBoolString() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .post(urlProject + "/1")
        .thenReturn();

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Failed Validation: completed should be BOOLEAN"));
    }


    @Test
    public void testPostProjectNoBodyFieldJson() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .post(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Office Work"));
    }



    @Test
    public void testPutProjectNoBodyFieldJson() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .put(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals("", response.jsonPath().getString("title"));
        assertEquals("", response.jsonPath().getString("description"));
    }


    @Test
    public void testDeleteProjectExistId() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/1")
        .thenReturn();

            
        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void testDeleteProjectNoExistId() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100")
        .thenReturn();
            assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testDeleteProjectTaskWrongIds() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100/tasks/100")
        .thenReturn();
            
        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testGetProjectCategoriesWrongId() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/100/categories")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertNull(response.jsonPath().get("categories"));
    }

    @Test
    public void testPostProjectCategories() {
        Response response  = given()
            .headers("Content-Type", "application/json")
            .body("{\"id\":\"100\"}")
        .when()
            .post(urlProject + "/100/categories")
        .thenReturn();
        
        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testPostProjectCategoriesIdAsInt() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{\"id\":100}")
        .when()
            .post(urlProject + "/100/categories")
        .thenReturn();
        
        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testDeleteProjectCategoriesWrongIds() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100/categories/100")
        .thenReturn();
         
        
        assertEquals(404,response.getStatusCode());
    }


    //JSON Payload Generations
    @Test
    public void testGetProjects() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject)
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().getList("projects"));
    }


    @Test
    public void testHeadProjects() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject)
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().isEmpty());
    }


    @Test
    public void testPostProject() {
        given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"some description\" }")
        .when()
            .post(urlProject)
        .thenReturn();

        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject)
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals(2, response.jsonPath().getList("projects").size());
    }


    // /projects/:id

    @Test
    public void testGetProjectId() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals(1, response.jsonPath().getList("projects").size());
    }


    @Test
    public void testGetProjectIdNotExistJSON() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/100")
        .thenReturn();
        
        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testHeadProjectId() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().isEmpty());
    }


    @Test
    public void testHeadProjectIdNotExistJSON() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/100")
        .thenReturn();

        assertEquals(404, response.getStatusCode());
        assertTrue(response.getBody().asString().isEmpty());
    }


    @Test
    public void testPostProjectId() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"new title\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .post(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals("new title", response.jsonPath().getString("title"));
    }


    @Test
    public void testPostProjectIdNotExist() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .post(urlProject + "/100")
        .thenReturn();
          
        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testPostProjectNoBodyFieldJsonJSON() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .post(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("qwertyuiop"));
    }


    @Test
    public void testPostProjectNoBodyFieldJsonNullEntry() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":null,\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .post(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        String bodyAsString = response.getBody().asString();
        assertTrue(bodyAsString.contains("Office Work"));
        assertTrue(bodyAsString.contains("qwertyuiop"));
    }


    @Test
    public void testPutProjectId() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"new title\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .put(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals("new title", response.jsonPath().getString("title"));
    }


    @Test
    public void testPutProjectIdNotExist() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":\"sometitle\",\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .put(urlProject + "/100")
        .thenReturn();
        
        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void testPutProjectNoBodyFieldJsonJSON() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .put(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals("", response.jsonPath().getString("title"));
        assertEquals("", response.jsonPath().getString("description"));
    }


    @Test
    public void testPutProjectNoBodyFieldNullEntry() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{ \"title\":null,\"completed\":true,\"active\":false,\"description\":\"qwertyuiop\" }")
        .when()
            .put(urlProject + "/1")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals("", response.jsonPath().getString("title"));
        assertEquals("qwertyuiop", response.jsonPath().getString("description"));
    }


    @Test
    public void testDeleteProjectId() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/1")
        .thenReturn();
            
            assertEquals(200,response.getStatusCode());
    }

    @Test
    public void testDeleteProjectNoExistIdJSON() {
       Response response =  given()
            .headers("Content-Type", "application/json")
        .when()
            .delete(urlProject + "/100")
        .thenReturn();
        
        assertEquals(404,response.getStatusCode());
    }

    // /projects/:id/tasks

    @Test
    public void testGetProjectTask() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .get(urlProject + "/1/tasks")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertEquals(2, response.jsonPath().getList("todos").size());
    }


    @Test
    public void testHeadProjectTask() {
        Response response = given()
            .headers("Content-Type", "application/json")
        .when()
            .head(urlProject + "/1/tasks")
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().isEmpty());
    }


    @Test
    public void testPostProjectTask() {
        Response response = given()
            .headers("Content-Type", "application/json")
            .body("{ \"id\":\"1\" }")
        .when()
            .post(urlProject + "/1/tasks")
        .thenReturn();
        
        assertEquals(201,response.getStatusCode());
    }


    //XML Payload generations
    @Test
    public void testGetProjectsXML() {
        Response response = given()
            .headers("Content-Type", "application/xml")
        .when()
            .get(urlProject)
        .thenReturn();

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody().asString());
    }


    @Test
    public void testPostProjectIdNotExistXML() {
      
        Response response = given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>100</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject + "/100")
        .thenReturn(); 

        
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testPostProjectNoBodyFieldXml() {
        Response response = given()
            .headers("Content-Type", "application/xml")
        .when()
            .post(urlProject + "/1")
        .thenReturn();
            
        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void testPutProject() {
        Response response = given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .put(urlProject + "/1")
        .thenReturn();
        
        assertEquals(400,response.getStatusCode());
    }

    @Test
    public void testPostProjectTaskXML() {
        Response response = given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject + "/1/tasks")
        .thenReturn();
            
            
            assertEquals(400,response.getStatusCode());
    }

    @Test
    public void testPostProjectCategoriesXML() {
        Response response = given()
            .headers("Content-Type", "application/xml")
            .body("<project>\n  <active>true</active>\n  <description>qwertyuiop</description>\n  <id>1</id>\n  <completed>true</completed>\n  <title>qwertyuiop</title>\n</project>")
        .when()
            .post(urlProject + "/1/categories")
        .thenReturn();
        assertEquals(400,response.getStatusCode());
    }
}