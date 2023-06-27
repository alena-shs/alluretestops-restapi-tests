package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

import static tests.TestData.*;

public class Specs extends TestBase{
    public static RequestSpecification request = with()
            .filter(withCustomTemplates())
            .baseUri("https://allure.autotests.cloud")
            .basePath("/api/rs")
            .header("X-XSRF-TOKEN", cookieMap.get("XSRF-TOKEN"))
            .cookies("XSRF-TOKEN", cookieMap.get("XSRF-TOKEN"),
                    "ALLURE_TESTOPS_SESSION", cookieMap.get("ALLURE_TESTOPS_SESSION"))
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .build();
    public static ResponseSpecification deleteResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(204)
            .build();
    public static ResponseSpecification deletedProjectResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(403)
            .build();

}
