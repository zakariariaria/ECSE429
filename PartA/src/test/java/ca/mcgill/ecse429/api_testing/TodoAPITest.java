package ca.mcgill.ecse429.api_testing;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.json.simple.JSONObject;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;


@TestMethodOrder(MethodOrderer.Random.class)

public class TodoAPITest {
	
	private static boolean available;

    @BeforeAll
    public static void checkApiAvailability() {
        try {
            int statusCode = given()
                                .when()
                                .get("http://localhost:4567/")
                                .then()
                                .extract()
                                .statusCode();

            available = (statusCode == 200);
        } catch (Exception e) {
            fail(e.getMessage());
            available = false;
        }
    }
    
    @AfterAll
    public static void checkApiShutdown() {
        try {
            // Send a request to the shutdown endpoint
            int statusCode = given()
                                .when()
                                .post("http://localhost:4567/shutdown")
                                .then()
                                .extract()
                                .statusCode();
            
            // Assert that the API is not responding with a 200 status code, 
            // indicating it's not available after shutdown
            assertNotEquals(200, statusCode);
        } catch (Exception e) {
        }
    }
    
	@Test
    public void testTodosGet() throws Exception{
        Response response = given()
                                .when()
                                .get("http://localhost:4567/todos")
                                .then()
                                .extract()
                                .response();
        
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode());
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
    }
	
	@Test
    public void testHeadTodos() throws Exception{
        Response response = given()
        					.when()
        					.head("http://localhost:4567/todos")
        					.then()
        					.extract()
        					.response();
        assertEquals(200, response.getStatusCode());
    }
	
	@Test
	public void testPostTodos() throws Exception{
		String title = "placeholder";
		boolean doneStatus = false;
		String description = "";
		
		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("doneStatus", doneStatus);
		object.put("description", description);
		
		Response response = given()
							.contentType(ContentType.JSON)
							.body(object.toJSONString())
							.when()
							.post("http://localhost:4567/todos");
		
		assertEquals(201,response.getStatusCode());
		
		JsonPath jsonResponse = response.jsonPath();
		assertEquals(title,jsonResponse.get("title"));
		assertEquals(doneStatus,Boolean.parseBoolean(jsonResponse.get("doneStatus")));
		assertEquals(description,jsonResponse.get("description"));
	}
	
	@Test
	public void testGetIDTodos() throws Exception{		
		Response response = given()
							.contentType(ContentType.JSON)
							.when()
							.get("http://localhost:4567/todos/1");
		
		JsonPath jsonResponse = response.jsonPath();
		assertEquals(200,response.getStatusCode());
		assertEquals("scan paperwork", jsonResponse.getString("todos[0].title"));
	    assertEquals(false, jsonResponse.getBoolean("todos[0].doneStatus")); 
	    assertEquals("", jsonResponse.getString("todos[0].description"));	
	}
	
	@Test
	public void testPostIDTodos() throws Exception{

		String title = "placeholder";
		boolean doneStatus = false;
		String description = "";

		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("doneStatus", doneStatus);
		object.put("description", description);

		Response responsePost = given()
				.contentType(ContentType.JSON)
				.body(object.toJSONString())
				.when()
				.post("http://localhost:4567/todos");

		assertEquals(201,responsePost.getStatusCode());

		JsonPath jsonResponse = responsePost.jsonPath();
		String todoID = jsonResponse.get("id");

		String titleUpdate = "updated title";

		JSONObject objectUpdate = new JSONObject();
		object.put("title", titleUpdate);


		Response responseUpdate = given()
				.contentType(ContentType.JSON)
				.body(object.toJSONString())
				.when()
				.post("http://localhost:4567/todos/"+todoID);

		assertEquals(200, responseUpdate.getStatusCode());

		JsonPath jsonResponseUpdate = responseUpdate.jsonPath();
		assertEquals("updated title", jsonResponseUpdate.get("title"));
	}
	
	@Test
	public void testPutIDTodos() throws Exception{		
		
		String title = "placeholder";
		boolean doneStatus = false;
		String description = "";

		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("doneStatus", doneStatus);
		object.put("description", description);

		Response responsePost = given()
				.contentType(ContentType.JSON)
				.body(object.toJSONString())
				.when()
				.post("http://localhost:4567/todos");

		assertEquals(201,responsePost.getStatusCode());

		JsonPath jsonResponse = responsePost.jsonPath();
		String todoID = jsonResponse.get("id");

		String titleUpdate = "updated title";

		JSONObject objectUpdate = new JSONObject();
		object.put("title", titleUpdate);


		Response responsePut = given()
				.contentType(ContentType.JSON)
				.body(object.toJSONString())
				.when()
				.put("http://localhost:4567/todos/"+todoID);

		assertEquals(200, responsePut.getStatusCode());

		JsonPath jsonResponseUpdate = responsePut.jsonPath();
		assertEquals("updated title", jsonResponseUpdate.get("title"));
	}
	
	@Test
	public void testDeleteIDTodos() throws Exception{
		String title = "placeholder";
		boolean doneStatus = false;
		String description = "";
		
		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("doneStatus", doneStatus);
		object.put("description", description);
		
		Response responsePost = given()
							.contentType(ContentType.JSON)
							.body(object.toJSONString())
							.when()
							.post("http://localhost:4567/todos");
		
		assertEquals(201,responsePost.getStatusCode());
		
		JsonPath jsonResponse = responsePost.jsonPath();
		String todoID = jsonResponse.get("id");
		
		
		Response responseDelete = given()
				.when()
				.delete("http://localhost:4567/todos/"+todoID);
		
		assertEquals(200,responseDelete.getStatusCode());
		
		Response responseGet = given()
                .when()
                .get("http://localhost:4567/todos/"+todoID)
                .then()
                .extract()
                .response();
		
		String jsonError = "\"errorMessages\":[\"Could not find an instance with todos/" + todoID + "\"]}";
		String validJsonError = "{" + jsonError;

		assertEquals(validJsonError, responseGet.getBody().asString(), "The body should be null");
		assertEquals(404, responseGet.getStatusCode());
	}
	
	@Test
	public void testHeadIDTodos() throws Exception{
		Response response = given()
				.when()
				.head("http://localhost:4567/todos/1")
				.then()
				.extract()
				.response();
		assertEquals(200, response.getStatusCode());
	}
	
	@Test
	public void testOptionsTodos() throws Exception{
		Response response = given()
				.when()
				.options("http://localhost:4567/todos/")
				.then()
				.extract()
				.response();
		assertEquals(200, response.getStatusCode(),"This should give 200");
	}
	
	@Test
	public void testOptionsIDTodos() throws Exception{
		Response response = given()
			    .log().all()  // This will log the request details
			    .when()
			    .options("http://localhost:4567/todos/1");

			response.then().log().all(); // This will log the response details

			assertEquals(200, response.getStatusCode());
	}
	
	
   
    
    
    
}
