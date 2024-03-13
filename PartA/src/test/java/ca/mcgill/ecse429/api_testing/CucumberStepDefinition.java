package ca.mcgill.ecse429.api_testing;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.Request;
import org.junit.runner.RunWith;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CucumberStepDefinition {
	
	
	// =========================== Feature : Create a new todo item ===========================
	private Response response; // Instance variable to hold the response
    private String title; // Instance variable for title
    private String description; // Instance variable for description
    private String doneStatus; // Instance variable for doneStatus
    private String id;
    private String projectsId;
    private String tasksoffId;
    private String categoryId;
    private String completed;
 
	
	// BUG FOUND : WHEN CREATING A TODO ITEM, ADDING A DONESTATUS RESULTS IN 400 ERROR 
	@When("a request is sent to create a new todo item with title {string} and description {string}")
	public void a_request_is_sent_to_create_a_new_todo_item_with_title_and_description(String title, String description) {
		this.title = title;
        this.description = description;
        
        
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("description", description);
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .post("http://localhost:4567/todos");
	}

	@Then("the response body should contain a todo item with title {string} and description {string} and doneStatus {string}")
	public void the_response_body_should_contain_a_todo_item_with_title_and_description_and_done_status(String title, String description, String doneStatus) {
        JsonPath jsonResponse = response.jsonPath();
        assertEquals(title, jsonResponse.get("title"));
        assertEquals(Boolean.parseBoolean(doneStatus), Boolean.parseBoolean(jsonResponse.get("doneStatus").toString()));
        assertEquals(description, jsonResponse.get("description"));
	}
	
	@When("a request is sent to create a new todo item with title {string}")
	public void a_request_is_sent_to_create_a_new_todo_item_with_title(String title) {
		this.title = title;
        
        JSONObject object = new JSONObject();
        object.put("title", title);
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .post("http://localhost:4567/todos");
	}

	

	@When("a request is sent to create a new todo item with a missing title")
	public void a_request_is_sent_to_create_a_new_todo_item_with_a_missing_title() {
		boolean doneStatus = false;
		String description = "";
		
		JSONObject object = new JSONObject();
		object.put("doneStatus", doneStatus);
		object.put("description", description);
		
		this.response = given()
							.contentType(ContentType.JSON)
							.body(object.toJSONString())
							.when()
							.post("http://localhost:4567/todos");
	}
	
	@Then("the response should contain a {int} status code")
	public void the_response_should_contain_a_status_code(Integer int1) {
		assertEquals(int1, response.getStatusCode());
	}	
	
	
	// =========================== Feature : Get todo item ===========================
	@When("a request is sent to get all todo items")
	public void a_request_is_sent_to_get_all_todo_items() {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/todos");
	}

	@Then("the response body should contain a list of todo items")
	public void the_response_body_should_contain_a_list_of_todo_items() {
		assertNotNull(this.response.getBody());
	}
	//BUG FOUND : CANNOT GET TODO ITEM WITH ID OF 1
	@When("a request is sent to get a todo item with id {string}")
	public void a_request_is_sent_to_get_a_todo_item_with_id(String id) {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/todos/"+id);
	}

	@Then("the response body should contain a todo item with {string} and {string} and {string}")
	public void the_response_body_should_contain_a_todo_item_with_and_and(String title, String description, String doneStatus) {
		JsonPath jsonResponse = response.jsonPath();

	    assertEquals(title, jsonResponse.get("todos[0].title"));
	    assertEquals(description, jsonResponse.get("todos[0].description"));
	    assertEquals(Boolean.parseBoolean(doneStatus), Boolean.parseBoolean((String) jsonResponse.get("todos[0].doneStatus")));
	}
	
	
	// =========================== Feature : Update todo item ===========================
	
	@Given("a todo item exists with title {string} and description {string}")
	public void a_todo_item_exists_with_title_and_description(String title, String description) {
		this.title = title;
        this.description = description;
        
        
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("description", description);
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .post("http://localhost:4567/todos");
        JsonPath jsonResponse = response.jsonPath();
        this.id = jsonResponse.get("id");
        
	}
	
	@Given("a todo item exists with title {string}")
	public void a_todo_item_exists_with_title(String title) {
		this.title = title;
        
        JSONObject object = new JSONObject();
        object.put("title", title);
     
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .post("http://localhost:4567/todos");
        JsonPath jsonResponse = response.jsonPath();
        this.id = jsonResponse.get("id");
	}
	
	// BUG FOUND : WHEN UDPATING A TODO ITEM, TRYING TO UDPATE ANYTHING ELSE THAN TITLE RESULTS IN 400 ERROR 
	@When("a request is sent to update the fields of the todo item to title {string} and description {string} and doneStatus {string}")
	public void a_request_is_sent_to_update_the_fields_of_the_todo_item_to_title_and_description_and_done_status(String title, String description, String doneStatus) {
		this.title = title;
        this.description = description;
        this.doneStatus = doneStatus;
        
        
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("description", description);
        object.put("doneStatus", "true");
        
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .put("http://localhost:4567/todos/"+this.id);
	}

	@Then("the response body should contain the updated todo item with title {string} and description {string} and doneStatus {string}")
	public void the_response_body_should_contain_the_updated_todo_item_with_title_and_description_and_done_status(String title, String description, String doneStatus) {
		JsonPath jsonResponse = response.jsonPath();
        assertEquals(title, jsonResponse.get("title"));
        assertEquals(Boolean.parseBoolean(doneStatus), Boolean.parseBoolean(jsonResponse.get("doneStatus").toString()));
        assertEquals(description, jsonResponse.get("description"));
	}

	@When("a request is sent to update the title of the todo item to {string}")
	public void a_request_is_sent_to_update_the_title_of_the_todo_item_to(String title) {
		this.title = title;
      
        JSONObject object = new JSONObject();
        object.put("title", title);
    
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .put("http://localhost:4567/todos/"+this.id);
	}

	@Then("the response body should contain the updated todo item with {string} and {string} and {string}")
	public void the_response_body_should_contain_the_updated_todo_item_with_and_and(String title, String description, String doneStatus) {
		JsonPath jsonResponse = response.jsonPath();
        assertEquals(title, jsonResponse.get("title"));
        assertEquals(Boolean.parseBoolean(doneStatus), Boolean.parseBoolean(jsonResponse.get("doneStatus").toString()));
        assertEquals(description, jsonResponse.get("description"));
	}

	@When("a request is sent to update a todo item with id {string}")
	public void a_request_is_sent_to_update_a_todo_item_with_id(String id) {
		this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .put("http://localhost:4567/todos/"+this.id);
	}
	
	// =========================== Feature : Assigning todo to project ===========================
	@Given("a project exists")
	public void a_project_exists() {
		this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:4567/projects");
		JsonPath jsonResponse = response.jsonPath();
		this.projectsId = jsonResponse.get("id");
	}

	@When("a request is sent to assign the todo item to the project")
	public void a_request_is_sent_to_assign_the_todo_item_to_the_project() {
		JSONObject object = new JSONObject();
		object.put("id", this.id);

		Response response = given()
		    .contentType(ContentType.JSON)
		    .body(object.toJSONString())
		    .when()
		    .post("http://localhost:4567/projects/" + projectsId + "/tasks");
	}

	@Then("the todo item with title {string} should be succesfully associated with project")
	public void the_todo_item_with_title_should_be_succesfully_associated_with_project(String title) {
		Response response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/todos/" + this.id + "/tasksof");

		JsonPath jsonResponse = response.jsonPath();
		List<Map<String, ?>> projects = jsonResponse.getList("projects");

		assertNotNull(projects);
		
		Map<String, ?> project = projects.get(0); 
		assertNotNull(project);
		assertEquals(this.projectsId, project.get("id"));
	}
	
	@When("I send a request to create a new project with title {string} and active status {string}")
	public void i_send_a_request_to_create_a_new_project_with_title_and_active_status(String title, String activeStatus) {
		JSONObject projectDetails = new JSONObject();
	    projectDetails.put("title", title);
	    projectDetails.put("active", activeStatus);

	    this.response = given()
	        .contentType(ContentType.JSON)
	        .body(projectDetails.toJSONString())
	        .when()
	        .post("http://localhost:4567/todos/" + this.id + "/tasksof");
	}
	// BUG FOUND : WHEN SPECIFYING FIELDS, PROJECT CANNOT BE CREATED (ERROR 400)
	@Then("a new project should be created with the specified title and active status")
	public void a_new_project_should_be_created_with_the_specified_title_and_active_status() {
		assertNotNull(this.response);
	    

	    // Assuming the response includes the project details
	    JsonPath jsonResponse = this.response.jsonPath();
	    String returnedTitle = jsonResponse.get("title");

	    assertEquals("New Project", returnedTitle);
	   
	}

	@When("a request is sent to assign a todo item with id {string}")
	public void a_request_is_sent_to_assign_a_todo_item_with_id(String id) {
		this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:4567/todos/" + id + "/tasksof");
	}
	
	// =========================== Feature : Categorizing todos ===========================
	
	@Given("a category exists with title {string}")
	public void a_category_exists_with_title(String title) {
		JSONObject categoryDetails = new JSONObject();
		categoryDetails.put("title", title);

	    this.response = given()
	        .contentType(ContentType.JSON)
	        .body(categoryDetails.toJSONString())
	        .when()
	        .post("http://localhost:4567/categories");
	    JsonPath jsonResponse = response.jsonPath();
		this.categoryId = jsonResponse.get("id");
	    
	}
	
	@When("I send a request to create a new category with title {string}")
	public void i_send_a_request_to_create_a_new_category_with_title(String title) {
		JSONObject categoryDetails = new JSONObject();
		categoryDetails.put("title", title);

	    this.response = given()
	        .contentType(ContentType.JSON)
	        .body(categoryDetails.toJSONString())
	        .when()
	        .post("http://localhost:4567/todos/" + this.id + "/categories");
	}

	@Then("the category {string} should be successfully associated with the todo item")
	public void the_category_should_be_successfully_associated_with_the_todo_item(String categoryTitle) {
		Response getCategoryResponse = given()
		        .contentType(ContentType.JSON)
		        .when()
		        .get("http://localhost:4567/todos/" + this.id + "/categories");

		    // Parse the response
		    JsonPath jsonResponse = getCategoryResponse.jsonPath();
		    List<Map<String, String>> categories = jsonResponse.getList("categories");

		    // Check that the categories list is not null and has only one item
		    assertNotNull(categories, "The categories list should not be null");
		    assertEquals(1, categories.size(), "There should be only one category associated with the todo item");

		    // Optionally, check that the title of the category matches the expected name
		    String actualCategoryTitle = categories.get(0).get("title");
		    assertEquals(categoryTitle, actualCategoryTitle);
	}

	@When("I send a request to create a new category without a title")
	public void i_send_a_request_to_create_a_new_category_without_a_title() {
	    this.response = given()
	        .contentType(ContentType.JSON)
	        .when()
	        .post("http://localhost:4567/todos/" + this.id + "/categories");
	}
	
	@When("I send a request to create a associate the category with the todo")
	public void i_send_a_request_to_create_a_associate_the_category_with_the_todo() {
		JSONObject requestBody = new JSONObject();
	    requestBody.put("id", categoryId);

	    given()
	        .contentType(ContentType.JSON)
	        .body(requestBody.toString())
	        .when()
	        .post("http://localhost:4567/todos/" + this.id + "/categories");
	}

	// =========================== Feature : Delete Category Project Relationship ===========================

    @Given("a project exists with title {string}")
    public void a_project_with_id(String title) {
		JSONObject projectsDetails = new JSONObject();
		projectsDetails.put("title", title);

		this.response = given()
				.contentType(ContentType.JSON)
				.body(projectsDetails.toJSONString())
				.when()
				.post("http://localhost:4567/projects");
		JsonPath jsonResponse = response.jsonPath();
		this.projectsId = jsonResponse.get("id");
    }

    @When("I send a request to create an association between the category to the project")
    public void i_create_project_title_for_category() {
		JSONObject requestBody = new JSONObject();
		requestBody.put("id", projectsId);

        this.response = given()
            .contentType(ContentType.JSON)
            .body(requestBody.toString())
            .when()
            .post("http://localhost:4567/categories/" + this.categoryId + "/projects");
    }
	@When("I delete category {string} and project {string} relationship")
	public void iDeleteProjectAndCategoryRelationship(String title, String title1) {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/categories/" + this.categoryId + "/projects/" + this.projectsId);
	}

	@When("I delete project {string} and category {string} relationship")
	public void iDeleteCategoryAndProjectRelationship(String title, String title1) {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/projects/" + this.projectsId + "/categories/" + this.categoryId);
	}

    @Then("there is no project for category {string}")
    public void rel_between_todo_category_does_not_exist() {
        this.response = given()
            .contentType(ContentType.JSON)
            .when()
            .get("http://localhost:4567/categories/" + this.categoryId + "/projects");
        
        JsonPath jsonResponse = response.jsonPath();
        assertEquals(0, jsonResponse.getList("projects").size());
    }

	// =========================== Feature : Delete Todo Project Relationship ===========================
	@When("I delete relationship between todo {string} and project {string} with status {string}")
	public void delete_todo_project_relationship(String id1, String id2, String status) {
		this.response = given()
				.when()
				.delete("http://localhost:4567/todos/" + id1 + "/tasksof/" + id2);
		assertEquals(Integer.parseInt(status), response.getStatusCode());
	}

	@Then("deleted relationship for todo {string} and project does not exist")
	public void no_relationship_between_todo_and_project(String id1) {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/todos/" + id1 + "/tasksof");

		JsonPath jsonResponse = response.jsonPath();
		assertEquals(0, jsonResponse.getList("projects").size());
	}

	// =========================== Feature : Delete Project Category Relationship ===========================
	@When("I send a request to create an association between the project to the category")
	public void i_create_category_for_project_relation() {
		JSONObject requestBody = new JSONObject();
		requestBody.put("id", categoryId);

		this.response = given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/projects/" + this.projectsId + "/categories");
	}

	@When("I delete project {string} and category relationship")
	public void i_delete_category_to_project_rel(String projectTitle, String categoryTitle) {
		this.response = given()
				.when()
				.delete("http://localhost:4567/projects/" + this.projectsId + "/categories/" + this.categoryId);
	}

	//=========================== Feature : Delete Project ===========================
	@When("a request is sent to delete this project")
	public void aRequestIsSentToDeleteThisProject() {
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/projects/"+this.projectsId);
	}

	@When("a request is sent to delete this project without specifying its id")
	public void aRequestIsSentToDeleteThisProjectWithoutSpecifyingItsId() {

		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/projects/");
	}

	@When("a request is sent to delete a project with id {string}")
	public void aRequestIsSentToDeleteAProjectWithId(String id) {
		this.projectsId=id;
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/projects/"+this.projectsId);

	}

	// =========================== Feature : View Projects for Category ===========================

	@Then("verify project with title {string} exists under category {string}")
	public void project_title_exists_category(String thetitle, String theid) {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/categories/" + theid + "/projects");
		assertEquals(200, response.getStatusCode());

		JsonPath jsonResponse = response.jsonPath();
		JSONObject category = jsonResponse.getJsonObject("categories[0]");
		String title = (String) category.get("title");
		assertEquals(thetitle, title);
	}

	@Given("a project exist with title {string}")
	public void aProjectExistWithTitle(String title) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("title", title);
		this.response = given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toJSONString())
				.when()
				.post("http://localhost:4567/projects");
		JsonPath jsonResponse = response.jsonPath();
		this.projectsId = jsonResponse.get("id");
	}

	@Given("a association between the project and category exit")
	public void aAssociationBetweenTheProjectAndCategoryExit() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", this.projectsId);
		this.response = given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toJSONString())
				.when()
				.post("http://localhost:4567/projects/" + this.projectsId + "/categories");
	}

	@When("I send a request get the category of the project")
	public void iSendARequestGetTheCategoryOfTheProject() {
		this.title = title;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", this.projectsId);
		this.response = given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toJSONString())
				.when()
				.get("http://localhost:4567/projects/" + this.projectsId + "/categories");
	}


	@Then("the response should contain a empty list of categories")
	public void theResponseShouldContainAEmptyListOfCategories() {
		assertEquals(null, response.jsonPath().getList("category"));
	}

	@When("a request is made to delete the todo and project relationship")
	public void aRequestIsMadeToDeleteTheTodoAndProjectRelationship() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", this.projectsId);
		this.response = given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toJSONString())
				.when()
				.delete("http://localhost:4567/todos/" + this.id + "/tasksof/" + this.projectsId);

	}

	@Given("a association between the project and todo item exist")
	public void aAssociationBetweenTheProjectAndTodoItemExist() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", this.projectsId);
		this.response = given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toJSONString())
				.when()
				.post("http://localhost:4567/todos/" + this.id + "/tasksof");
	}

	@When("a request is made to delete the project")
	public void aRequestIsMadeToDeleteTheProject() {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/projects/" + this.projectsId);

	}

	@When("a request is sent to get the project from the category")
	public void aRequestIsSentToGetTheProjectFromTheCategory() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", this.projectsId);
		this.response = given()
				.contentType(ContentType.JSON)
				.body(jsonObject.toJSONString())
				.when()
				.get("http://localhost:4567/todos/" + this.id + "/tasksof");
	}


	@Then("the response body should not contain the project with title {string}")
	public void theResponseBodyShouldContainTheProjectWithTitle(String title) {
		List<Map<String, Object>> projects = response.jsonPath().getList("projects");
		boolean containsTitle = false;
		for (Map<String, Object> project : projects) {
			if (project.containsKey("title")) {
				containsTitle = true;
				break;
			}
			assertTrue(containsTitle);
		}
	}
	


	@When("a request is sent to create a new category with title {string}")
	public void aRequestIsSentToCreateANewCategoryWithTitle(String title) {
		this.title=title;
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/categories");
	}

	@And("the response body should contain a category with title {string}")
	public void theResponseBodyShouldContainACategoryWithTitle(String title) {
		JsonPath jsonResponse = response.jsonPath();
		assertEquals(title,jsonResponse.get("title"));
	}

	@When("a request is sent to create a new category with title {string} and description {string}")
	public void aRequestIsSentToCreateANewCategoryWithTitleDescription(String title, String description) {
		this.title=title;
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		requestBody.put("description", description);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/categories");
	}

	@And("the response body should contain a category with title {string} and description {string}")
	public void theResponseBodyShouldContainACategoryWithTitleDescription(String title, String description) {
		JsonPath jsonResponse = response.jsonPath();
		assertEquals(title,jsonResponse.get("title"));
		assertEquals(description,jsonResponse.get("description"));
	}

	@When("a request is sent to create a new category  with a missing title")
	public void aRequestIsSentToCreateANewCategoryWithAMissingTitle() {
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.post("http://localhost:4567/categories");
	}


	@When("a request is sent to update the field of the category to title {string}")
	public void aRequestIsSentToUpdateTheFieldOfTheCategoryToTitle(String title) {
		this.title=title;
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/categories/"+this.categoryId);
	}

	@When("a request is sent to update the field of the category to title {string} and description {string}")
	public void aRequestIsSentToUpdateTheFieldOfTheCategoryToTitleAndDescription(String title, String description) {
		this.title=title;
		this.description=description;
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		requestBody.put("description", description);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/categories/"+this.categoryId);
	}


	@When("a request is sent to update the title of a category with id {string} to title {string}")
	public void aRequestIsSentToUpdateTheTitleOfACategoryWithIdToTitle(String id, String title) {
		this.categoryId=id;
		this.title=title;
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/categories/"+this.categoryId);
	}

	@When("a request is sent to delete this category")
	public void aRequestIsSentToDeleteThisCategory() {
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/categories/"+this.categoryId);
	}

	@When("a request is sent to delete a category with id {string}")
	public void aRequestIsSentToDeleteACategoryWithId(String id) {
		this.categoryId=id;
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/categories/"+this.categoryId);

	}

	@Given("the todo item with title {string} is associated with the category")
	public void theTodoItemWithTitleIsAssociatedWithTheCategory(String title) {
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/categories/"+this.categoryId+"/todos");
	}

	@When("a request is sent to get the todos associated with the category")
	public void aRequestIsSentToGetTheTodosAssociatedWithTheCategory() {
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/categories/"+this.categoryId+"/todos");
	}

	@When("a request is sent to get the todos with title{string} associated with the category")
	public void aRequestIsSentToGetTheTodosWithTitleAssociatedWithTheCategory(String title) {

		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/categories/"+this.categoryId+"/todos"+this.id);
	}

	//Bugs found:
	@And("the response body should contain a todo item with title {string}")
	public void theResponseBodyShouldContainATodoItemWithTitle(String title) {
		JsonPath JsonResponse=this.response.jsonPath();
		assertEquals(title,JsonResponse.get("title"));
	}

	@When("a request is sent to delete this category without specifying its id")
	public void aRequestIsSentToDeleteThisCategoryWithoutSpecifyingItsId() {

		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/categories/");
	}

	@And("the response body should contain an empty list of todo items")
	public void theResponseBodyShouldContainAEmptyListOfTodoItems() {
		assertNotNull(response);
		int size = response.jsonPath().getList("todos").size();
		assertEquals(0,size);
	}

	@Given("the category with title {string} is associated with the todo")
	public void theCategoryWithTitleIsAssociatedWithTheTodo(String title) {
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.post("http://localhost:4567/todos/"+this.id+"/categories");
		this.categoryId=response.jsonPath().get("id");
	}

	@When("a request is sent to delete the category with title {string} associated with the todo using the id")
	public void aRequestIsSentToDeleteTheCategoryWithTitleAssociatedWithTheTodo(String title) {
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/todos/"+this.id+"/categories/"+this.categoryId);
	}

	@When("a request is sent to delete the category with title {string} associated with the todo using both the id and title")
	public void aRequestIsSentToDeleteTheCategoryWithTitleAssociatedWithTheTodoUsingBothTheIdAndTitle(String title) {
		JSONObject requestBody = new JSONObject();
		requestBody.put("title", title);
		this.response=given()
				.contentType(ContentType.JSON)
				.body(requestBody.toString())
				.when()
				.delete("http://localhost:4567/todos/"+this.id+"/categories/"+this.categoryId);
	}

	@When("a request is sent to delete the category with title {string} associated with the todo without specifying the id")
	public void aRequestIsSentToDeleteTheCategoryWithTitleAssociatedWithTheTodoWithoutSpecifyingTheId(String title) {
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.delete("http://localhost:4567/todos/"+this.id+"/categories/");
	}

	@When("a request is sent to get the todos with title{string} using a invalid category id {string}")
	public void aRequestIsSentToGetTheTodosWithTitleUsingAInvalidCategoryId(String title, String id) {
		this.categoryId=id;
		this.response=given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/todos/"+this.id+"/categories/"+this.categoryId);
	}
	
	
	// =========================== Project Feature 1: Get project ===========================

	@When("a request is sent to get all projects")
	public void a_request_is_sent_to_get_all_projects() {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/projects");
	}

	@Then("the response body should contain a list of projects")
	public void the_response_body_should_contain_a_list_of_projects() {
		assertNotNull(this.response.getBody());
	}

	@When("a request is sent to get a project with id {string}")
	public void a_request_is_sent_to_get_a_project_with_id(String id) {
		this.response = given()
				.contentType(ContentType.JSON)
				.when()
				.get("http://localhost:4567/todos/"+id);
	}

	// BUG FOUND : CANNOT GET A PROJECT WITH AN EXISTING ID
	@Then("the response body should contain a project with {string} and {string}")
	public void the_response_body_should_contain_a_project_with_and(String title, String description) {
		JsonPath jsonResponse = response.jsonPath();
		String titleObject = jsonResponse.get("projects[0].title");
		String descObject = jsonResponse.get("projects[0].title");
		
	    assertEquals(title,titleObject);
	    assertEquals(description, descObject);
	    assertEquals(Boolean.parseBoolean(doneStatus), Boolean.parseBoolean((String) jsonResponse.get("todos[0].doneStatus")));
	}
	
	// =========================== Project Feature 2: Create a Project by Specifying Name ===========================
	
	@When("a request is sent to create a new project item with title {string} and description {string}")
	public void a_request_is_sent_to_create_a_new_project_item_with_title_and_description(String title, String description) {
		this.title = title;
        this.description = description;
        
        
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("description", description);
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .post("http://localhost:4567/projects");
	}

	@Then("the response body should contain a project item with title {string} and description {string}")
	public void the_response_body_should_contain_a_project_item_with_title_and_description(String title, String description) {
		JsonPath jsonResponse = response.jsonPath();
        assertEquals(title, jsonResponse.get("title"));
        assertEquals(description, jsonResponse.get("description"));
	}

	@When("a request is sent to create a new project item with title {string}")
	public void a_request_is_sent_to_create_a_new_project_item_with_title(String title) {
		this.title = title;
        
        JSONObject object = new JSONObject();
        object.put("title", title);
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .post("http://localhost:4567/projects");
	}
	
	@Then("the response body should contain a project item with title {string} and description {string} and completed {string}")
	public void the_response_body_should_contain_a_project_item_with_title_and_description_and_completed(String title, String description, String completed) {
		JsonPath jsonResponse = response.jsonPath();
        assertEquals(title, jsonResponse.get("title"));
        assertEquals(Boolean.parseBoolean(completed), Boolean.parseBoolean(jsonResponse.get("completed").toString()));
        assertEquals(description, jsonResponse.get("description"));
	}

	@When("a request is sent to create a new project item with a missing title")
	public void a_request_is_sent_to_create_a_new_project_item_with_a_missing_title() {
		
		String description = "";
		
		JSONObject object = new JSONObject();
		object.put("description", description);
		
		this.response = given()
							.contentType(ContentType.JSON)
							.body(object.toJSONString())
							.when()
							.post("http://localhost:4567/todos");
	}
	
	// =========================== Project Feature 3: Get category ===========================
	@When("a request is sent to get all categories")
    public void a_request_is_sent_to_get_all_categories() {
        this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:4567/categories");
    }

    @Then("the response body should contain a list of categories")
    public void the_response_body_should_contain_a_list_of_categories() {
        assertNotNull(this.response.getBody());
    }
    
    @When("a request is sent to get a category with id {string}")
    public void a_request_is_sent_to_get_a_category_with_id(String id) {
        this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:4567/todos/"+id);
    }
    
    
 // =========================== Project Feature 4: Update project ===========================
    
    @Given("a project item exists with title {string}")
    public void a_project_item_exists_with_title(String title) {
    	this.title = title;
       
        
        
        JSONObject object = new JSONObject();
        object.put("title", title);
   
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .post("http://localhost:4567/todos");
        JsonPath jsonResponse = response.jsonPath();
        this.id = jsonResponse.get("id");
    }
    
    //BUG FOUND: CAN'T UPDATE PROJECT
    @When("a request is sent to update the fields of the project item to title {string} and description {string} and completed {string}")
    public void a_request_is_sent_to_update_the_fields_of_the_project_item_to_title_and_description_and_completed(String title, String description, String completed) {
    	this.title = title;
        this.description = description;
        this.completed = completed;
        
        
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("description", description);
        object.put("completed", "true");
        
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .put("http://localhost:4567/projects/"+this.id);
    }
    
    //BUG FOUND: CAN'T UPDATE PROJECT
    @When("a request is sent to update the title of the project to {string}")
    public void a_request_is_sent_to_update_the_title_of_the_project_to(String title) {
    	this.title = title;
        
        
        
        JSONObject object = new JSONObject();
        object.put("title", title);
       
        
        
        this.response = given()
                        .contentType(ContentType.JSON)
                        .body(object.toJSONString())
                        .when()
                        .put("http://localhost:4567/projects/"+this.id);
    }

    @Then("the response body should contain the updated project item with title {string} and description {string} and completed {string}")
    public void the_response_body_should_contain_the_updated_project_item_with_title_and_description_and_completed(String title, String description, String completed) {
    	JsonPath jsonResponse = response.jsonPath();
        assertEquals(title, jsonResponse.get("title"));
        assertEquals(Boolean.parseBoolean(completed), Boolean.parseBoolean(jsonResponse.get("completed").toString()));
        assertEquals(description, jsonResponse.get("description"));
    }

    @When("a request is sent to update a project item with id {string}")
    public void a_request_is_sent_to_update_a_project_item_with_id(String id) {
    	this.response = given()
                .contentType(ContentType.JSON)
                .when()
                .put("http://localhost:4567/projects/"+this.id);
    }
    
    
	
	
	
	


    
}