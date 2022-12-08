package com.nighthawk.spring_portfolio.mvc.lightboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/light")
public class LightApiController {
    @GetMapping("/random")
    public ResponseEntity<JsonNode> getLight() throws JsonMappingException, JsonProcessingException {
        Light light = new Light();

        // turn calculator object into JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(light.toString()); // this requires exception handling

      return ResponseEntity.ok(json);  // JSON response, see ExceptionHandlerAdvice for throws
    }
    @GetMapping("/board")
    public ResponseEntity<JsonNode> getBoard() throws JsonMappingException, JsonProcessingException {
        LightBoard lightBoard = new LightBoard(10, 10);

        // turn calculator object into JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(lightBoard.toString()); // this requires exception handling

      return ResponseEntity.ok(json);  // JSON response, see ExceptionHandlerAdvice for throws
    }
}
