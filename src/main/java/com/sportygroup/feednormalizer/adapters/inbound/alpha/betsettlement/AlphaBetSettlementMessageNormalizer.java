package com.sportygroup.feednormalizer.adapters.inbound.alpha.betsettlement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.feednormalizer.adapters.inbound.AbstractMessageNormalizer;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import com.sportygroup.feednormalizer.application.ports.Provider;
import com.sportygroup.feednormalizer.domain.message.BetSettlementMessage;
import org.springframework.stereotype.Component;

@Component
public class AlphaBetSettlementMessageNormalizer
    extends AbstractMessageNormalizer<AlphaBetSettlementMessage, BetSettlementMessage> {

  public AlphaBetSettlementMessageNormalizer(ObjectMapper mapper) {
    super(mapper, AlphaBetSettlementMessage.class);
  }

  @Override
  public boolean supports(MessageOrigin messageOrigin, JsonNode payload) {
    if (messageOrigin == null
        || messageOrigin.provider() == null
        || messageOrigin.provider() != Provider.ALPHA) {
      return false;
    }
    JsonNode t = payload.path("msg_type");
    return t != null && t.isTextual() && "settlement".equals(t.asText());
  }
}
