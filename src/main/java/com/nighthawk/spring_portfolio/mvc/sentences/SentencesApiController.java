package com.nighthawk.spring_portfolio.mvc.sentences;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/sentences")
public class SentencesApiController {
    @GetMapping("/{sentence}")
    public ResponseEntity<JsonNode> getResult(@PathVariable String sentence) throws JsonMappingException, JsonProcessingException {
        Sentences mySentence = new Sentences(sentence); // create object, will run constructor, make tokens, rpn, and result

        // turn calculator object into JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(mySentence.apiToString()); // this requires exception handling

      return ResponseEntity.ok(json);  // JSON response, see ExceptionHandlerAdvice for throws
    }
}
