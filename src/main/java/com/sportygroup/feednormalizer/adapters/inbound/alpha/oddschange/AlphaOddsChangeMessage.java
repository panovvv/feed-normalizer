package com.sportygroup.feednormalizer.adapters.inbound.alpha.oddschange;

import static com.sportygroup.feednormalizer.domain.message.Outcome.AWAY;
import static com.sportygroup.feednormalizer.domain.message.Outcome.DRAW;
import static com.sportygroup.feednormalizer.domain.message.Outcome.HOME;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sportygroup.feednormalizer.adapters.inbound.IncomingMessage;
import com.sportygroup.feednormalizer.adapters.inbound.MessageUnprocessableException;
import com.sportygroup.feednormalizer.domain.message.OddsChangeMessage;
import com.sportygroup.feednormalizer.domain.message.Outcome;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record AlphaOddsChangeMessage(
    @JsonProperty("msg_type") String msgType,
    @JsonProperty("event_id") String eventId,
    @JsonProperty("values") Map<String, BigDecimal> values)
    implements IncomingMessage<OddsChangeMessage> {
  public OddsChangeMessage toStandardMessage() {
    Map<Outcome, BigDecimal> oddsMap =
        Optional.ofNullable(values)
            .map(
                it ->
                    it.entrySet().stream()
                        .collect(Collectors.toMap(e -> fromCode(e.getKey()), Map.Entry::getValue)))
            .orElse(Map.of());
    return new OddsChangeMessage(eventId, oddsMap);
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
