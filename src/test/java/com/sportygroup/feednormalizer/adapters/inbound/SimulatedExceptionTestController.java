package com.sportygroup.feednormalizer.adapters.inbound;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class SimulatedExceptionTestController {
  @PostMapping("/throw-an-exception")
  public ResponseEntity<Void> processRequest() {
    throw new RuntimeException("Simulated exception");
  }
}
