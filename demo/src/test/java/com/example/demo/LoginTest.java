package com.example.demo;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions; // ✅ Add this line
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.util.List;


public class LoginTest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        extent = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/LoginTestReport.html");
        extent.attachReporter(reporter);
    }

    @BeforeMethod
public void setup() {
    WebDriverManager.chromedriver().setup();

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // Run in headless mode
    options.addArguments("--no-sandbox"); // Recommended for Docker
    options.addArguments("--disable-dev-shm-usage"); // Recommended for Docker
    options.addArguments("--disable-gpu"); // Often required in Linux
    options.addArguments("--remote-allow-origins=*"); // Fixes remote origin issues

    driver = new ChromeDriver(options);
    driver.manage().window().maximize();
    driver.get("https://the-internet.herokuapp.com/login");
}


    @Test
    public void testLoginWithExcelData() {
        List<String[]> data = ExcelUtil.readLoginData("src/test/resources/data.xlsx");

        for (String[] credentials : data) {
            String username = credentials[0];
            String password = credentials[1];

            test = extent.createTest("Login Test with: " + username);

            try {
                WebElement userField = driver.findElement(By.id("username"));
                WebElement passField = driver.findElement(By.id("password"));
                WebElement loginBtn = driver.findElement(By.cssSelector("button[type='submit']"));

                userField.clear();
                passField.clear();
                userField.sendKeys(username);
                passField.sendKeys(password);
                loginBtn.click();

                WebElement message = driver.findElement(By.id("flash"));
                if (message.getText().contains("You logged into a secure area")) {
                    test.pass("Login successful for: " + username);
                } else {
                    test.fail("Login failed for: " + username);
                }
            } catch (Exception e) {
                test.fail("Exception during login test: " + e.getMessage());
            }

            driver.get("https://the-internet.herokuapp.com/logout");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        extent.flush();
    }
}
