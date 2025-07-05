package com.sportygroup.feednormalizer.testconfiguration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public abstract class BaseRestApiTest {
  static {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @LocalServerPort private int port;
  protected RequestSpecification spec;

  @BeforeEach
  public void setup() {
    spec =
        given()
            .baseUri("http://localhost")
            .port(port)
            .auth()
            .preemptive()
            .basic("testapiuser", "testapipassword")
            .contentType(ContentType.JSON);
  }

  protected Stream<Arguments> requestSpecificationsForValidBodyTests() {
    RequestSpecification valid =
        given()
            .baseUri("http://localhost")
            .port(port)
            .auth()
            .preemptive()
            .basic("testapiuser", "testapipassword")
            .contentType(ContentType.JSON);

    RequestSpecification noContentType =
        given()
            .baseUri("http://localhost")
            .port(port)
            .auth()
            .preemptive()
            .basic("testapiuser", "testapipassword");

    RequestSpecification noAuth =
        given().baseUri("http://localhost").port(port).contentType(ContentType.JSON);

    return Stream.of(
        arguments("Valid auth + Content-type", valid, HttpStatus.OK.value()),
        arguments("Missing Content-Type", noContentType, HttpStatus.BAD_REQUEST.value()),
        arguments("Missing auth headers", noAuth, HttpStatus.UNAUTHORIZED.value()));
  }

  protected Stream<Arguments> betaProviderMessages() {
    return Stream.of(
        arguments(
            "Beta provider odds change message",
            """
            {
              "type": "ODDS",
              "event_id": "ev456",
              "odds": {
                "home": 1.95,
                "draw": 3.2,
                "away": 4.0
              }
            }
            """),
        arguments(
            "Beta provider bet settlement message",
            """
            {
              "type": "SETTLEMENT",
              "event_id": "ev456",
              "result": "away"
            }
            """));
  }
}
