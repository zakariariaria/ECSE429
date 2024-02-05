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
import io.restassured.path.xml.XmlPath;
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
    public void testTodosGetJSON() throws Exception{
        Response response = given()
                                .when()
                                .get("http://localhost:4567/todos")
                                .then()
                                .extract()
                                .response();
        
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode());
    }
	
	@Test
    public void testTodosGetXML() throws Exception{
        Response response = given()
        						.header("Accept", ContentType.XML)
                                .when()
                                .get("http://localhost:4567/todos")
                                .then()
                                .contentType(ContentType.XML)
                                .extract()
                                .response();
        
        assertNotNull(response.getBody().xmlPath().get());
        assertEquals(200, response.getStatusCode());
    }
	
	@Test
    public void testTodosGetNonExistingIDJSON() throws Exception{
        Response response = given()
                                .when()
                                .get("http://localhost:4567/todos/1000")
                                .then()
                                .extract()
                                .response();
        
        String jsonError = "\"errorMessages\":[\"Could not find an instance with todos/1000\"]}" ;
		String validJsonError = "{" + jsonError;

		assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
		assertEquals(404, response.getStatusCode());
    }
	
	@Test
    public void testTodosGetNonExistingIDXML() throws Exception{
        Response response = given()
        						.header("Accept", ContentType.XML)
                                .when()
                                .get("http://localhost:4567/todos/1000")
                                .then()
                                .contentType(ContentType.XML)
                                .extract()
                                .response();
        
        assertEquals("Could not find an instance with todos/1000", response.getBody().xmlPath().get().toString());
		assertEquals(404, response.getStatusCode());
    }
	
	@Test
    public void testHeadTodosJSON() throws Exception{
        Response response = given()
        					.when()
        					.head("http://localhost:4567/todos")
        					.then()
        					.extract()
        					.response();
        assertEquals(200, response.getStatusCode());
    }
	
	@Test
    public void testHeadTodosXML() throws Exception{
        Response response = given()
              				.header("Accept", ContentType.XML)
        					.when()
        					.head("http://localhost:4567/todos")
        					.then()
        					.contentType(ContentType.XML)
        					.extract()
        					.response();
        assertEquals(200, response.getStatusCode());
    }
	
	@Test
	public void testPostTodosJSON() throws Exception{
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
	public void testPostTodosXML() throws Exception{
		String xmlPayload = "<todo>" +
				"<title>placeholder</title>" +
				"<doneStatus>false</doneStatus>" +
				"<description></description>" +
				"</todo>";

		Response response = given()
				.header("Accept", ContentType.XML)
				.contentType(ContentType.XML)
				.body(xmlPayload)
				.when()
				.post("http://localhost:4567/todos");

		assertEquals(201, response.getStatusCode());

		// Use xmlPath to parse the XML response
		XmlPath xmlResponse = response.xmlPath();
		assertEquals("placeholder", xmlResponse.get("todo.title"));
		assertEquals("false", xmlResponse.get("todo.doneStatus").toString()); // XML parsing returns strings
		assertEquals("", xmlResponse.get("todo.description"));
	}
	
	@Test
	public void testPostTodosWithoutTitleJSON() throws Exception{
		boolean doneStatus = false;
		String description = "";
		
		JSONObject object = new JSONObject();
		object.put("doneStatus", doneStatus);
		object.put("description", description);
		
		Response response = given()
							.contentType(ContentType.JSON)
							.body(object.toJSONString())
							.when()
							.post("http://localhost:4567/todos");
		
		assertEquals(400,response.getStatusCode());
		
		String jsonError = "\"errorMessages\":[\"title : field is mandatory\"]}";
		String validJsonError = "{" + jsonError;
		 
		assertEquals(response.getBody().asString(), validJsonError);
	}
	
	@Test
	public void testPostTodosWithoutTitleXML() throws Exception{
		String xmlPayload = "<todo>" +
				"<doneStatus>false</doneStatus>" +
				"<description></description>" +
				"</todo>";
		
		Response response = given()
				.header("Accept", ContentType.XML)
				.contentType(ContentType.XML)
				.body(xmlPayload)
				.when()
				.post("http://localhost:4567/todos");
		
		assertEquals(400,response.getStatusCode()); 
		assertEquals(response.getBody().asString(), "<errorMessages><errorMessage>title : field is mandatory</errorMessage></errorMessages>");
	}
	
	@Test
	public void testGetIDTodosJSON() throws Exception{		
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
	public void testGetIDTodosXML() throws Exception{	
		
		Response response = given()
				.header("Accept", ContentType.XML)
                .when()
                .get("http://localhost:4567/todos/1")
                .then()
                .contentType(ContentType.XML)
                .extract()
                .response();
		
		XmlPath xmlResponse = response.xmlPath();

		assertEquals(200,response.getStatusCode());
		assertEquals("scan paperwork", xmlResponse.getString("todos.todo[0].title"));
	    assertEquals(false, xmlResponse.getBoolean("todos.todo[0].doneStatus")); 
	    assertEquals("", xmlResponse.getString("todos.todo[0].description"));	
	}
	
	@Test
	public void testPostIDTodosJSON() throws Exception{

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
	public void testPostIDTodosXML() throws Exception{

		
	    String xmlPayload = "<todo>" +
	                        "<title>placeholder</title>" +
	                        "<doneStatus>false</doneStatus>" +
	                        "<description></description>" +
	                        "</todo>";

	    
	    Response responsePost = given()
	                            .contentType(ContentType.XML)
	                            .accept(ContentType.XML)
	                            .body(xmlPayload)
	                            .when()
	                            .post("http://localhost:4567/todos");

	    assertEquals(201, responsePost.getStatusCode());

	    XmlPath postResponseXml = responsePost.xmlPath();
	    String todoID = postResponseXml.getString("todo.id");

	    
	    String updateXmlPayload = "<todo>" +
	                              "<title>updated title</title>" +
	                              "</todo>";

	  
	    Response responseUpdate = given()
	                              .contentType(ContentType.XML)
	                              .accept(ContentType.XML)
	                              .body(updateXmlPayload)
	                              .when()
	                              .post("http://localhost:4567/todos/" + todoID);

	    assertEquals(200, responseUpdate.getStatusCode());

	    XmlPath updateResponseXml = responseUpdate.xmlPath();
	    assertEquals("updated title", updateResponseXml.getString("todo.title"));
	}
	
	@Test
	public void testPutIDTodosJSON() throws Exception{		
		
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
	public void testPutIDTodosXML() throws Exception{		
		
	    String xmlPayload = "<todo>" +
	                        "<title>placeholder</title>" +
	                        "<doneStatus>false</doneStatus>" +
	                        "<description></description>" +
	                        "</todo>";

	    
	    Response responsePost = given()
	                            .contentType(ContentType.XML)
	                            .accept(ContentType.XML)
	                            .body(xmlPayload)
	                            .when()
	                            .post("http://localhost:4567/todos");

	    assertEquals(201, responsePost.getStatusCode());

	    XmlPath postResponseXml = responsePost.xmlPath();
	    String todoID = postResponseXml.getString("todo.id");

	    
	    String updateXmlPayload = "<todo>" +
	                              "<title>updated title</title>" +
	                              "</todo>";

	   
	    Response responseUpdate = given()
	                              .contentType(ContentType.XML)
	                              .accept(ContentType.XML)
	                              .body(updateXmlPayload)
	                              .when()
	                              .post("http://localhost:4567/todos/" + todoID);

	    assertEquals(200, responseUpdate.getStatusCode());

	    XmlPath updateResponseXml = responseUpdate.xmlPath();
	    assertEquals("updated title", updateResponseXml.getString("todo.title"));
	}
	
	@Test
	public void testPutIDTodosNonExistingIDJSON() throws Exception{		
		String todoID = "1000";
		Response responseGet = given()
                .when()
                .put("http://localhost:4567/todos/"+todoID)
                .then()
                .extract()
                .response();
		
		String jsonError = "{\"errorMessages\":[\"Invalid GUID for " + todoID + " entity todo\"]}";
		
		assertEquals(jsonError, responseGet.getBody().asString(), "The body should be null");
		assertEquals(404, responseGet.getStatusCode());
	}
	
	@Test
	public void testPutIDTodosNonExistingIDXML() throws Exception {
	    String todoID = "1000";
	    Response response = given()
	            .header("Accept", ContentType.XML)
	            .contentType(ContentType.XML)
	            .when()
	            .put("http://localhost:4567/todos/" + todoID)
	            .then()
	            .extract()
	            .response();

	    

	    assertEquals(404, response.getStatusCode());
	    assertEquals("Invalid GUID for 1000 entity todo", response.getBody().xmlPath().get().toString());
	}
	
	@Test
	public void testDeleteIDTodosJSON() throws Exception{
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
	public void testDeleteIDTodosXML() throws Exception {
	    
	    String title = "placeholder";
	    JSONObject object = new JSONObject();
	    object.put("title", title);
	    object.put("doneStatus", false);
	    object.put("description", "");

	    Response responsePost = given()
	        .contentType(ContentType.JSON)
	        .body(object.toJSONString())
	        .when()
	        .post("http://localhost:4567/todos");

	    assertEquals(201, responsePost.getStatusCode());

	    JsonPath jsonResponse = responsePost.jsonPath();
	    String todoID = jsonResponse.get("id");

	   
	    Response responseDelete = given()
	        .when()
	        .delete("http://localhost:4567/todos/" + todoID);

	    assertEquals(200, responseDelete.getStatusCode());

	    
	    Response responseGet = given()
	        .header("Accept", ContentType.XML)
	        .when()
	        .get("http://localhost:4567/todos/" + todoID)
	        .then()
	        .extract()
	        .response();

	    

	    assertEquals("Could not find an instance with todos/"+todoID, responseGet.getBody().xmlPath().get().toString());
	    assertEquals(404, responseGet.getStatusCode());
	}
	
	@Test
	public void testDeleteIDTodosNonExistingIDJSON() throws Exception{
		String todoID = "1000";
		Response responseGet = given()
                .when()
                .delete("http://localhost:4567/todos/"+todoID)
                .then()
                .extract()
                .response();
		
		String jsonError = "\"errorMessages\":[\"Could not find any instances with todos/" + todoID + "\"]}";
		String validJsonError = "{" + jsonError;

		assertEquals(validJsonError, responseGet.getBody().asString(), "The body should be null");
		assertEquals(404, responseGet.getStatusCode());
	}
	
	@Test
	public void testDeleteIDTodosNonExistingIDXML() throws Exception{
		String todoID = "1000";
		Response response = given()
				.header("Accept", ContentType.XML)
                .when()
                .delete("http://localhost:4567/todos/"+todoID)
                .then()
                .contentType(ContentType.XML)
                .extract()
                .response();
		
		

		assertEquals("Could not find any instances with todos/"+todoID, response.getBody().xmlPath().get().toString());
		assertEquals(404, response.getStatusCode());
	}
	
	@Test
	public void testHeadIDTodosJSON() throws Exception{
		Response response = given()
				.when()
				.head("http://localhost:4567/todos/1")
				.then()
				.extract()
				.response();
		assertEquals(200, response.getStatusCode());
	}
	
	@Test
	public void testHeadIDTodosXML() throws Exception{
		Response response = given()
				.header("Accept", ContentType.XML)
				.when()
				.head("http://localhost:4567/todos/1")
				.then()
				.contentType(ContentType.XML)
				.extract()
				.response();
		assertEquals(200, response.getStatusCode());
	}
	
	@Test
	public void testOptionsIDTodosJSON() throws Exception{
		Response response = given()
				.when()
				.options("http://localhost:4567/todos/1")
				.then()
				.extract()
				.response();
		assertEquals(200, response.getStatusCode());
	}
	
	/**@Test Does not work with XML, only JSON
	public void testOptionsIDTodosXML() throws Exception{
		Response response = given()
				.header("Accept", ContentType.XML)
				.when()
				.options("http://localhost:4567/todos/1")
				.then()
				.contentType(ContentType.XML)
				.extract()
				.response();
		assertEquals(200, response.getStatusCode());
	}**/
	  
}
