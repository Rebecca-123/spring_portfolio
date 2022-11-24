package com.nighthawk.spring_portfolio.mvc.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/person")
public class PersonApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private PersonJpaRepository repository;

    /*
    GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
    GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

     /*
    * BMI Classification API
    * Endpoint parameter: BMI
     */
    private JSONObject body2; // last run result
    private HttpStatus status2; // last run status
    @GetMapping("/bmi/classification/{bmi}") // added to end of prefix as endpoint
    public ResponseEntity<JSONObject> getBMIClassification(@PathVariable int bmi) 
        throws JsonMappingException, JsonProcessingException {
        String url = "https://body-mass-index-bmi-calculator.p.rapidapi.com/weight-category?bmi=" + bmi;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", "199e385baamshc0a4c645191a179p191ebdjsn70f0155c5394")
                .header("X-RapidAPI-Host", "body-mass-index-bmi-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

             // JSONParser extracts text body and parses to JSONObject
             this.body2 = (JSONObject) new JSONParser().parse(response.body());
             this.status2 = HttpStatus.OK;  //200 success
        }
        catch(Exception e) {  // capture failure info
            HashMap<String, String> status2 = new HashMap<>();
            status2.put("status", "RapidApi failure: " + e);

            // Setup object for error
            this.body2 = (JSONObject) status2;
            this.status2 = HttpStatus.INTERNAL_SERVER_ERROR; // 500 error
        }
        return new ResponseEntity<>(body2, status2);
    }

    /*
    * BMI API
    * Height and weight in endpoint parameters
     */
    private JSONObject body; // last run result
    private HttpStatus status; // last run status
    @GetMapping("/bmi/{height}/{weight}") // added to end of prefix as endpoint
    public ResponseEntity<JSONObject> getBMI(@PathVariable int height, @PathVariable int weight) 
        throws JsonMappingException, JsonProcessingException {
        String url = "https://body-mass-index-bmi-calculator.p.rapidapi.com/imperial?weight=" + weight + "&height=" + height;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", "199e385baamshc0a4c645191a179p191ebdjsn70f0155c5394")
                .header("X-RapidAPI-Host", "body-mass-index-bmi-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

             // JSONParser extracts text body and parses to JSONObject
             this.body = (JSONObject) new JSONParser().parse(response.body());
             this.status = HttpStatus.OK;  // 200 success
        }
        catch(Exception e) {  // capture failure info
            HashMap<String, String> status = new HashMap<>();
            status.put("status", "RapidApi failure: " + e);

            // Setup object for error
            this.body = (JSONObject) status;
            this.status = HttpStatus.INTERNAL_SERVER_ERROR; // 500 error
        }
        return new ResponseEntity<>(body, status);
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping( "/post")
    public ResponseEntity<Object> postPerson(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("name") String name,
                                             @RequestParam("dob") String dobString,
                                             @RequestParam("height") int height) {
        Date dob;
        try {
            dob = new SimpleDateFormat("MM-dd-yyyy").parse(dobString);
        } catch (Exception e) {
            return new ResponseEntity<>(dobString +" error; try MM-dd-yyyy", HttpStatus.BAD_REQUEST);
        }
        // A person object WITHOUT ID will create a new record with default roles as student
        Person person = new Person(email, password, name, dob, height);
        repository.save(person);
        return new ResponseEntity<>(email +" is created successfully", HttpStatus.CREATED);
    }

    /*
    The personSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personSearch(@RequestBody final Map<String,String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Person> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
    The personStats API adds stats by Date to Person table 
    */
    @PostMapping(value = "/setStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> personStats(@RequestBody final Map<String,Object> stat_map) {
        // find ID
        long id=Long.parseLong((String)stat_map.get("id"));  
        Optional<Person> optional = repository.findById((id));
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            for (Map.Entry<String,Object> entry : stat_map.entrySet())  {
                // Add all attribute other than "date" to the "attribute_map"
                if (!entry.getKey().equals("date") && !entry.getKey().equals("id"))
                    attributeMap.put(entry.getKey(), entry.getValue());
                // Add BMI info if weight is provided    
                if (entry.getKey().equals("weight"))
                    attributeMap.put("bmi", person.calcBMI(entry.getValue())); // BMI (number)
                    attributeMap.put("weightCategory", person.bmiClassification(person.calcBMI(entry.getValue())));  // weight classification based on BMI
            }

            // Set Date and Attributes to SQL HashMap
            Map<String, Map<String, Object>> date_map = new HashMap<>();
            String newDate = (String) stat_map.get("date");
            date_map.put(newDate, attributeMap);
            if(!(person.getStats().get(newDate) == null)){
                // replace if existing
                person.deleteStats(newDate);
                person.addStats(newDate, attributeMap);
            }
            else{ // no info for date yet
                // append if new
                person.addStats(newDate, attributeMap);
            }
            repository.save(person);  // conclude by writing the stats updates

            // return Person with update Stats
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        
    }
}
