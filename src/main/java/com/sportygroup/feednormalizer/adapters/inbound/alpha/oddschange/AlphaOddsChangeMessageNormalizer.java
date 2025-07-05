package com.sportygroup.feednormalizer.adapters.inbound.alpha.oddschange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.feednormalizer.adapters.inbound.AbstractMessageNormalizer;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import com.sportygroup.feednormalizer.application.ports.Provider;
import com.sportygroup.feednormalizer.domain.message.OddsChangeMessage;
import org.springframework.stereotype.Component;

@Component
public class AlphaOddsChangeMessageNormalizer
    extends AbstractMessageNormalizer<AlphaOddsChangeMessage, OddsChangeMessage> {
  public AlphaOddsChangeMessageNormalizer(ObjectMapper mapper) {
    super(mapper, AlphaOddsChangeMessage.class);
  }

  @Override
  public boolean supports(MessageOrigin messageOrigin, JsonNode payload) {
    if (messageOrigin == null
        || messageOrigin.provider() == null
        || messageOrigin.provider() != Provider.ALPHA) {
      return false;
    }
    JsonNode t = payload.path("msg_type");
    return t != null && t.isTextual() && "odds_update".equals(t.asText());
  }
}
