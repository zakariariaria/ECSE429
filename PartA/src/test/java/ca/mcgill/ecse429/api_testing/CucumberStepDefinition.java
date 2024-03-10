package ca.mcgill.ecse429.api_testing;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;

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

	


	
	// =========================== Project Feature 1: Create a Project by Specifying Name ===========================

    @Given("the user is on the project creation page")
    public void givenUserIsOnProjectCreationPage() {
        // Implementation for navigating to the project creation page
    }

    @When("the user specifies the name of the project")
    public void whenUserSpecifiesProjectName() {
        // Implementation for specifying the name of the project
    }

    @When("the user submits the form")
    public void whenUserSubmitsForm() {
        // Implementation for submitting the form
    }

    @Then("the project with the specified name is created successfully")
    public void thenProjectCreatedSuccessfully() {
        // Implementation for verifying the project creation
    }

    @When("the user submits the form without specifying the name")
    public void whenUserSubmitsFormWithoutName() {
        // Implementation for submitting the form without specifying the name
    }

    @Then("an error message is displayed indicating the missing information")
    public void thenErrorMessageDisplayedForMissingInformation() {
        // Implementation for verifying the error message
    }

    @When("the system encounters an error during submission")
    public void whenSystemEncountersError() {
        // Implementation for simulating a system error during submission
    }

    @Then("an error message is displayed, and the project is not created")
    public void thenErrorMessageDisplayedForSystemError() {
        // Implementation for verifying the error message for system error
    }


	// =========================== Project Feature 2: Get Project Information by Specifying Name ==========================
	@Given("the user is on the project information page")
    public void givenUserIsOnProjectInformationPageByName() {
        // Implementation for navigating to the project information page
    }

    @When("the user specifies the name of the project")
    public void whenUserSpecifiesProjectNameToGetIt() {
        // Implementation for specifying the name of the project
    }

    @When("the user requests project information")
    public void whenUserRequestsProjectInformationByName() {
        // Implementation for requesting project information
    }

    @Then("the relevant project information is displayed")
    public void thenRelevantProjectInformationDisplayedByName() {
        // Implementation for verifying the relevant project information is displayed
    }

    @When("the user specifies the name of a non-existing project")
    public void whenUserSpecifiesNonExistingProjectName() {
        // Implementation for specifying the name of a non-existing project
    }

    @Then("an error message is displayed, indicating that the project does not exist")
    public void thenErrorMessageDisplayedForNonExistingProject() {
        // Implementation for verifying the error message for a non-existing project
    }

    @When("the user tries to request project information without specifying the name")
    public void whenUserRequestsProjectInformationWithoutName() {
        // Implementation for attempting to request project information without specifying the name
    }

    @Then("an error message is displayed, indicating that the project name is required")
    public void thenErrorMessageDisplayedForMissingProjectName() {
        // Implementation for verifying the error message for missing project name
    }
    

	// =========================== Project Feature 3: Close a Project ===========================
	@Given("the project manager is on the project details page")
    public void givenProjectManagerOnProjectDetailsPage() {
        // Implementation for navigating to the project details page
    }

    @Given("the project is in an active state")
    public void givenProjectIsActive() {
        // Implementation for setting up an active project state
    }

    @When("the project manager chooses to close the project")
    public void whenProjectManagerClosesProject() {
        // Implementation for closing the project
    }

    @Then("the project is closed successfully")
    public void thenProjectClosedSuccessfully() {
        // Implementation for verifying the project closure
    }

    @Given("the project is already closed")
    public void givenProjectIsAlreadyClosed() {
        // Implementation for setting up an already closed project state
    }

    @When("the project manager tries to close the project")
    public void whenProjectManagerTriesToCloseClosedProject() {
        // Implementation for attempting to close an already closed project
    }

    @Then("an error message is displayed, indicating that the project is already closed")
    public void thenErrorMessageDisplayedForClosingClosedProject() {
        // Implementation for verifying the error message for closing an already closed project
    }

    @Given("the project manager tries to close a non-existing project")
    public void givenProjectManagerTriesToCloseNonExistingProject() {
        // Implementation for setting up a non-existing project scenario
    }

    @Then("an error message is displayed, indicating that the project does not exist")
    public void thenErrorMessageDisplayedForClosingNonExistingProject() {
        // Implementation for verifying the error message for closing a non-existing project
    }


	// =========================== Project Feature 4: Get Project Information by Specifying ID ===========================
	@Given("the user is on the project information page")
    public void givenUserIsOnProjectInformationPage() {
        // Implementation for navigating to the project information page
    }

    @When("the user specifies the ID of the project")
    public void whenUserSpecifiesProjectId() {
        // Implementation for specifying the ID of the project
    }

    @When("the user requests project information")
    public void whenUserRequestsProjectInformation() {
        // Implementation for requesting project information
    }

    @Then("the relevant project information is displayed")
    public void thenRelevantProjectInformationDisplayed() {
        // Implementation for verifying the relevant project information is displayed
    }

    @When("the user specifies the ID of a non-existing project")
    public void whenUserSpecifiesNonExistingProjectId() {
        // Implementation for specifying the ID of a non-existing project
    }

    @Then("an error message is displayed, indicating that the project does not exist")
    public void thenErrorMessageDisplayedForNonExistingProjectId() {
        // Implementation for verifying the error message for a non-existing project ID
    }

    @When("the user tries to request project information without specifying the ID")
    public void whenUserRequestsProjectInformationWithoutId() {
        // Implementation for attempting to request project information without specifying the ID
    }

    @Then("an error message is displayed, indicating that the project ID is required")
    public void thenErrorMessageDisplayedForMissingProjectId() {
        // Implementation for verifying the error message for missing project ID
    }


	// =========================== Project Feature 5: Modify the Name of a Project ===========================

    @When("the user modifies the name of the project")
    public void whenUserModifiesProjectName() {
        // Implementation for modifying the name of the project
    }

    @When("the user saves the changes")
    public void whenUserSavesChanges() {
        // Implementation for saving the changes
    }

    @Then("the project's name is updated successfully")
    public void thenProjectNameUpdatedSuccessfully() {
        // Implementation for verifying the project name update
    }

    @When("the user attempts to modify the name with an empty value")
    public void whenUserAttemptsToModifyWithNameEmpty() {
        // Implementation for simulating an attempt to modify with an empty value
    }

    @Then("an error message is displayed indicating the empty name is not allowed")
    public void thenErrorMessageDisplayedForEmptyName() {
        // Implementation for verifying the error message for empty name
    }

    @When("the user decides not to modify the name")
    public void whenUserDecidesNotToModifyName() {
        // Implementation for simulating the decision not to modify the name
    }

    @Then("the project's name remains unchanged")
    public void thenProjectNameRemainsUnchanged() {
        // Implementation for verifying that the project's name remains unchanged
    }

}
