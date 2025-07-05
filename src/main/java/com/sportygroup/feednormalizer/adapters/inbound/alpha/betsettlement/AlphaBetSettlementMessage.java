package com.sportygroup.feednormalizer.adapters.inbound.alpha.betsettlement;

import static com.sportygroup.feednormalizer.domain.message.Outcome.AWAY;
import static com.sportygroup.feednormalizer.domain.message.Outcome.DRAW;
import static com.sportygroup.feednormalizer.domain.message.Outcome.HOME;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sportygroup.feednormalizer.adapters.inbound.IncomingMessage;
import com.sportygroup.feednormalizer.adapters.inbound.MessageUnprocessableException;
import com.sportygroup.feednormalizer.domain.message.BetSettlementMessage;
import com.sportygroup.feednormalizer.domain.message.Outcome;

public record AlphaBetSettlementMessage(
    @JsonProperty("msg_type") String msgType,
    @JsonProperty("event_id") String eventId,
    @JsonProperty("outcome") String outcome)
    implements IncomingMessage<BetSettlementMessage> {
  @Override
  public BetSettlementMessage toStandardMessage() {
    return new BetSettlementMessage(eventId, fromCode(outcome));
  }

  public static Outcome fromCode(String code) {
    return switch (code) {
      case "1" -> HOME;
      case "X", "x" -> DRAW;
      case "2" -> AWAY;
      default -> throw new MessageUnprocessableException("Unknown outcome code: " + code);
    };
  }
}
