package com.sportygroup.feednormalizer.adapters.inbound.beta.betsettlement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.feednormalizer.adapters.inbound.AbstractMessageNormalizer;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import com.sportygroup.feednormalizer.application.ports.Provider;
import com.sportygroup.feednormalizer.domain.message.BetSettlementMessage;
import org.springframework.stereotype.Component;

@Component
public class BetaBetsSettlementMessageNormalizer
    extends AbstractMessageNormalizer<BetaBetSettlementMessage, BetSettlementMessage> {
  public BetaBetsSettlementMessageNormalizer(ObjectMapper mapper) {
    super(mapper, BetaBetSettlementMessage.class);
  }

  @Override
  public boolean supports(MessageOrigin messageOrigin, JsonNode payload) {
    if (messageOrigin == null
        || messageOrigin.provider() == null
        || messageOrigin.provider() != Provider.BETA) {
      return false;
    }
    JsonNode t = payload.path("type");
    return t != null && t.isTextual() && "SETTLEMENT".equals(t.asText());
  }
}
