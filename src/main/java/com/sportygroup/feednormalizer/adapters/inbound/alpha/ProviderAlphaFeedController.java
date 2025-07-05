package com.sportygroup.feednormalizer.adapters.inbound.alpha;

import static com.sportygroup.feednormalizer.adapters.inbound.configuration.SwaggerApiTags.PROVIDER_ALPHA;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.feednormalizer.adapters.inbound.alpha.betsettlement.AlphaBetSettlementMessage;
import com.sportygroup.feednormalizer.adapters.inbound.alpha.oddschange.AlphaOddsChangeMessage;
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
public class ProviderAlphaFeedController {

  private final ThirdPartyMessageProcessingService service;

  @PostMapping("/provider-alpha/feed")
  @Operation(
      summary = "Provider Alpha feed",
      tags = {PROVIDER_ALPHA})
  @RequestBody(
      description = "Either an Odds Change or a Bet Settlement message",
      required = true,
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema =
                  @Schema(
                      oneOf = {AlphaOddsChangeMessage.class, AlphaBetSettlementMessage.class},
                      description = "JSON payload matching one of the two message schemas"),
              examples = {
                @ExampleObject(
                    name = "ODDS_CHANGE message example",
                    summary = "ODDS_CHANGE message",
                    value =
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
                        """,
                    description =
                        "`outcome` in `values` can only take one of the following values: 1, X or 2"),
                @ExampleObject(
                    name = "BET_SETTLEMENT message example",
                    summary = "BET_SETTLEMENT message",
                    value =
                        """
                        {
                          "msg_type": "settlement",
                          "event_id": "ev123",
                          "outcome": "1"
                        }
                        """,
                    description = "`outcome` can only take one of the following values: 1, X or 2")
              }))
  public ResponseEntity<Void> processRequest(
      @org.springframework.web.bind.annotation.RequestBody JsonNode node) {
    service.processMessage(new MessageOrigin(Provider.ALPHA), node);
    return ResponseEntity.ok().build();
  }
}
