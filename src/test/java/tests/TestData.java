package tests;

import com.github.javafaker.Faker;
import pages.LoginPage;

import java.util.HashMap;

public class TestData {
    static Faker faker = new Faker();
    static String login = "allure8",
            password = "allure8",
            projectId = "2276",
            projectName = (faker.rickAndMorty().character()+"-Ailinyan"),
            projectAbbr = "A1",
            defaultTestCaseStatus = "Draft",
            defaultTestCaseColor = "#abb8c3",
            testCaseName = faker.name().fullName(),
            editedTestCaseName = faker.name().title(),
            testStepName = faker.shakespeare().asYouLikeItQuote(),
            renewedStepName = faker.shakespeare().hamletQuote();
    static HashMap<String, String> cookieMap = LoginPage.login(login, password);
}
