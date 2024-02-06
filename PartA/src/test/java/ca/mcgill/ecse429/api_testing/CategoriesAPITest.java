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
			assertEquals(200, response.getStatusCode());         		  
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
assertEquals(200, response.getStatusCode());	      		  
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
				String xmlPayload = "<category>" +
						"<title>job</title>" +
						"<description>this is a job category</description>" +
						"</category>";

				Response response = given()
						.header("Accept", ContentType.XML)
						.contentType(ContentType.XML)
						.body(xmlPayload)
						.when()
						.post("http://localhost:4567/categories")
						.then()
						.log().all()
						.extract()
						.response();
								
				assertEquals(201, response.getStatusCode());
				// Use xmlPath to parse the XML response
				XmlPath xmlResponse = response.xmlPath();
				assertEquals("job", xmlResponse.get("categories.title"));
				assertEquals("this is a job category", xmlResponse.get("categories.description"));
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
		 public void testCategoriesPostNullXML()throws Exception  {
		   String xmlPayload = "<category>" +
					"<title>null</title>" +
					"<description>this is a job category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(400, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
		    assertEquals("title : field is mandatory", response.getBody().xmlPath().get().toString());	    
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
		 public void testCategoriesPostWrongIdXML()throws Exception  {
		   String xmlPayload = "<category>" +
					"<title>Advertisement</title>" +
					"<description>this is a job category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories/-1")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(404, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
		    assertEquals("No such category entity instance with GUID or ID -1 found", response.getBody().xmlPath().get().toString());	    
		    }
	   @Test
		 public void testCategoriesPostIdJSON()throws Exception  {
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
		 public void testCategoriesPostIdXML()throws Exception  {
		   String xmlPayload = "<category>" +
					"<title>job</title>" +
					"<description>this is a job category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories/1")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(200, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
			assertEquals("job", xmlResponse.get("categories.title"));
			assertEquals("this is a job category", xmlResponse.get("categories.description")); 
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
	   @Test
		 public void testCategoriesPostIdEmptyXML()throws Exception  {
		   String xmlPayload = "<category>" +
					"<title></title>" +
					"<description>this is a job category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories/2")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(400, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
		    assertEquals("Failed Validation: title : can not be empty", response.getBody().xmlPath().get().toString());	    

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
			 assertEquals(200, response.getStatusCode());         
		    }
	   @Test
		 public void testCategoriesPostIdNullXML()throws Exception  {
		   String xmlPayload = "<category>" +
					"<title>null</title>" +
					"<description>this is a job category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories/2")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(200, response.getStatusCode());
			// Use xmlPath to parse the XML response		   
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
	 public void testCategoriesPutIdXML()throws Exception  {
		  String xmlPayload = "<category>" +
					"<title>products</title>" +
					"<description>this is a product category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.put("http://localhost:4567/categories/1")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(200, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
			assertEquals("products", xmlResponse.get("categories.title"));
			assertEquals("this is a product category", xmlResponse.get("categories.description")); 	
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
	 public void testCategoriesPutWrongIdXML()throws Exception  {
		  String xmlPayload = "<category>" +
					"<title>Bars</title>" +
					"<description>this is a job category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories/-1")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(404, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
		    assertEquals("No such category entity instance with GUID or ID -1 found", response.getBody().xmlPath().get().toString());	    


		 
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
			 assertEquals(validJsonError, response.getBody().asString(), "");
			 assertEquals(400, response.getStatusCode());          
	             	           	                     
	    }
	@Test
	 public void testCategoriesPutEmptyXML()throws Exception  {
		  String xmlPayload = "<category>" +
					"<title></title>" +
					"<description>this is a job category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories/1")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(400, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
		    assertEquals("Failed Validation: title : can not be empty", response.getBody().xmlPath().get().toString());	    		
	}
	//bug identified,expected to fail but passed
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
			 //String jsonError = "\"errorMessages\":[\"Failed Validation: title : can not be null\"]}" ;
			// String validJsonError = "{" + jsonError;
			// assertEquals(validJsonError, response.getBody().asString(), "The body should be null");
			 assertEquals(200, response.getStatusCode());                       	           	                     
	    }
	@Test
	 public void testCategoriesPutNullXML()throws Exception  {
		 String xmlPayload = "<category>" +
					"<title>null</title>" +
					"<description>this is a null category</description>" +
					"</category>";

			Response response = given()
					.header("Accept", ContentType.XML)
					.contentType(ContentType.XML)
					.body(xmlPayload)
					.when()
					.post("http://localhost:4567/categories/1")
					.then()
					.log().all()
					.extract()
					.response();
							
			assertEquals(200, response.getStatusCode());
			// Use xmlPath to parse the XML response
			XmlPath xmlResponse = response.xmlPath();
	}
	

	@Test
	 public void testCategoriesDeleteJSON() {
		String title = "film";
		String description = "This is a film category";
		
		JSONObject object = new JSONObject();
		object.put("title",title);
		object.put("description", description);
		
		Response response1 = given()
							.contentType(ContentType.JSON)
							.body(object.toJSONString())
							.when()
							.post("http://localhost:4567/categories")
							.then()
							.log().all()					
							.extract()
							.response();
		JsonPath jsonResponse = response1.jsonPath();
		String categoryID = jsonResponse.get("id");
	
		Response response = given()
				.contentType(ContentType.JSON)
				//.body(object.toJSONString())
				.when()
				.post("http://localhost:4567/categories/"+categoryID)
				.then()
				.log().all()					
				.extract()
				.response();
assertEquals(200, response.getStatusCode());   
JsonPath jsonResponse2 = response.jsonPath();
assertEquals(title,jsonResponse2.get("title"));
assertEquals(description,jsonResponse2.get("description"));
	    }
	@Test
	 public void testCategoriesDeleteWrongIdJSON() {
		
		Response response = given()
							.contentType(ContentType.JSON)
							//.body(object.toJSONString())
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
	 public void testCategoriesDeleteWrongIdXML() throws Exception {
		Response response = given()
				.header("Accept", ContentType.XML)
                .when()
                .delete("http://localhost:4567/categories/-1")
                .then()
                .contentType(ContentType.XML)
                .extract()
                .response();
		
		assertEquals("Could not find any instances with categories/-1", response.getBody().xmlPath().get().toString());
		assertEquals(404, response.getStatusCode());		           	                     
	    }

	@Test
	public void testCategoriesDeleteXML() throws Exception{
		String xmlPayload = "<category>" +
				"<title>dummy</title>" +
				"<description>this is a dummy category</description>" +
				"</category>";

		Response response = given()
				.header("Accept", ContentType.XML)
				.contentType(ContentType.XML)
				.body(xmlPayload)
				.when()
				.post("http://localhost:4567/categories")
				.then()
				.log().all()
				.extract()
				.response();				
		assertEquals(201, response.getStatusCode());

		// Use xmlPath to parse the XML response
		XmlPath xmlResponse = response.xmlPath();
	    String categoryID = xmlResponse.get("category.id");
	    
	    
		Response response1 = given()
				.header("Accept", ContentType.XML)
				.when()
				.delete("http://localhost:4567/categories/"+categoryID)
				.then()
				.log().all()
				.contentType(ContentType.XML)
				.extract()
				.response();
		assertEquals(200, response1.getStatusCode());	
	}
}