package com.groofycode.GroofyCode.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Vector;

@Service
public class ProblemFetcher {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private ProblemParser problemParser;

    @Autowired
    public ProblemFetcher(ProblemParser problemParser){
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.problemParser = problemParser;
    }


    public ResponseEntity<Object> tempFetchProblems() throws Exception {
        String csvFile = "problems1.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        String[] header = { "name", "solvedCount", "contestId", "index"};
        writer.writeNext(header);

        String apiUrl = "https://codeforces.com/api/contest.list?gym=false";
        String response = restTemplate.getForObject(apiUrl, String.class);
        JsonNode jsonResponse = objectMapper.readTree(response);
        JsonNode resultNode = jsonResponse.get("result");
        int current = 0;
        for(JsonNode problemNode : resultNode){
            if(!problemNode.get("phase").asText().equals("FINISHED"))continue;
            if(!problemNode.get("type").asText().equals("ICPC") && !problemNode.get("type").asText().equals("CF"))continue;
            if(current < 2000){
                current++;
                continue;
            }
            if(current == 2500)break;
            while (true) {
                try {
                    String apiContest = "https://codeforces.com/api/contest.standings?contestId=" + problemNode.get("id") + "&from=1&count=1";
                    response = restTemplate.getForObject(apiContest, String.class);
                    jsonResponse = objectMapper.readTree(response);
                    JsonNode problems = jsonResponse.get("result").get("problems");
                    ArrayList<String> solvedProblems = problemParser.parseSolvedProblemCount("https://codeforces.com/contest/" + problemNode.get("id"));
                    int num = 0;
                    for (JsonNode problem : problems) {
                        String[] data = {problem.get("name").asText(), solvedProblems.get(num), problem.get("contestId").asText(), problem.get("index").asText()};
                        writer.writeNext(data);
                        num++;
                    }
                    current++;
                    break;
                }
                catch (HttpClientErrorException e) {
                    // Handle HTTP error responses (like 404, 500, etc.)
                    try {
                        jsonResponse = objectMapper.readTree(e.getResponseBodyAsString());
                        if(jsonResponse.get("status").asText().equals("FAILED") && jsonResponse.get("comment").asText().startsWith("contestId: Contest with id"))break;
                        System.out.println("HTTP error occurred: " + e.getStatusCode());
                        System.out.println(e.getResponseBodyAsString());
                    } catch (JsonProcessingException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                }
                catch (Exception e) {
                    System.out.println("Problem fetching problems from " + problemNode.get("id").asText());
                    Thread.sleep(2000);
                }
            }
            System.out.println("Passed " + current + " contest(s) out of " + resultNode.size() + " contest(s)");
            System.out.println("Percentage: " + 100 * current / resultNode.size() + "%");
        }
        writer.close();
        return ResponseEntity.ok("DONE");
    }

    public ResponseEntity<Object> fetchProblems() throws Exception {
        String csvFile = "problems.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        String[] header = { "name", "solvedCount", "contestId", "index", "rating"};
        writer.writeNext(header);


        String apiUrl = "https://codeforces.com/api/problemset.problems";
        String response = restTemplate.getForObject(apiUrl, String.class);
        JsonNode jsonResponse = objectMapper.readTree(response);
        JsonNode problemsJSON = jsonResponse.get("result").get("problems");
        JsonNode problemStatisticsJSON = jsonResponse.get("result").get("problemStatistics");
        for(int i = 0; i < problemsJSON.size(); i++){
            if(problemsJSON.get(i).get("rating") == null)continue;
            JsonNode problem = problemsJSON.get(i);
            JsonNode problemStatistics = problemStatisticsJSON.get(i);
            String[] data = {problem.get("name").asText(), problemStatistics.get("solvedCount").asText(), problem.get("contestId").asText(), problem.get("index").asText(), problem.get("rating").asText()};
            writer.writeNext(data);
        }
        writer.close();
        return ResponseEntity.ok("DONE");
    }
}
