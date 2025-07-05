package com.sportygroup.feednormalizer.adapters.outbound.queue;

import com.sportygroup.feednormalizer.application.ports.MessageQueue;
import com.sportygroup.feednormalizer.domain.message.OddsChangeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingOddsChangeMessageQueue implements MessageQueue<OddsChangeMessage> {

  @Override
  public Class<OddsChangeMessage> getMessageType() {
    return OddsChangeMessage.class;
  }

  @Override
  public void queueMessage(OddsChangeMessage message) {
    log.info("Normalized OddsChange: eventId={} odds={}", message.eventId(), message.odds());
  }
}
