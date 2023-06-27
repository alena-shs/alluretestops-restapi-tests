package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static tests.TestData.*;

public class AllureTestOpsTests extends TestBase{

    @Test
    @DisplayName("Delete a new project (with creation)")
    void createProject() {
        CreateProjectBody createProjectBody = new CreateProjectBody();
        createProjectBody.setName(projectName);
        createProjectBody.setAbbr(projectAbbr);
        createProjectBody.setIsPublic(false);

        CreateProjectResponse createProjectResponse = step("Create a project", () ->
                given(Specs.request)
                        .body(createProjectBody)
                        .when()
                        .post("/project")
                        .then()
                        .spec(Specs.responseSpec)
                        .extract().as(CreateProjectResponse.class));
        step("Verify that the new project has the correct name", () ->
                assertThat(createProjectResponse.getName()).isEqualTo(projectName));
        step("Verify the correct short name", () ->
                assertThat(createProjectResponse.getAbbr()).isEqualTo(projectAbbr));

        Long idToDelete = createProjectResponse.getId();
        step("Delete the created project", () ->
                given(Specs.request)
                        .when()
                        .delete("/project/"+idToDelete)
                        .then()
                        .spec(Specs.deleteResponseSpec));

        step("Verify that the project is deleted", () ->
                given(Specs.request)
                        .when()
                        .get("/project/"+idToDelete)
                        .then()
                        .spec(Specs.deletedProjectResponseSpec));
    }


    @Test
    @DisplayName("Edit test case name")
    void editTestCase() {
        CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
        testCaseBody.setName(testCaseName);
        CreateTestCaseBody editedTestCaseBody = new CreateTestCaseBody();
        editedTestCaseBody.setName(editedTestCaseName);

        CreateTestCaseResponse createTestCaseResponse = step("Create a test case", () ->
                given(Specs.request)
                        .body(testCaseBody)
                        .queryParam("projectId", projectId)
                        .when()
                        .post("/testcasetree/leaf")
                        .then()
                        .spec(Specs.responseSpec)
                        .extract().as(CreateTestCaseResponse.class));

        step("Verify that the initial test case name is correct", () ->
                assertThat(createTestCaseResponse.getName()).isEqualTo(testCaseName));
        step("Verify that the new test case status is a default one", () ->
                assertThat(createTestCaseResponse.getStatusName()).isEqualTo(defaultTestCaseStatus));
        step("Verify that the new test case color is a default one", () ->
                assertThat(createTestCaseResponse.getStatusColor()).isEqualTo(defaultTestCaseColor));

        HashMap<String, String> editRequestParams = new HashMap<>();
        editRequestParams.put("projectId", projectId);
        editRequestParams.put("leafId", createTestCaseResponse.getId().toString());

        CreateTestCaseResponse editedTestCaseResponse = step("Edit the test case name",  () ->
            given(Specs.request)
                    .body(editedTestCaseBody)
                    .queryParams(editRequestParams)
                    .when()
                    .post("/testcasetree/leaf/rename")
                    .then()
                    .spec(Specs.responseSpec)
                    .extract().as(CreateTestCaseResponse.class));

        step("Verify that the case with a correct ID was changed", () ->
                assertThat(editedTestCaseResponse.getId()).isEqualTo(createTestCaseResponse.getId()));
        step("Verify that the new test case name is correct", () ->
                assertThat(editedTestCaseResponse.getName()).isEqualTo(editedTestCaseName));
        step("Verify that test case status didn't change", () ->
                assertThat(editedTestCaseResponse.getStatusName()).isEqualTo(createTestCaseResponse.getStatusName()));
        step("Verify that test case color didn't change", () ->
                assertThat(editedTestCaseResponse.getStatusColor()).isEqualTo(createTestCaseResponse.getStatusColor()));
    }


    @Test
    @DisplayName("Edit steps in the test case")
    void addStepsToTestCase() {
        CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
        testCaseBody.setName(testCaseName);

        CreateTestCaseResponse createTestCaseResponse = step("Create testcase", () ->
                given(Specs.request)
                    .body(testCaseBody)
                    .queryParam("projectId", projectId)
                    .when()
                    .post("/testcasetree/leaf")
                    .then()
                    .spec(Specs.responseSpec)
                    .extract().as(CreateTestCaseResponse.class));

        step("Verify that the new project has the correct name", () ->
                assertThat(createTestCaseResponse.getName()).isEqualTo(testCaseName));
        step("Verify that the new test case status is a default one", () ->
                assertThat(createTestCaseResponse.getStatusName()).isEqualTo(defaultTestCaseStatus));
        step("Verify that the new test case color is a default one", () ->
                assertThat(createTestCaseResponse.getStatusColor()).isEqualTo(defaultTestCaseColor));

        AddTestStepBody.ListStepsData step = new AddTestStepBody.ListStepsData();
        step.setName(testStepName);
        step.setSpacing("");

        AddTestStepBody addStepBody = new AddTestStepBody();
        List<AddTestStepBody.ListStepsData> listSteps = new ArrayList<>();
        listSteps.add(step);
        addStepBody.setSteps(listSteps);
        addStepBody.setWorkPath(0);

        AddTestStepResponse addTestStepResponse = step("Add a step to the test case",  () ->
            given(Specs.request)
                    .body(addStepBody)
                    .when()
                    .post("/testcase/"+createTestCaseResponse.getId()+"/scenario")
                    .then()
                    .spec(Specs.responseSpec)
                    .extract().as(AddTestStepResponse.class));

        AddTestStepBody.ListStepsData editedStep = new AddTestStepBody.ListStepsData();
        editedStep.setName(renewedStepName);
        editedStep.setSpacing("");
        System.out.println(editedStep.getName());

        AddTestStepBody editStepBody = new AddTestStepBody();
        List<AddTestStepBody.ListStepsData> listNewSteps = new ArrayList<>();
        listNewSteps.add(editedStep);
        editStepBody.setSteps(listNewSteps);
        editStepBody.setWorkPath(0);

        step("Edit a created step in the test case",  () -> {
            given(Specs.request)
                    .body(editStepBody)
                    .when()
                    .post("/testcase/"+createTestCaseResponse.getId()+"/scenario")
                    .then()
                    .spec(Specs.responseSpec)
                    .extract().as(AddTestStepResponse.class);
        });
    }
}
