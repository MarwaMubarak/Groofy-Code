package com.groofycode.GroofyCode.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

@Service
public class CodeforcesSubmissionService {
    private final WebDriver driver;

    public CodeforcesSubmissionService() {
        // Set the path to your ChromeDriver executable
//        System.setProperty("webdriver.chrome.driver", "C:/Program Files/Google/Chrome/Application/chrome.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Ensure the browser runs in headless mode
        options.addArguments("--disable-gpu"); // GPU hardware acceleration isn't necessary for headless
        options.addArguments("--window-size=1920,1200"); // Set a window size that supports most websites
        options.addArguments("--ignore-certificate-errors"); // Optional: if dealing with self-signed SSL certificates
        options.addArguments("--disable-extensions"); // Disable extensions to minimize resource consumption
        options.addArguments("--no-sandbox"); // Disable the sandbox for Chrome if running as root in a very constrained environment (not recommended)
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
        driver = new ChromeDriver();
    }

    private boolean isLoggedIn() {
        try {
            WebElement profileElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, '/profile')]")));
            return profileElement != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void login(String username, String password) {
        driver.get("https://codeforces.com/enter");
        WebElement usernameField = driver.findElement(By.name("handleOrEmail"));
        WebElement passwordField = driver.findElement(By.name("password"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        passwordField.submit();
    }

    public void submitCode(String problemUrl, String language, String code) {
        // Navigate to the Codeforces login page
        driver.get("https://codeforces.com/enter");
        // Find the username and password input fields, and fill them with your credentials
        if (!isLoggedIn()) {
            WebElement usernameField = driver.findElement(By.name("handleOrEmail"));
            WebElement passwordField = driver.findElement(By.name("password"));
            usernameField.sendKeys("MaSLN");
            passwordField.sendKeys("devilofhellskitchen");

            // Submit the login form
            passwordField.submit();
        }
        // Wait for a few seconds for the page to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Navigate to the problem page
        driver.get(problemUrl);

        // Find the language dropdown and select the desired language
        WebElement languageDropdown = driver.findElement(By.name("programTypeId"));
        languageDropdown.sendKeys(language);

        // Find the button to upload code file
        File file = new File("code.cpp");
        try {
            // If the file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // Use FileWriter with append mode (true)
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the code to the file
            bufferedWriter.write(code);

            // Always close the writer
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WebElement uploadFileBtn = driver.findElement(By.xpath("//input[@name='sourceFile']"));
        uploadFileBtn.sendKeys(file.getAbsolutePath());

        // Submit the code
        WebElement submitButton = driver.findElement(By.xpath("//input[@value='Submit']"));
        submitButton.submit();

        if (driver.getCurrentUrl().startsWith(problemUrl)) {
            System.out.println("Submission failed");
        } else {
            System.out.println("Submission successful");
        }

        file.delete();


        // Close the browser after submission
//        driver.quit();
    }
}