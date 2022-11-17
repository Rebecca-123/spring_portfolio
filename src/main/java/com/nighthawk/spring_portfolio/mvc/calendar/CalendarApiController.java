package com.nighthawk.spring_portfolio.mvc.calendar;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Calendar API
@RestController
@RequestMapping("/api/calendar")
public class CalendarApiController {

    /** GET isLeapYear endpoint
     * ObjectMapper throws exceptions on bad JSON
     *  @throws JsonProcessingException
     *  @throws JsonMappingException
     */

     /** Endpoint
      * /api/calendar/isLeapYear/2022
      * Returns: {"year":2020,"isLeapYear":false}
      */
    @GetMapping("/isLeapYear/{year}")
    public ResponseEntity<JsonNode> getIsLeapYear(@PathVariable int year) throws JsonMappingException, JsonProcessingException {
      // Backend Year Object
      Year year_obj = new Year();
      year_obj.setYear(year);  // evaluates Leap Year

      // Turn Year Object into JSON
      ObjectMapper mapper = new ObjectMapper(); 
      JsonNode json = mapper.readTree(year_obj.isLeapYearToString()); // this requires exception handling

      return ResponseEntity.ok(json);  // JSON response, see ExceptionHandlerAdvice for throws
    }

     /** Endpoint
      * /api/calendar/leapYearCount/1600/1699
      * Returns: {"year":16001699,"isLeapYear":25}
      */
    @GetMapping("/leapYearCount/{year1}/{year2}")
    public ResponseEntity<JsonNode> getLeapYearCount2(@PathVariable int year1, @PathVariable int year2) throws JsonMappingException, JsonProcessingException {
      Year year_obj = new Year();
      year_obj.setYears(year1, year2);
      ObjectMapper mapper = new ObjectMapper(); 
      JsonNode json = mapper.readTree(year_obj.leapYearCountToString());
      return ResponseEntity.ok(json); 
    }

    /** Endpoint
      * /api/calendar/dayOfWeek/11/16/2022
      * Returns: {"Date":11162022,"dayOfWeek":3}
      */
    @GetMapping("/dayOfWeek/{month}/{day}/{year}")
    public ResponseEntity<JsonNode> getDayOfWeek(@PathVariable int month, @PathVariable int day, @PathVariable int year) throws JsonMappingException, JsonProcessingException {
      Year year_obj = new Year();
      year_obj.setTheDayOfWeek(month, day, year);  // evaluates day of week
      ObjectMapper mapper = new ObjectMapper(); 
      JsonNode json = mapper.readTree(year_obj.dayOfWeekToString());
      return ResponseEntity.ok(json);
    }
}