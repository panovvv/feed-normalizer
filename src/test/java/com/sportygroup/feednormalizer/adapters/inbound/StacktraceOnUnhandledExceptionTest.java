package com.sportygroup.feednormalizer.adapters.inbound;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

import com.sportygroup.feednormalizer.testconfiguration.BaseRestApiTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StacktraceOnUnhandledExceptionTest extends BaseRestApiTest {
  private static final String ENDPOINT_THAT_THROWS = "/throw-an-exception";

  @Test
  void whenEndpointReturns500_thenNoStackTraceExposed() {
    spec.when()
        .post(ENDPOINT_THAT_THROWS)
        .then()
        .statusCode(500)
        .body("$", not(hasKey("stackTrace")))
        .body(not(containsString("Exception")))
        .body(not(containsString("\tat ")));
  }
}
