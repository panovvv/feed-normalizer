package com.sportygroup.feednormalizer.adapters.inbound.beta;

import static com.sportygroup.feednormalizer.adapters.inbound.configuration.SwaggerApiTags.PROVIDER_BETA;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.feednormalizer.adapters.inbound.beta.betsettlement.BetaBetSettlementMessage;
import com.sportygroup.feednormalizer.adapters.inbound.beta.oddschange.BetaOddsChangeMessage;
import com.sportygroup.feednormalizer.application.ThirdPartyMessageProcessingService;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import com.sportygroup.feednormalizer.application.ports.Provider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProviderBetaFeedController {

  private final ThirdPartyMessageProcessingService service;

  @Operation(
      summary = "Provider Beta feed",
      tags = {PROVIDER_BETA})
  @RequestBody(
      description = "Either an Odds Change or a Bet Settlement message",
      required = true,
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema =
                  @Schema(
                      oneOf = {BetaOddsChangeMessage.class, BetaBetSettlementMessage.class},
                      description = "JSON payload matching one of the two message schemas"),
              examples = {
                @ExampleObject(
                    name = "ODDS_CHANGE message example",
                    summary = "ODDS_CHANGE message",
                    value =
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
                        """,
                    description =
                        "`outcome` in `odds` can only take one of the following values: home, draw or away"),
                @ExampleObject(
                    name = "BET_SETTLEMENT message example",
                    summary = "BET_SETTLEMENT message",
                    value =
                        """
                        {
                          "type": "SETTLEMENT",
                          "event_id": "ev456",
                          "result": "away"
                        }
                        """,
                    description =
                        "`outcome` can only take one of the following values: home, draw or away")
              }))
  @PostMapping("/provider-beta/feed")
  public ResponseEntity<Void> processRequest(
      @org.springframework.web.bind.annotation.RequestBody JsonNode node) {
    service.processMessage(new MessageOrigin(Provider.BETA), node);
    return ResponseEntity.ok().build();
  }
}
