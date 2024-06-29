package com.groofycode.GroofyCode.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groofycode.GroofyCode.dto.Game.ProblemSubmitDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.utilities.MatchStatusMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

@Component
public class CodeforcesSubmissionService {
    private final MatchStatusMapper matchStatusMapper;
    private final WebDriver driver;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CodeforcesSubmissionService(MatchStatusMapper matchStatusMapper, SimpMessagingTemplate messagingTemplate) {
        this.matchStatusMapper = matchStatusMapper;
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
        driver = new ChromeDriver(options);
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.messagingTemplate = messagingTemplate;
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

    public Integer submitCode(String problemUrl, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        messagingTemplate.convertAndSendToUser(userInfo.getUsername(), "/code", "Queueing...");
        // Navigate to the Codeforces login page
        driver.get("https://codeforces.com/enter");
        // Find the username and password input fields, and fill them with your credentials
        if (!isLoggedIn()) {
            WebElement usernameField = driver.findElement(By.name("handleOrEmail"));
            WebElement passwordField = driver.findElement(By.name("password"));
            usernameField.sendKeys("GroofyCode");
            passwordField.sendKeys("grfP123)(");

            // Submit the login form
            passwordField.submit();
        }
        // Wait for a few seconds for the page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Navigate to the problem page
        driver.get(problemUrl);

        // Find the language dropdown and select the desired language
        WebElement languageDropdown = driver.findElement(By.name("programTypeId"));
        languageDropdown.sendKeys(problemSubmitDTO.getLanguage());

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
            bufferedWriter.write(problemSubmitDTO.getCode());

            // Always close the writer
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WebElement uploadFileBtn = driver.findElement(By.xpath("//input[@name='sourceFile']"));
        uploadFileBtn.sendKeys(file.getAbsolutePath());

        messagingTemplate.convertAndSendToUser(userInfo.getUsername(), "/code", "Submitting...");

        // Submit the code
        WebElement submitButton = driver.findElement(By.xpath("//input[@value='Submit']"));
        submitButton.submit();

        if (driver.getCurrentUrl().startsWith(problemUrl)) {
            messagingTemplate.convertAndSendToUser(userInfo.getUsername(), "/code", "");
            throw new Exception("Duplicated source code. Please try again.");
        }

        messagingTemplate.convertAndSendToUser(userInfo.getUsername(), "/code", "Testing...");

        String apiUrl = "https://codeforces.com/api/user.status?handle=GroofyCode&count=1";
        int timeoutInSeconds = 60; // Adjust timeout as needed
        long startTime = System.currentTimeMillis();
        String verdict;

        do {
            // Check if timeout has elapsed
            if ((System.currentTimeMillis() - startTime) > timeoutInSeconds * 1000) {
                throw new Exception("Timeout while waiting for submission result");
            }

            // Wait for a few seconds before checking again
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Get the submission status from the API
            String response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode resultNode = jsonResponse.get("result").get(0);
            verdict = resultNode.get("verdict").asText();

        } while (verdict.equals("TESTING"));
        System.out.println(verdict);
        file.delete();

        messagingTemplate.convertAndSendToUser(userInfo.getUsername(), "/code", "");
        return matchStatusMapper.getStatusStringToInt().get(verdict);

        // Close the browser after submission
        //        driver.quit();
    }
}