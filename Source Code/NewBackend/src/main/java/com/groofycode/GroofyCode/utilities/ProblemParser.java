package com.groofycode.GroofyCode.utilities;

import com.groofycode.GroofyCode.dto.Game.ProblemDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProblemParser {

    public ResponseEntity<Object> parseFullProblem(String contestId, String problemId) throws Exception {
        // Connect to the URL and parse the HTML document
        Document doc = Jsoup.connect("https://codeforces.com/problemset/problem/" + contestId + "/" + problemId).get();

        // Select the first element with the class 'problem-statement'
        Element statement = doc.selectFirst(".problem-statement");
        assert statement != null;

        // Prepare the response map
        Map<String, Object> responseMap = new HashMap<>();

        // Populate the map with structured text data from different specifications
        responseMap.put("header", getHeaderText(statement.selectFirst(".header")));
        responseMap.put("statement", getChildTexts(statement.child(1))); // Assuming the second child is required
        responseMap.put("input", getChildTexts(doc.selectFirst(".input-specification")));
        responseMap.put("output", getChildTexts(doc.selectFirst(".output-specification")));
        responseMap.put("sampleTests", getSampleTests(doc)); // Modified to handle while loop logic
        responseMap.put("notes", getChildTexts(doc.selectFirst(".note")));

        // Return the map as JSON
        return ResponseEntity.ok(responseMap);
    }
    private List<String> getHeaderText(Element element) {
        List<String> texts = new ArrayList<>();
        if (element != null) {
            Elements children = element.children();
            for (int i = 0; i < children.size(); i++) {
                Element titleElement = children.get(i);
                String title = titleElement.text();
                texts.add(title);
            }
        }
        return texts;
    }
    private List<String> getChildTexts(Element element) {
        List<String> texts = new ArrayList<>();
        if (element != null) {
            Elements children = element.children();
            for (Element child : children) {
                // Check if the child element contains an <img> tag
//                Elements images = child.getElementsByTag("img");
//                Elements sup = child.getElementsByClass("upper-index");
//                if (!images.isEmpty()) {
//                    // If there are images, format and add their URLs to the texts
//                    for (Element img : images) {
//                        String imgUrl = img.absUrl("src");  // Use absUrl to get the absolute URL
//                        texts.add("IMAGELINKGROOFYCODE:" + imgUrl);
//                    }
//                }
//                if (!sup.isEmpty()) {
//                    for (Element s : sup) {
//                        String supText = s.text();
//                        texts.add("SUPTEXTGROOFYCODE:" + supText);
//                    }
//                }
//                // Check and replace math symbols
                String textContent = replaceMathSymbols(child.html());
                texts.add(textContent);
//                texts.add(child.html());
            }
        }
        return texts;
    }

    // Helper method to replace $$$ with MathJax tags
    private String replaceMathSymbols(String text) {
        String pattern = "\\$\\$\\$(.+?)\\$\\$\\$";
        // Properly escaping $ signs for the replacement string
        return text.replaceAll(pattern, "\\$\\$$1\\$\\$");
    }

    private List<List<String>> getSampleTests(Document doc) {
        List<List<String>> groupedTests = new ArrayList<>();
        List<String> firstCase = new ArrayList<>();
        StringBuilder tests = new StringBuilder();
        int i = 0;
        while (true) {
            Elements testLines = doc.select(".test-example-line-" + i);
            if (testLines.isEmpty()) {
                break; // If no elements are found for this index, break the loop
            }
            List<String> testGroup = new ArrayList<>();
            for (Element line : testLines) {
                tests.append(line.text()).append("\n");
            }

            i++;
        }
        if (!tests.isEmpty()) {
            firstCase.add(tests.toString());
        }

        // Check if no standard format tests were found, then try to find sample-test format
        Elements sampleTests = doc.select(".sample-test");
        if (firstCase.isEmpty()) {
            for (Element sampleTest : sampleTests) {
                // Select input and output pairs
                Elements inputs = sampleTest.select(".input pre");
                Elements outputs = sampleTest.select(".output pre");

                // Assuming that each input has a corresponding output
                for (int j = 0; j < inputs.size(); j++) {
                    List<String> testGroup = new ArrayList<>();
                    String inputText = inputs.get(j).html().replace("<br>", "\n");
                    String outputText = outputs.get(j).html().replace("<br>", "\n");
                    testGroup.add(inputText);
                    testGroup.add(outputText);
                    groupedTests.add(testGroup);
                }
            }
        } else {
            for (Element sampleTest : sampleTests) {
                StringBuilder testGroup = new StringBuilder();
                Elements outputs = sampleTest.select(".output pre");

                // Assuming that each input has a corresponding output
                for (int j = 0; j < outputs.size(); j++) {
                    String outputText = outputs.get(j).html().replace("<br>", "\n");
                    testGroup.append(outputText).append("\n");
                }
                String testGroupString = testGroup.toString();
                firstCase.add(testGroupString);
                groupedTests.add(firstCase);
            }
        }
        return groupedTests;
    }

    public ArrayList<String> parseSolvedProblemCount(String problemUrl) throws Exception {
        while (true) {
            try {
                Document doc = Jsoup.connect(problemUrl).get();

                Element table = doc.select(".problems").get(0);

                ArrayList<String> answer = new ArrayList<>();
                for (Element row : table.select("tr")) {
                    if (row.select("td").isEmpty()) continue;
                    Elements cols = row.select("td");
                    String lastTdText = cols.get(cols.size() - 1).text().trim(); // Get trimmed text
                    // Check if the text is empty or null
                    if (lastTdText.isEmpty()) {
                        answer.add("0");
                    } else {
                        answer.add(lastTdText.substring(1)); // Assuming you want to skip the first character
                    }
                }
                return answer;
            } catch (Exception e) {
                System.out.println("Couldn't connect to problem url: " + problemUrl);
                Thread.sleep(20000);
            }
        }

    }

    public ProblemDTO parseCodeforcesUrl(String url) {
        String baseUrl = "https://codeforces.com/contest/";

        // Check if the URL starts with the expected base URL
        if (!url.startsWith(baseUrl)) {
            throw new IllegalArgumentException("Invalid Codeforces URL: " + url);
        }

        // Remove the base URL part
        String remainingUrl = url.substring(baseUrl.length());

        // Split the remaining URL by "/problem/"
        String[] parts = remainingUrl.split("/problem/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Codeforces URL: " + url);
        }

        try {
            String contestId = parts[0];
            String problemIndex = parts[1];
            ProblemDTO problemDTO = new ProblemDTO();
            problemDTO.setContestId(contestId);
            problemDTO.setIndex(problemIndex);
            return problemDTO;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid contest ID in URL: " + url, e);
        }
    }
}
