package com.nighthawk.spring_portfolio.mvc.person;
import com.nighthawk.spring_portfolio.mvc.person.StepTracker;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/*
Person is a POJO, Plain Old Java Object.
First set of annotations add functionality to POJO
--- @Setter @Getter @ToString @NoArgsConstructor @RequiredArgsConstructor
The last annotation connect to database
--- @Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TypeDef(name="json", typeClass = JsonType.class)
public class Person {
    
    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Size(min=5)
    @Column(unique=true)
    @Email
    private String email;

    @NotEmpty
    private String password;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    

    /* HashMap is used to store JSON for daily "stats"
    "stats": {
        "2022-11-13": {
            "calories": 2200,
            "steps": 8000
        }
    }
    */
    @Type(type="json")
    @Column(columnDefinition = "jsonb")
    private Map<String,Map<String, Object>> stats = new HashMap<>(); 
    
    // inches
    private int height;

    // Step Tracker
    public int minSteps;

    // Constructor used when building object from an API
    public Person(String email, String password, String name, Date dob, int height, int minSteps) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.height = height;
        this.minSteps = minSteps;
    }

    // A custom getter to return age from dob attribute
    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears(); }
        return -1;
    }

    public void addStats(String newDate, Map<String, Object> attributeMap){
        this.stats.put(newDate, attributeMap);
    }

    public void deleteStats(String oldDate){
        this.stats.remove(oldDate);
    }

    private JSONObject body; // last run result
    private HttpStatus status; // last run status
    public Object calcBMI(Object weight){
        String url = "https://body-mass-index-bmi-calculator.p.rapidapi.com/imperial?weight=" + weight + "&height=" + this.height;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", "bdd7c1e507msh4b0a5adae74c68cp127439jsn94bd137c3d62")
                .header("X-RapidAPI-Host", "body-mass-index-bmi-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

             // JSONParser extracts text body and parses to JSONObject
             this.body = (JSONObject) new JSONParser().parse(response.body());
             this.status = HttpStatus.OK;  //200 success
        }
        catch(Exception e) {  // capture failure info
            HashMap<String, String> status = new HashMap<>();
                status.put("status", "RapidApi failure: " + e);

                //Setup object for error
                this.body = (JSONObject) status;
                this.status = HttpStatus.INTERNAL_SERVER_ERROR; //500 error
        }
        return this.body.get("bmi");
    }

    private JSONObject body2; // last run result
    private HttpStatus status2; // last run status
    public Object bmiClassification(Object bmi){
        String url = "https://body-mass-index-bmi-calculator.p.rapidapi.com/weight-category?bmi=" + bmi;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", "bdd7c1e507msh4b0a5adae74c68cp127439jsn94bd137c3d62")
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
        return this.body2.get("weightCategory");
    }

    // Display class attributes
    public String personToString(){
        return String.format("{\"id\": %s, \"email\": %s, \"password\": %s, \"name\": %s, \"dob\": %s, \"stats\": %s, \"height\": %s}", this.id, this.email, this.password, this.name, this.dob, this.stats, this.height);
    }

    // Tester method
    public static void main(String[] args) {
        // no-arg
        Person p1 = new Person();
        System.out.println(p1.personToString());

        // default time zone
	    ZoneId defaultZoneId = ZoneId.systemDefault();

        // all-arg: id, email, password, name, dob, stats
        LocalDate dob = LocalDate.of(2000, 1, 1);
        // convert LocalDate to Date object
        Date date = Date.from(dob.atStartOfDay(defaultZoneId).toInstant());

        Map<String,Map<String, Object>> stats = new HashMap<>();
        // stats.put("2000-01-01", null);
       
        JSONObject b1 = new JSONObject();
        JSONObject b2 = new JSONObject();
        HttpStatus h1 = null;
        HttpStatus h2 = null;

        Person p2 = new Person(1l, "person@gmail.com", "12345", "Person", date, stats, 60, 0, b1, h1, b2, h2);
        System.out.println(p2.personToString());

        System.out.println(p2.getStats().get("date"));
    }

}