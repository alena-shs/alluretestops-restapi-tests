package pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class LoginPage {

    public static HashMap<String, String> login(String login, String password) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://allure.autotests.cloud");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement usernameField = driver.findElement(By.name("username")),
                passwordField = driver.findElement(By.name("password")),
                submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            usernameField.sendKeys(login);
            passwordField.sendKeys(password);
            submitButton.click();
            driver.findElement(By.name("logo-icon")).click();

            Set<Cookie> allcookies = driver.manage().getCookies();
            System.out.println("Size: " + allcookies.size());

            Iterator<Cookie> itr = allcookies.iterator();
            HashMap<String, String> cookiesMap = new HashMap<String, String>();
            while (itr.hasNext()) {
                Cookie cookie = itr.next();
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
            driver.quit();
        return cookiesMap;
    }
}
