package com.sportygroup.feednormalizer.adapters.inbound.alpha;

import com.sportygroup.feednormalizer.testconfiguration.BaseRestApiTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProviderAlphaFeedControllerTest extends BaseRestApiTest {
  private static final String ENDPOINT_PROVIDER_ALPHA_FEED = "/provider-alpha/feed";

  @Test
  void emptyBody_returns400BadRequest() {
    spec.body("")
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void malformedJson_returns400BadRequest() {
    spec.body("{invalid json")
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void unknownMsgType_returns422UnprocessableEntity() {
    spec.body(
            """
            {
              "msg_type":"unknown"
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }

  @Test
  void correctMsgTypeButBodyNotCorrespondingToMsgType_returns422UnprocessableEntity() {
    spec.body(
            """
            {
              "msg_type":"odds_update",
              "incorrect field for odds_update msg": {
                "bla": "string"
              }
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }

  @Test
  void correctMessageButMsgTypeCaseNotMatching_returns422UnprocessableEntity() {
    spec.body(
            """
            {
              "msg_type": "Odds_Update",
              "event_id": "ev123",
              "values": {
                "1": 2.0,
                "X": 3.1,
                "2": 3.8
              }
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }

  @Test
  void correctMessageForOddsUpdateButEmptyOdds_returns422UnprocessableEntity() {
    spec.body(
            """
            {
              "msg_type": "odds_update",
              "event_id": "ev123",
              "values": {
              }
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }

  @Test
  void correctMessageForOddsUpdateButUnknownOddsCode_returns422UnprocessableEntity() {
    spec.body(
            """
            {
              "msg_type": "odds_update",
              "event_id": "ev123",
              "values": {
                "wut": 2.0,
                "X": 3.1,
                "2": 3.8
              }
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }

  @Test
  void correctMessageForBetSettlementButUnknownOutcomeCode_returns422UnprocessableEntity() {
    spec.body(
            """
            {
              "msg_type": "settlement",
              "event_id": "ev123",
              "outcome": "wut"
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("requestSpecificationsForValidBodyTests")
  void correctMsgTypeAndBodyForOddsUpdate_returnsOkOnlyWithCorrectAuthenticatedRequest(
      String testName, RequestSpecification specOverride, int expectedStatus) {
    specOverride
        .body(
            """
            {
              "msg_type": "odds_update",
              "event_id": "ev123",
              "values": {
                "1": 2.0,
                "X": 3.1,
                "2": 3.8
              }
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(expectedStatus);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("requestSpecificationsForValidBodyTests")
  void correctMsgTypeAndBodyForBetSettlement_returnsOkOnlyWithCorrectAuthenticatedRequest(
      String testName, RequestSpecification specOverride, int expectedStatus) {
    specOverride
        .body(
            """
            {
              "msg_type": "settlement",
              "event_id": "ev123",
              "outcome": "1"
            }
            """)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(expectedStatus);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("betaProviderMessages")
  void correctBetaProviderMessagesSentToAlphaEndpoint_returns422UnprocessableEntity(
      String testName, String payload) {
    spec.body(payload)
        .when()
        .post(ENDPOINT_PROVIDER_ALPHA_FEED)
        .then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
  }
}
