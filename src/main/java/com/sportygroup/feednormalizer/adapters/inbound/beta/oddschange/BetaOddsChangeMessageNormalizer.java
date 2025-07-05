package com.sportygroup.feednormalizer.adapters.inbound.beta.oddschange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.feednormalizer.adapters.inbound.AbstractMessageNormalizer;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import com.sportygroup.feednormalizer.application.ports.Provider;
import com.sportygroup.feednormalizer.domain.message.OddsChangeMessage;
import org.springframework.stereotype.Component;

@Component
public class BetaOddsChangeMessageNormalizer
    extends AbstractMessageNormalizer<BetaOddsChangeMessage, OddsChangeMessage> {
  public BetaOddsChangeMessageNormalizer(ObjectMapper mapper) {
    super(mapper, BetaOddsChangeMessage.class);
  }

  @Override
  public boolean supports(MessageOrigin messageOrigin, JsonNode payload) {
    if (messageOrigin == null
        || messageOrigin.provider() == null
        || messageOrigin.provider() != Provider.BETA) {
      return false;
    }
    JsonNode t = payload.path("type");
    return t != null && t.isTextual() && "ODDS".equals(t.asText());
  }
}
