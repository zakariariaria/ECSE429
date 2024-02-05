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
                .when()
                .get("http://localhost:4567/categories/1")
                .then()
                .extract()
                .response();
		assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode());     
    }
	@Test
    public void testCategoriesGetIdXML() throws Exception{
		 Response response = given()
   				.header("Accept", ContentType.XML)
					.when()
					.get("http://localhost:4567/categories/1")
					.then()
					.contentType(ContentType.XML)
					.extract()
					.response();
assertNotNull(response.getBody());
assertEquals(200, response.getStatusCode());
    }
	
	@Test
    public void testCategoriesWrongIdJSON() {
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
    public void testCategoriesHeadIdJSON() {
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
    public void testCategoriesHeadIdXML() {
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
	    public void testCategoriesPostJSON() {
				String title = "film";
				String description = "This is a film category";
				
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
				assertEquals(doneStatus,Boolean.parseBoolean(jsonResponse.get("doneStatus").toString()));
				assertEquals(description,jsonResponse.get("description"));
			}                         
	   
	   @Test
		 public void testPostCategoriesEmpty() {
		        given().
		            log().all().
		            headers("Content-Type", "application/json").
		            body("{\"title\":\"\",\"description\":\"this is empty\"}").
		        when().
		            post("http://localhost:4567/categories").
		        then().
		            log().all().
		            statusCode(400);
		      
		             
		    }
	   @Test
		 public void testPostCategoriesNull() {
		        given().
		            log().all().
		            headers("Content-Type", "application/json").
		            body("{\"title\":null,\"description\":\"this is null\"}").
		        when().
		            post("http://localhost:4567/categories").
		        then().
		            log().all().
		            statusCode(400);           	                     
		    }
	   @Test
		 public void testPostCategoriesWrongId() {
		        given().
		            log().all().
		            headers("Content-Type", "application/json").
		            body("{\"title\":\"WrongId\",\"description\":\"this is wrongId\"}").
		        when().
		            post("http://localhost:4567/categories/-1").
		        then().
		            log().all().
		            statusCode(404);        
		    }
	   @Test
		 public void testPostCategoriesId() {
		        given().
		            log().all().
		            headers("Content-Type", "application/json").
		            body("{\"title\":\"Homework\",\"description\":\"this is homework\"}").
		        when().
		            post("http://localhost:4567/categories/1").
		        then().
		            log().all().
		            statusCode(200);        
		    }
	   @Test
		 public void testPostCategoriesIdEmpty() {
		        given().
		            log().all().
		            headers("Content-Type", "application/json").
		            body("{\"title\":\"\",\"description\":\"this is homework\"}").
		        when().
		            post("http://localhost:4567/categories/1").
		        then().
		            log().all().
		            statusCode(400);        
		    }
	   @Test
		 public void testPostCategoriesIdNull() {
		        given().
		            log().all().
		            headers("Content-Type", "application/json").
		            body("{\"title\":null,\"description\":\"this is homework\"}").
		        when().
		            post("http://localhost:4567/categories/1").
		        then().
		            log().all().
		            statusCode(200);        
		    }
	  
	  
	    
	@Test
	public void testHeadCategories() {
        //RestAssured.baseURI = "http://localhost:4567";

        given().
        when().
            head("http://localhost:4567/categories").
        then(). 
        log().all().
        statusCode(200);    
    }
	@Test
	 public void testPutCategories() {
	        given().
	            log().all().
	            headers("Content-Type", "application/json").
	            body("{\"title\":\"foo\",\"description\":\"this is foo\"}").
	        when().
	            put("http://localhost:4567/categories/1").
	        then().
	            log().all().
	            statusCode(200).
	            body("title", equalTo("foo")).
	            body("description", equalTo("this is foo"));  	           	                     
	    }
	@Test
	 public void testPutCategoriesWrongId() {
	        given().
	            log().all().
	            headers("Content-Type", "application/json").
	            body("{\"title\":\"foo\",\"description\":\"this is foo\"}").
	        when().
	            put("http://localhost:4567/categories/-1").
	        then().
	            log().all().
	            statusCode(404);         	           	                     
	    }
	@Test
	 public void testPutCategoriesEmpty() {
	        given().
	            log().all().
	            headers("Content-Type", "application/json").
	            body("{\"title\":\"\",\"description\":\"this is foo\"}").
	        when().
	            put("http://localhost:4567/categories/1").
	        then().
	            log().all().
	            statusCode(400);         	           	                     
	    }
	@Test
	 public void testPutCategoriesNull() {
	        given().
	            log().all().
	            headers("Content-Type", "application/json").
	            body("{\"title\":null,\"description\":\"this is foo\"}").
	        when().
	            put("http://localhost:4567/categories/1").
	        then().
	            log().all().
	            statusCode(400);         	           	                     
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
