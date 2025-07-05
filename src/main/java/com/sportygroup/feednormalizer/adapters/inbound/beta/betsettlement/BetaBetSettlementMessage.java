package com.sportygroup.feednormalizer.adapters.inbound.beta.betsettlement;

import static com.sportygroup.feednormalizer.domain.message.Outcome.AWAY;
import static com.sportygroup.feednormalizer.domain.message.Outcome.DRAW;
import static com.sportygroup.feednormalizer.domain.message.Outcome.HOME;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sportygroup.feednormalizer.adapters.inbound.IncomingMessage;
import com.sportygroup.feednormalizer.adapters.inbound.MessageUnprocessableException;
import com.sportygroup.feednormalizer.domain.message.BetSettlementMessage;
import com.sportygroup.feednormalizer.domain.message.Outcome;

public record BetaBetSettlementMessage(
    @JsonProperty("type") String type,
    @JsonProperty("event_id") String eventId,
    @JsonProperty("result") String result)
    implements IncomingMessage<BetSettlementMessage> {
  @Override
  public BetSettlementMessage toStandardMessage() {
    return new BetSettlementMessage(eventId, fromCode(result));
  }

  public static Outcome fromCode(String code) {
    return switch (code) {
      case "home" -> HOME;
      case "draw" -> DRAW;
      case "away" -> AWAY;
      default -> throw new MessageUnprocessableException("Unknown outcome code: " + code);
    };
  }
}
