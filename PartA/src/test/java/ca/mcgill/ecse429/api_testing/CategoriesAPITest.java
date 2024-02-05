package ca.mcgill.ecse429.api_testing;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;

class CategoriesAPITest {

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
    public void testCategoriesGetJSON() throws Exception {     
		  Response response = given()
                  .when()
                  .get("http://localhost:4567/categories")
                  .then()
                  .extract()
                  .response();
	      assertNotNull(response.getBody());
         assertEquals(200, response.getStatusCode());
}
	@Test
    public void testCategoriesGetXML() throws Exception{
		 Response response = given()
   				.header("Accept", ContentType.XML)
					.when()
					.get("http://localhost:4567/categories")
					.then()
					.contentType(ContentType.XML)
					.extract()
					.response();
assertEquals(200, response.getStatusCode());
    }
	
	@Test
    public void testCategoriesHeadJSON()throws Exception {
		Response response = given()
                .when()
                .head("http://localhost:4567/categories")
                .then()
                .extract()
                .response();
		assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode());      
    }
	@Test
    public void testCategoriesHeadXML() throws Exception{
		 Response response = given()
   				.header("Accept", ContentType.XML)
					.when()
					.head("http://localhost:4567/categories")
					.then()
					.contentType(ContentType.XML)
					.extract()
					.response();
assertEquals(200, response.getStatusCode());
    }
	
	@Test
    public void testCategoriesGetIdJSON()throws Exception {
		Response response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/categories/2");

JsonPath jsonResponse = response.jsonPath();
assertEquals(200,response.getStatusCode());

}
	@Test
    public void testCategoriesGetIdXML() throws Exception{
		 Response response = given()
   				.header("Accept", ContentType.XML)
					.when()
					.get("http://localhost:4567/categories/1")
					.then()
					.log().all()
					.contentType(ContentType.XML)
					.extract()
					.response();
		 
assertNotNull(response.getBody());
assertEquals(200, response.getStatusCode());
    }
	
	@Test
    public void testCategoriesWrongIdJSON() throws Exception  {
		Response response = given()
                .when()
                .get("http://localhost:4567/categories/-1")
                .then()
                .log().all()
                .extract()
                .response();
		 String jsonError = "\"errorMessages\":[\"Could not find an instance with categories/-1\"]}" ;
		 String validJsonError = "{" + jsonError;
			assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			assertEquals(404, response.getStatusCode());          
    }
	
	@Test
    public void testCategoriesWrongIdXML() throws Exception{
        Response response = given()
        						.header("Accept", ContentType.XML)
                                .when()
                                .get("http://localhost:4567/categories/-1")
                                .then()
                                .log().all()
                                .contentType(ContentType.XML)
                                .extract()
                                .response();
        
        assertEquals("Could not find an instance with categories/-1", response.getBody().xmlPath().get().toString());
		assertEquals(404, response.getStatusCode());
    }
	@Test
    public void testCategoriesHeadIdJSON() throws Exception {
		Response response = given()
                .when()
                .head("http://localhost:4567/categories/1")
                .then()
                .log().all()
                .extract()
                .response();	
			assertEquals(404, response.getStatusCode());         		  
    }
	@Test
    public void testCategoriesHeadIdXML()throws Exception  {
		 Response response = given()
					.header("Accept", ContentType.XML)
                 .when()
                 .head("http://localhost:4567/categories/1")
                 .then()
                 .log().all()
                 .contentType(ContentType.XML)
                 .extract()
                 .response();
assertEquals(404, response.getStatusCode());	      		  
    }
	   @Test
	    public void testCategoriesPostJSON() throws Exception {
				String title = "film";
				String description = "This is a film category";
				
				JSONObject object = new JSONObject();
				object.put("title",title);
				object.put("description", description);
				
				Response response = given()
									.contentType(ContentType.JSON)
									.body(object.toJSONString())
									.when()
									.post("http://localhost:4567/categories")
									.then()
									.log().all()					
									.extract()
									.response();	
				assertEquals(201,response.getStatusCode());
				JsonPath jsonResponse = response.jsonPath();
				assertEquals(title,jsonResponse.get("title"));
				assertEquals(description,jsonResponse.get("description"));
			}                         
	   
	   @Test
	   public void testCategoriesPostXML() throws Exception {
	       String xmlPayload = "<categories>" +
	                           "<title>film</title>" +
	                           "<description>This is a film category</description>" + 
	                           "</categories>";

	       Response response = given()
	               .header("Accept", ContentType.XML)
	               .contentType(ContentType.XML)
	               .body(xmlPayload)
	               .when()
	               .post("http://localhost:4567/categories");
	              
		   assertEquals(201,response.getStatusCode());
	       XmlPath xmlResponse = response.xmlPath();
	       assertEquals("film", xmlResponse.get("title"));
	       assertEquals("This is a film category", xmlResponse.get("description")); // Adjusted to match the provided description
	   }
	   @Test
		 public void testCategoriesPostNullJSON()throws Exception  {
			String title = null;
			String description = "This is a film category";
			
			JSONObject object = new JSONObject();
			object.put("title",title);
			object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories")
								.then()
								.log().all()					
								.extract()
								.response();	
			 String jsonError = "\"errorMessages\":[\"title : field is mandatory\"]}" ;
			 String validJsonError = "{" + jsonError;
			 assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(400, response.getStatusCode());      	     	                     
		    }
	   @Test
		 public void testCategoriesPostWrongIdJSON()throws Exception  {
		   String title = "film";
			String description = "This is a film category";
			
			JSONObject object = new JSONObject();
			object.put("title",title);
			object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/-1")
								.then()
								.log().all()					
								.extract()
								.response();	
			 String jsonError = "\"errorMessages\":[\"No such category entity instance with GUID or ID -1 found\"]}" ;
			 String validJsonError = "{" + jsonError;
			 assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(404, response.getStatusCode());    
		    }
	   @Test
		 public void testPostCategoriesIdJSON()throws Exception  {
		   String title = "Drinks";
			String description = "This is a drink category";
			
			JSONObject object = new JSONObject();
			object.put("title",title);
			object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/2")
								.then()
								.log().all()					
								.extract()
								.response();	
			assertEquals(200,response.getStatusCode());
			JsonPath jsonResponse = response.jsonPath();
			assertEquals(title,jsonResponse.get("title"));
			assertEquals(description,jsonResponse.get("description"));             
		    }
	   @Test
		 public void testCategoriesPostIdEmptyJSON()throws Exception  {
		    String title = "";
			String description = "This is a drink category";
			
			JSONObject object = new JSONObject();
			object.put("title",title);
			object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/2")
								.then()
								.log().all()					
								.extract()
								.response();	
			 String jsonError = "\"errorMessages\":[\"Failed Validation: title : can not be empty\"]}" ;
			 String validJsonError = "{" + jsonError;
			 assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(400, response.getStatusCode());      
		    }
	   //Bugs found here,expected to pass but failed due to api issues
	   @Test
		 public void testCategoriesPostIdNullJSON()throws Exception  {
		    String title = null;
			String description = "This is a drink category";
			
			JSONObject object = new JSONObject();
			object.put("title",title);
			object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/2")
								.then()
								.log().all()					
								.extract()
								.response();	
			 String jsonError = "\"errorMessages\":[\"Failed Validation: title : can not be null\"]}" ;
			 String validJsonError = "{" + jsonError;
			 assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(200, response.getStatusCode());         
		    }
	  
	  
	@Test
	 public void testCategoriesPutIdJSON()throws Exception  {
		    String title = "Food";
			String description = "This is a food category";
			
			JSONObject object = new JSONObject();
			object.put("title",title);
			object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/2")
								.then()
								.log().all()					
								.extract()
								.response();
			assertEquals(200,response.getStatusCode());
			JsonPath jsonResponse = response.jsonPath();
			assertEquals(title,jsonResponse.get("title"));
			assertEquals(description,jsonResponse.get("description"));        	                     
	    }
	@Test
	 public void testCategoriesPutWrongIdJSON()throws Exception  {
		 String title = "Food";
		 String description = "This is a food category";
			
		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/-1")
								.then()
								.log().all()					
								.extract()
								.response();
			 String jsonError = "\"errorMessages\":[\"No such category entity instance with GUID or ID -1 found\"]}" ;
			 String validJsonError = "{" + jsonError;
			 assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(404, response.getStatusCode());          
	             	           	                     
	    }
	@Test
	 public void testCategoriesPutEmptyJSON()throws Exception  {
		 String title = "";
		 String description = "This is a food category";
			
		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/2")
								.then()
								.log().all()					
								.extract()
								.response();
			 String jsonError = "\"errorMessages\":[\"Failed Validation: title : can not be empty\"]}" ;
			 String validJsonError = "{" + jsonError;
			 assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(400, response.getStatusCode());          
	             	           	                     
	    }
	@Test
	 public void testCategoriesPutNullJSON()throws Exception  {
		 String title = null;
		 String description = "This is a food category";
			
		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("description", description);
			
			Response response = given()
								.contentType(ContentType.JSON)
								.body(object.toJSONString())
								.when()
								.post("http://localhost:4567/categories/2")
								.then()
								.log().all()					
								.extract()
								.response();
			 String jsonError = "\"errorMessages\":[\"Failed Validation: title : can not be null\"]}" ;
			 String validJsonError = "{" + jsonError;
			 assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(400, response.getStatusCode());          
	             	           	                     
	    }
	

	@Test
	 public void testDeleteCategories() {
	        given().
	            log().all().
	            headers("Content-Type", "application/json").
	            body("{\"title\":\"foo\",\"description\":\"this is foo\"}").
		        when().
	        when().
	            delete("http://localhost:4567/categories/1").
	        then().
	            log().all().
	            statusCode(200);         	           	                     
	    }
	@Test
	 public void testDeleteCategoriesWrongId() {
	        given().
	            log().all().
	            headers("Content-Type", "application/json").
	            body("{\"title\":\"foo\",\"description\":\"this is foo\"}").
		        when().
	        when().
	            delete("http://localhost:4567/categories/-1").
	        then().
	            log().all().
	            statusCode(404);         	           	                     
	    }
	@Test
	 public void testDeleteCategoresWrongId() {
        given().
            log().all().
            headers("Content-Type", "application/json").
            body("{\"title\":\"foo\",\"description\":\"this is foo\"}").
	        when().
        when().
            delete("http://localhost:4567/categories/-1").
        then().
            log().all().
            statusCode(404);         	           	                     
    }
	@Test
	public void testDeleteCategoriesXML() throws Exception{
		Response response = given()
				.header("Accept", ContentType.XML)
				.when()
				.delete("http://localhost:4567/categories/1")
				.then()
				.contentType(ContentType.XML)
				.extract()
				.response();
		assertEquals(200, response.getStatusCode());
	}
    

}
