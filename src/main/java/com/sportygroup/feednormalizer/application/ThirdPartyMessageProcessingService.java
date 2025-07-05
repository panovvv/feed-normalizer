package com.sportygroup.feednormalizer.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.feednormalizer.application.ports.IncomingMessageNormalizer;
import com.sportygroup.feednormalizer.application.ports.IncomingMessageNormalizerFactory;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import com.sportygroup.feednormalizer.application.ports.MessageQueue;
import com.sportygroup.feednormalizer.application.ports.StandardMessageQueueFactory;
import com.sportygroup.feednormalizer.domain.message.StandardMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThirdPartyMessageProcessingService {
  private final IncomingMessageNormalizerFactory normalizerFactory;
  private final StandardMessageQueueFactory messageQueueFactory;

  public void processMessage(MessageOrigin messageOrigin, JsonNode payload) {
    IncomingMessageNormalizer messageNormalizer = normalizerFactory.get(messageOrigin, payload);
    StandardMessage standardMessage = messageNormalizer.toStandardMessage(payload);

    MessageQueue<StandardMessage> queue = messageQueueFactory.getFor(standardMessage);
    queue.queueMessage(standardMessage);
  }
}
