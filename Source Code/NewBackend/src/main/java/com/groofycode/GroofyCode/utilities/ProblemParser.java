package com.groofycode.GroofyCode.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProblemParser {

    public ResponseEntity<Object> parseFullProblem() throws Exception {
        // Connect to the URL and parse the HTML document
        Document doc = Jsoup.connect("https://codeforces.com/contest/1978/problem/A").get();

        // Select the first element with the class 'problem-statement'
        Element statement = doc.selectFirst(".problem-statement");
        assert statement != null;

        // Prepare the response map
        Map<String, Object> responseMap = new HashMap<>();

        // Populate the map with structured text data from different specifications
        responseMap.put("header", getChildTexts(statement.selectFirst(".header")));
        responseMap.put("statement", getChildTexts(statement.child(1))); // Assuming the second child is required
        responseMap.put("input", getChildTexts(doc.selectFirst(".input-specification")));
        responseMap.put("output", getChildTexts(doc.selectFirst(".output-specification")));
        responseMap.put("sampleTests", getSampleTests(doc)); // Modified to handle while loop logic
        responseMap.put("notes", getChildTexts(doc.selectFirst(".note")));

        // Return the map as JSON
        return ResponseEntity.ok(responseMap);
    }

    private List<String> getChildTexts(Element element) {
        List<String> texts = new ArrayList<>();
        if (element != null) {
            Elements children = element.children();
            for (Element child : children) {
                // Check if the child element contains an <img> tag
                Elements images = child.getElementsByTag("img");
                if (!images.isEmpty()) {
                    // If there are images, format and add their URLs to the texts
                    for (Element img : images) {
                        String imgUrl = img.absUrl("src");  // Use absUrl to get the absolute URL
                        texts.add("IMAGELINKGROOFYCODE:" + imgUrl);
                    }
                }
                // Check and replace math symbols
                String textContent = replaceMathSymbols(child.text());
                texts.add(textContent);
            }
        }
        return texts;
    }

    // Helper method to replace $$$ with MathJax tags
    private String replaceMathSymbols(String text) {
        String pattern = "\\$\\$\\$(.+?)\\$\\$\\$";
        return text.replaceAll(pattern, "<MathJax>{\"$1\"}</MathJax>");
    }

    private List<List<String>> getSampleTests(Document doc) {
        List<List<String>> groupedTests = new ArrayList<>();
        int i = 0;
        while (true) {
            Elements testLines = doc.select(".test-example-line-" + i);
            if (testLines.isEmpty()) {
                break; // If no elements are found for this index, break the loop
            }
            List<String> testGroup = new ArrayList<>();
            for (Element line : testLines) {
                testGroup.add(line.text());
            }
            groupedTests.add(testGroup);
            i++;
        }
        return groupedTests;
    }

    public ArrayList<String> parseSolvedProblemCount(String problemUrl) throws Exception{
        while (true){
            try {
                Document doc = Jsoup.connect(problemUrl).get();

                Element table = doc.select(".problems").get(0);

                ArrayList<String> answer = new ArrayList<>();
                for(Element row : table.select("tr")) {
                    if(row.select("td").isEmpty())continue;
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
            }
            catch(Exception e){
                System.out.println("Couldn't connect to problem url: " + problemUrl);
                Thread.sleep(20000);
            }
        }

    }
}
