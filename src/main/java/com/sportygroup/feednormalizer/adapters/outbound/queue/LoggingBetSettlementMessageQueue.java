package com.sportygroup.feednormalizer.adapters.outbound.queue;

import com.sportygroup.feednormalizer.application.ports.MessageQueue;
import com.sportygroup.feednormalizer.domain.message.BetSettlementMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingBetSettlementMessageQueue implements MessageQueue<BetSettlementMessage> {

  @Override
  public Class<BetSettlementMessage> getMessageType() {
    return BetSettlementMessage.class;
  }

  @Override
  public void queueMessage(BetSettlementMessage message) {
    log.info(
        "Normalized BetSettlement: eventId={} outcome={}", message.eventId(), message.outcome());
  }
}
