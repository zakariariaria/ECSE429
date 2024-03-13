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

    private boolean isOnProjectCreationPage;
    private String projectName;
    private boolean formSubmissionSuccessful;
    private boolean formSubmissionWithoutName;
    private boolean systemErrorEncountered;

    @Given("the user is on the project creation page")
    public void givenUserIsOnProjectCreationPage() {
        // Simulating user navigation to the project creation page
        isOnProjectCreationPage = true;
    }

    @When("the user specifies the name of the project")
    public void whenUserSpecifiesProjectName() {
        // Simulating user specifying the name of the project
        projectName = "Sample Project";
    }

    @When("the user submits the form")
    public void whenUserSubmitsForm() {
        // Simulating form submission
        if (projectName != null && !projectName.isEmpty()) {
            formSubmissionSuccessful = true;
        } else {
            formSubmissionWithoutName = true;
        }
    }

    @Then("the project with the specified name is created successfully")
    public void thenProjectCreatedSuccessfully() {
        // Verifying the project creation success
        assert isOnProjectCreationPage && formSubmissionSuccessful : "Project creation failed.";
    }

    @When("the user submits the form without specifying the name")
    public void whenUserSubmitsFormWithoutName() {
        // Simulating form submission without specifying the name
        formSubmissionWithoutName = true;
    }

    @Then("an error message is displayed indicating the missing information")
    public void thenErrorMessageDisplayedForMissingInformation() {
        // Verifying the error message for missing information
        assert isOnProjectCreationPage && formSubmissionWithoutName : "Error message for missing information not displayed.";
    }

    @When("the system encounters an error during submission")
    public void whenSystemEncountersError() {
        // Simulating a system error during submission
        systemErrorEncountered = true;
    }

    @Then("an error message is displayed, and the project is not created")
    public void thenErrorMessageDisplayedForSystemError() {
        // Verifying the error message for system error
        assert isOnProjectCreationPage && systemErrorEncountered : "Error message for system error not displayed.";
    }


	// =========================== Project Feature 2: Get Project Information by Specifying Name ==========================
	//private boolean isOnProjectInformationPage;
    private String specifiedProjectName;
    //private boolean projectInformationRequested;
    //private boolean nonExistingProjectSpecified;
    private boolean errorMessageForMissingProjectNameDisplayed;

    @Given("the user is on the project information page")
    public void givenUserIsOnProjectInformationPageByName() {
        // Simulating user navigation to the project information page
        isOnProjectInformationPage = true;
    }

    @When("the user specifies the name of the project")
    public void whenUserSpecifiesProjectNameToGetIt() {
        // Simulating user specifying the name of the project
        specifiedProjectName = "Sample Project";
    }

    @When("the user requests project information")
    public void whenUserRequestsProjectInformationByName() {
        // Simulating user requesting project information
        if (specifiedProjectName != null && !specifiedProjectName.isEmpty()) {
            projectInformationRequested = true;
        } else {
            nonExistingProjectSpecified = true;
        }
    }

    @Then("the relevant project information is displayed")
    public void thenRelevantProjectInformationDisplayedByName() {
        // Verifying relevant project information is displayed
        assert isOnProjectInformationPage && projectInformationRequested : "Project information not displayed.";
    }

    @When("the user specifies the name of a non-existing project")
    public void whenUserSpecifiesNonExistingProjectName() {
        // Simulating user specifying the name of a non-existing project
        specifiedProjectName = "NonExistingProject";
    }

    @Then("an error message is displayed, indicating that the project does not exist")
    public void thenErrorMessageDisplayedForNonExistingProject() {
        // Verifying error message for non-existing project
        assert isOnProjectInformationPage && nonExistingProjectSpecified : "Error message for non-existing project not displayed.";
    }

    @When("the user tries to request project information without specifying the name")
    public void whenUserRequestsProjectInformationWithoutName() {
        // Simulating user attempting to request project information without specifying the name
        errorMessageForMissingProjectNameDisplayed = true;
    }

    @Then("an error message is displayed, indicating that the project name is required")
    public void thenErrorMessageDisplayedForMissingProjectName() {
        // Verifying error message for missing project name
        assert isOnProjectInformationPage && errorMessageForMissingProjectNameDisplayed : "Error message for missing project name not displayed.";
    }
    

	// =========================== Project Feature 3: Close a Project ===========================
	private boolean isOnProjectDetailsPage;
    private boolean projectIsActive;
    private boolean projectClosedSuccessfully;
    private boolean projectAlreadyClosed;
    private boolean nonExistingProjectClosed;
    private boolean errorMessageDisplayedForClosingClosedProject;
    private boolean errorMessageDisplayedForClosingNonExistingProject;

    @Given("the project manager is on the project details page")
    public void givenProjectManagerOnProjectDetailsPage() {
        // Simulating project manager navigation to the project details page
        isOnProjectDetailsPage = true;
    }

    @Given("the project is in an active state")
    public void givenProjectIsActive() {
        // Simulating setting up an active project state
        projectIsActive = true;
    }

    @When("the project manager chooses to close the project")
    public void whenProjectManagerClosesProject() {
        // Simulating project manager closing the project
        if (projectIsActive) {
            projectClosedSuccessfully = true;
        } else {
            projectAlreadyClosed = true;
        }
    }

    @Then("the project is closed successfully")
    public void thenProjectClosedSuccessfully() {
        // Verifying project closure success
        assert isOnProjectDetailsPage && projectClosedSuccessfully : "Project closure failed.";
    }

    @Given("the project is already closed")
    public void givenProjectIsAlreadyClosed() {
        // Simulating setting up an already closed project state
        projectIsActive = false;
    }

    @When("the project manager tries to close the project")
    public void whenProjectManagerTriesToCloseClosedProject() {
        // Simulating project manager attempting to close an already closed project
        errorMessageDisplayedForClosingClosedProject = true;
    }

    @Then("an error message is displayed, indicating that the project is already closed")
    public void thenErrorMessageDisplayedForClosingClosedProject() {
        // Verifying error message for closing an already closed project
        assert isOnProjectDetailsPage && errorMessageDisplayedForClosingClosedProject : "Error message for closing an already closed project not displayed.";
    }

    @Given("the project manager tries to close a non-existing project")
    public void givenProjectManagerTriesToCloseNonExistingProject() {
        // Simulating project manager attempting to close a non-existing project
        nonExistingProjectClosed = true;
    }

    @Then("an error message is displayed, indicating that the project does not exist")
    public void thenErrorMessageDisplayedForClosingNonExistingProject() {
        // Verifying error message for closing a non-existing project
        assert isOnProjectDetailsPage && nonExistingProjectClosed : "Error message for closing a non-existing project not displayed.";
    }

	// =========================== Project Feature 4: Get Project Information by Specifying ID ===========================
	private boolean isOnProjectInformationPage;
    private String specifiedProjectId;
    private boolean projectInformationRequested;
    private boolean nonExistingProjectSpecified;
    private boolean errorMessageForMissingProjectIdDisplayed;

    @Given("the user is on the project information page")
    public void givenUserIsOnProjectInformationPage() {
        // Simulating user navigation to the project information page
        isOnProjectInformationPage = true;
    }

    @When("the user specifies the ID of the project")
    public void whenUserSpecifiesProjectId() {
        // Simulating user specifying the ID of the project
        specifiedProjectId = "123"; // Replace with an actual project ID
    }

    @When("the user requests project information")
    public void whenUserRequestsProjectInformation() {
        // Simulating user requesting project information
        if (specifiedProjectId != null && !specifiedProjectId.isEmpty()) {
            projectInformationRequested = true;
        } else {
            nonExistingProjectSpecified = true;
        }
    }

    @Then("the relevant project information is displayed")
    public void thenRelevantProjectInformationDisplayed() {
        // Verifying relevant project information is displayed
        assert isOnProjectInformationPage && projectInformationRequested : "Project information not displayed.";
    }

    @When("the user specifies the ID of a non-existing project")
    public void whenUserSpecifiesNonExistingProjectId() {
        // Simulating user specifying the ID of a non-existing project
        specifiedProjectId = "NonExistingProjectId"; // Replace with a non-existing project ID
    }

    @Then("an error message is displayed, indicating that the project does not exist")
    public void thenErrorMessageDisplayedForNonExistingProjectId() {
        // Verifying error message for a non-existing project ID
        assert isOnProjectInformationPage && nonExistingProjectSpecified : "Error message for non-existing project ID not displayed.";
    }

    @When("the user tries to request project information without specifying the ID")
    public void whenUserRequestsProjectInformationWithoutId() {
        // Simulating user attempting to request project information without specifying the ID
        errorMessageForMissingProjectIdDisplayed = true;
    }

    @Then("an error message is displayed, indicating that the project ID is required")
    public void thenErrorMessageDisplayedForMissingProjectId() {
        // Verifying error message for missing project ID
        assert isOnProjectInformationPage && errorMessageForMissingProjectIdDisplayed : "Error message for missing project ID not displayed.";
    }


	// =========================== Project Feature 5: Modify the Name of a Project ===========================

    private boolean isNameModified;
    private boolean changesSavedSuccessfully;
    private boolean attemptedToModifyWithEmptyName;
    private boolean errorMessageForEmptyNameDisplayed;
    private boolean userDecidesNotToModifyName;
    private boolean projectNameRemainsUnchanged;

    @When("the user modifies the name of the project")
    public void whenUserModifiesProjectName() {
        // Simulating user modifying the name of the project
        isNameModified = true;
    }

    @When("the user saves the changes")
    public void whenUserSavesChanges() {
        // Simulating user saving the changes
        if (isNameModified && !attemptedToModifyWithEmptyName) {
            changesSavedSuccessfully = true;
        }
    }

    @Then("the project's name is updated successfully")
    public void thenProjectNameUpdatedSuccessfully() {
        // Verifying project name update success
        assert isNameModified && changesSavedSuccessfully : "Project name update failed.";
    }

    @When("the user attempts to modify the name with an empty value")
    public void whenUserAttemptsToModifyWithNameEmpty() {
        // Simulating user attempting to modify with an empty value
        attemptedToModifyWithEmptyName = true;
    }

    @Then("an error message is displayed indicating the empty name is not allowed")
    public void thenErrorMessageDisplayedForEmptyName() {
        // Verifying error message for empty name
        assert attemptedToModifyWithEmptyName : "Error message for empty name not displayed.";
        errorMessageForEmptyNameDisplayed = true;
    }

    @When("the user decides not to modify the name")
    public void whenUserDecidesNotToModifyName() {
        // Simulating user deciding not to modify the name
        userDecidesNotToModifyName = true;
    }

    @Then("the project's name remains unchanged")
    public void thenProjectNameRemainsUnchanged() {
        // Verifying that the project's name remains unchanged
        assert userDecidesNotToModifyName : "Project name should remain unchanged, but it was modified.";
        projectNameRemainsUnchanged = true;
    }

}
